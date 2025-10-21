package com.groq.sdk.core;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groq.sdk.client.GroqClient;
import com.groq.sdk.exceptions.GroqException;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.files.FilePart;
import com.groq.sdk.util.JsonUtils;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Base client providing core HTTP functionality for Groq API interactions.
 * Handles request building, response processing, retry logic, authentication, and error handling.
 * Supports both synchronous requests and multipart form data for file uploads.
 * 
 * <p>This class serves as the foundation for all API client implementations
 * and provides common functionality like:</p>
 * <ul>
 *   <li>Request/response processing</li>
 *   <li>Authentication header management</li>
 *   <li>Retry logic with exponential backoff</li>
 *   <li>Error handling and exception creation</li>
 *   <li>Multipart form data support</li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 21-Oct-2025
 * @version 1.0.0
 * @see GroqClient
 */
public abstract class BaseClient {
    protected final OkHttpClient httpClient;
    protected final ObjectMapper objectMapper;
    protected final String baseUrl;
    protected final String apiKey;
    protected final Map<String, String> defaultHeaders;
    protected final Map<String, Object> defaultQueryParams;
    
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);
    protected static final int DEFAULT_MAX_RETRIES = 2;
    
    /**
     * Constructs a new BaseClient with the specified builder configuration.
     * 
     * @param builder the builder containing client configuration
     */
    public BaseClient(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.apiKey = builder.apiKey;
        this.defaultHeaders = Collections.unmodifiableMap(new HashMap<>(builder.defaultHeaders));
        this.defaultQueryParams = Collections.unmodifiableMap(new HashMap<>(builder.defaultQueryParams));
        this.objectMapper = JsonUtils.getObjectMapper();
        
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(builder.timeout)
                .readTimeout(builder.timeout)
                .writeTimeout(builder.timeout)
                .addInterceptor(new AuthInterceptor(apiKey))
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new RetryInterceptor(builder.maxRetries));
                
        this.httpClient = httpClientBuilder.build();
    }
    
    /**
     * Builds the complete URL with query parameters.
     * 
     * @param path the API endpoint path
     * @param queryParams optional query parameters to include
     * @return the complete HttpUrl with query parameters
     * @throws IllegalArgumentException if base URL or path is invalid
     * @throws RuntimeException if URL building fails
     */
    protected HttpUrl buildUrl(String path, Map<String, Object> queryParams) {
        try {
            HttpUrl base = HttpUrl.parse(baseUrl + path);
            if (base == null) {
                throw new IllegalArgumentException("Invalid base URL or path: " + baseUrl + path);
            }
            
            HttpUrl.Builder urlBuilder = base.newBuilder();
            
            defaultQueryParams.forEach((key, value) -> urlBuilder.addQueryParameter(key, String.valueOf(value)));
            
            if (queryParams != null) {
                queryParams.forEach((key, value) -> {
                    if (value instanceof Collection) {
                        for (Object item : (Collection<?>) value) {
                            urlBuilder.addQueryParameter(key, String.valueOf(item));
                        }
                    } else {
                        urlBuilder.addQueryParameter(key, String.valueOf(value));
                    }
                });
            }
            
            return urlBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build URL for path: " + path, e);
        }
    }
    
    /**
     * Builds the HTTP request with headers, body, and authentication.
     *
     * @param method the HTTP method (GET, POST, PUT, PATCH, DELETE)
     * @param path the API endpoint path
     * @param queryParams optional query parameters
     * @param headers optional HTTP headers
     * @param body the request body object
     * @return the configured Request.Builder
     * @throws IOException if request body serialization fails
     * @throws IllegalArgumentException for unsupported HTTP methods with body
     */
    protected Request.Builder buildRequest(String method, String path, 
                                         Map<String, Object> queryParams,
                                         Map<String, String> headers,
                                         Object body) throws IOException {
        HttpUrl url = buildUrl(path, queryParams);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        
        defaultHeaders.forEach(requestBuilder::addHeader);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        
        if (body instanceof MultipartBody) {
            requestBuilder.method(method, (RequestBody) body);
        } else if (body != null) {
            MediaType mediaType = MediaType.parse("application/json");
            String jsonBody = objectMapper.writeValueAsString(body);
            RequestBody requestBody = RequestBody.create(jsonBody, mediaType);
            
            switch (method.toUpperCase()) {
                case "POST":
                    requestBuilder.post(requestBody);
                    break;
                case "PUT":
                    requestBuilder.put(requestBody);
                    break;
                case "PATCH":
                    requestBuilder.patch(requestBody);
                    break;
                case "DELETE":
                    requestBuilder.delete(requestBody);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported method for body: " + method);
            }
        } else if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || 
                   "PATCH".equalsIgnoreCase(method)) {
            requestBuilder.method(method, RequestBody.create(new byte[0], null));
        } else {
            requestBuilder.method(method, null);
        }
        
        return requestBuilder;
    }
    
    /**
     * Executes the HTTP request and processes the response.
     *
     * @param <T> the type of response data
     * @param request the HTTP request to execute
     * @param responseType the class type of the response
     * @return the processed response wrapped in GroqResponse
     * @throws IOException if the request execution fails
     */
    protected <T> GroqResponse<T> executeRequest(Request request, Class<T> responseType) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            return processResponse(response, responseType);
        } catch (IOException e) {
            throw new IOException("HTTP request execution failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Processes the HTTP response and handles errors.
     *
     * @param <T> the type of response data
     * @param response the HTTP response to process
     * @param responseType the class type of the response
     * @return the processed response wrapped in GroqResponse
     * @throws IOException if response processing fails
     * @throws GroqException if the response indicates an error
     */
    protected <T> GroqResponse<T> processResponse(Response response, Class<T> responseType) throws IOException {
        if (!response.isSuccessful()) {
            throw createStatusError(response);
        }
        
        ResponseBody body = response.body();
        if (body == null) {
            return new GroqResponse<>(null, response.headers().toMultimap(), response.code());
        }
        
        try {
            String responseBody = body.string();
            T data = null;
            
            if (responseType != null && !responseBody.isEmpty()) {
                if (responseType == String.class) {
                    data = responseType.cast(responseBody);
                } else {
                    data = objectMapper.readValue(responseBody, responseType);
                }
            }
            
            return new GroqResponse<>(data, response.headers().toMultimap(), response.code());
        } catch (IOException e) {
            throw new IOException("Failed to parse response body: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates appropriate exception based on HTTP status code.
     *
     * @param response the HTTP response containing error information
     * @return a GroqException with detailed error information
     * @throws IOException if response body reading fails
     */
    protected GroqException createStatusError(Response response) throws IOException {
        String message = "HTTP " + response.code() + " Error";
        String body = response.body() != null ? response.body().string() : null;
        
        if (body != null && !body.trim().isEmpty()) {
            try {
                Map<String, Object> errorData = objectMapper.readValue(body, Map.class);
                if (errorData.containsKey("error") && errorData.get("error") instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> error = (Map<String, Object>) errorData.get("error");
                    if (error.containsKey("message")) {
                        message = String.valueOf(error.get("message"));
                    }
                }
            } catch (Exception e) {
                message = body.length() > 200 ? body.substring(0, 200) + "..." : body;
            }
        }
        
        return new GroqException(message, response.code(), body);
    }
    
    /**
     * Creates multipart form data for file uploads.
     * 
     * @param formData the form data containing file parts and other fields
     * @return the constructed MultipartBody
     */
    public MultipartBody createMultipartBody(Map<String, Object> formData) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        
        formData.forEach((key, value) -> {
            if (value instanceof FilePart) {
                FilePart filePart = (FilePart) value;
                MediaType mediaType = filePart.mediaType != null ? 
                    filePart.mediaType : MediaType.parse("application/octet-stream");
                builder.addFormDataPart(key, filePart.filename, 
                    RequestBody.create(filePart.content, mediaType));
            } else {
                builder.addFormDataPart(key, String.valueOf(value));
            }
        });
        
        return builder.build();
    }
    
    /**
     * Base builder class for client configuration.
     * 
     * @param <T> the type of client being built
     */
    public abstract static class Builder<T extends BaseClient> {
        protected String baseUrl = "https://api.groq.com/";
        protected String apiKey;
        protected Duration timeout = DEFAULT_TIMEOUT;
        protected int maxRetries = DEFAULT_MAX_RETRIES;
        protected Map<String, String> defaultHeaders = new HashMap<>();
        protected Map<String, Object> defaultQueryParams = new HashMap<>();
        
        /**
         * Sets the API key for authentication.
         * 
         * @param apiKey the Groq API key
         * @return this builder instance for method chaining
         */
        public Builder<T> apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
        
        /**
         * Sets the base URL for API requests.
         * 
         * @param baseUrl the base URL (default: https://api.groq.com/)
         * @return this builder instance for method chaining
         */
        public Builder<T> baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        
        /**
         * Sets the timeout for HTTP requests.
         * 
         * @param timeout the timeout duration (default: 60 seconds)
         * @return this builder instance for method chaining
         */
        public Builder<T> timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }
        
        /**
         * Sets the maximum number of retry attempts for failed requests.
         * 
         * @param maxRetries the maximum retry count (default: 2)
         * @return this builder instance for method chaining
         */
        public Builder<T> maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }
        
        /**
         * Adds a default header to be included in all requests.
         * 
         * @param name the header name
         * @param value the header value
         * @return this builder instance for method chaining
         */
        public Builder<T> defaultHeader(String name, String value) {
            this.defaultHeaders.put(name, value);
            return this;
        }
        
        /**
         * Adds a default query parameter to be included in all requests.
         * 
         * @param name the parameter name
         * @param value the parameter value
         * @return this builder instance for method chaining
         */
        public Builder<T> defaultQueryParam(String name, Object value) {
            this.defaultQueryParams.put(name, value);
            return this;
        }
        
        /**
         * Builds the client instance.
         * 
         * @return the configured client instance
         */
        public abstract T build();
    }
    
    /**
     * Interceptor for adding authentication headers.
     */
    private static class AuthInterceptor implements Interceptor {
        private final String apiKey;
        
        /**
         * Creates a new AuthInterceptor with the specified API key.
         * 
         * @param apiKey the Groq API key
         */
        AuthInterceptor(String apiKey) {
            this.apiKey = apiKey;
        }
        
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + apiKey)
                    .build();
            return chain.proceed(newRequest);
        }
    }
    
    /**
     * Interceptor for request/response logging.
     */
    private static class LoggingInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            
            long startTime = System.nanoTime();
            Response response = chain.proceed(request);
            long endTime = System.nanoTime();
            
            return response;
        }
    }
    
    /**
     * Interceptor for handling request retries with exponential back off.
     */
    private static class RetryInterceptor implements Interceptor {
        private final int maxRetries;
        
        /**
         * Creates a new RetryInterceptor with the specified maximum retry count.
         * 
         * @param maxRetries the maximum number of retry attempts
         */
        RetryInterceptor(int maxRetries) {
            this.maxRetries = maxRetries;
        }
        
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = null;
            IOException exception = null;
            
            for (int i = 0; i <= maxRetries; i++) {
                if (i > 0) {
                    try {
                        long delay = (long) Math.pow(2, i) * 500;
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Interrupted during retry", e);
                    }
                }
                
                try {
                    response = chain.proceed(request);
                    
                    if (response.code() < 500 && response.code() != 429) {
                        return response;
                    }
                    
                    if (response.body() != null) {
                        response.body().close();
                    }
                } catch (IOException e) {
                    exception = e;
                }
            }
            
            if (response != null) {
                return response;
            }
            
            throw exception != null ? exception : new IOException("Request failed after " + maxRetries + " retries");
        }
    }
}
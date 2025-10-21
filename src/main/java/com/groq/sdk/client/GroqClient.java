package com.groq.sdk.client;

import java.util.Collections;
import java.util.Map;

import com.groq.sdk.core.BaseClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.resources.AudioResource;
import com.groq.sdk.resources.BatchesResource;
import com.groq.sdk.resources.ChatResource;
import com.groq.sdk.resources.EmbeddingsResource;
import com.groq.sdk.resources.FilesResource;
import com.groq.sdk.resources.ModelsResource;

import okhttp3.MultipartBody;
import okhttp3.Request;

/**
 * Main client for interacting with the Groq API.
 * Provides access to all API resources including chat, embeddings, audio, batches, files, and models.
 * Handles authentication, request building, and response processing with comprehensive error handling.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * GroqClient client = GroqClient.builder()
 *     .apiKey("your-api-key")
 *     .timeout(Duration.ofSeconds(30))
 *     .build();
 * 
 * GroqResponse<ModelList> response = client.models().list();
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 23-Aug-2025
 * @version 1.0.0
 * @see BaseClient
 * @see ChatResource
 * @see ModelsResource
 * @see EmbeddingsResource
 * @see AudioResource
 * @see BatchesResource
 * @see FilesResource
 */
public class GroqClient extends BaseClient {
    private final ChatResource chat;
    private final ModelsResource models;
    private final EmbeddingsResource embeddings;
    private final AudioResource audio;
    private final BatchesResource batches;
    private final FilesResource files;
    
    private GroqClient(Builder builder) {
        super(builder);
        this.chat = new ChatResource(this);
        this.models = new ModelsResource(this);
        this.embeddings = new EmbeddingsResource(this);
        this.audio = new AudioResource(this);
        this.batches = new BatchesResource(this);
        this.files = new FilesResource(this);
    }
    
    /**
     * Gets the chat resource for performing chat completions.
     * 
     * @return the chat resource instance
     * @see ChatResource
     */
    public ChatResource chat() {
        return chat;
    }
    
    /**
     * Gets the models resource for listing available models.
     * 
     * @return the models resource instance
     * @see ModelsResource
     */
    public ModelsResource models() {
        return models;
    }
    
    /**
     * Gets the embeddings resource for generating text embeddings.
     * 
     * @return the embeddings resource instance
     * @see EmbeddingsResource
     */
    public EmbeddingsResource embeddings() {
        return embeddings;
    }
    
    /**
     * Gets the audio resource for speech synthesis and transcription.
     * 
     * @return the audio resource instance
     * @see AudioResource
     */
    public AudioResource audio() {
        return audio;
    }
    
    /**
     * Gets the batches resource for batch processing operations.
     * 
     * @return the batches resource instance
     * @see BatchesResource
     */
    public BatchesResource batches() {
        return batches;
    }
    
    /**
     * Gets the files resource for file upload and management.
     * 
     * @return the files resource instance
     * @see FilesResource
     */
    public FilesResource files() {
        return files;
    }
    
    /**
     * Executes a GET request with comprehensive error handling.
     *
     * @param <T> the type of response data
     * @param path the API endpoint path
     * @param responseType the class type of the response
     * @param queryParams optional query parameters
     * @param headers optional HTTP headers
     * @return the API response wrapped in GroqResponse
     * @throws RuntimeException if the request fails
     */
    public <T> GroqResponse<T> get(String path, Class<T> responseType, 
                                 Map<String, Object> queryParams, 
                                 Map<String, String> headers) {
        return executeHttpMethod("GET", path, null, responseType, queryParams, headers);
    }
    
    /**
     * Executes a POST request with comprehensive error handling.
     *
     * @param <T> the type of response data
     * @param path the API endpoint path
     * @param body the request body object
     * @param responseType the class type of the response
     * @param queryParams optional query parameters
     * @param headers optional HTTP headers
     * @return the API response wrapped in GroqResponse
     * @throws RuntimeException if the request fails
     */
    public <T> GroqResponse<T> post(String path, Object body, Class<T> responseType,
                                  Map<String, Object> queryParams,
                                  Map<String, String> headers) {
        return executeHttpMethod("POST", path, body, responseType, queryParams, headers);
    }
    
    /**
     * Executes a PATCH request with comprehensive error handling.
     *
     * @param <T> the type of response data
     * @param path the API endpoint path
     * @param body the request body object
     * @param responseType the class type of the response
     * @param queryParams optional query parameters
     * @param headers optional HTTP headers
     * @return the API response wrapped in GroqResponse
     * @throws RuntimeException if the request fails
     */
    public <T> GroqResponse<T> patch(String path, Object body, Class<T> responseType,
                                   Map<String, Object> queryParams,
                                   Map<String, String> headers) {
        return executeHttpMethod("PATCH", path, body, responseType, queryParams, headers);
    }
    
    /**
     * Executes a PUT request with comprehensive error handling.
     *
     * @param <T> the type of response data
     * @param path the API endpoint path
     * @param body the request body object
     * @param responseType the class type of the response
     * @param queryParams optional query parameters
     * @param headers optional HTTP headers
     * @return the API response wrapped in GroqResponse
     * @throws RuntimeException if the request fails
     */
    public <T> GroqResponse<T> put(String path, Object body, Class<T> responseType,
                                 Map<String, Object> queryParams,
                                 Map<String, String> headers) {
        return executeHttpMethod("PUT", path, body, responseType, queryParams, headers);
    }
    
    /**
     * Executes a DELETE request with comprehensive error handling.
     *
     * @param <T> the type of response data
     * @param path the API endpoint path
     * @param responseType the class type of the response
     * @param queryParams optional query parameters
     * @param headers optional HTTP headers
     * @return the API response wrapped in GroqResponse
     * @throws RuntimeException if the request fails
     */
    public <T> GroqResponse<T> delete(String path, Class<T> responseType,
                                    Map<String, Object> queryParams,
                                    Map<String, String> headers) {
        return executeHttpMethod("DELETE", path, null, responseType, queryParams, headers);
    }
    
    /**
     * Executes a multipart form data request for file uploads.
     *
     * @param <T> the type of response data
     * @param path the API endpoint path
     * @param body the multipart form data body
     * @param responseType the class type of the response
     * @param headers optional HTTP headers
     * @return the API response wrapped in GroqResponse
     * @throws RuntimeException if the request fails
     */
    public <T> GroqResponse<T> executeMultipartRequest(String path, MultipartBody body, 
                                                     Class<T> responseType,
                                                     Map<String, String> headers) {
        try {
            Request.Builder requestBuilder = new Request.Builder()
                .url(buildUrl(path, Collections.emptyMap()))
                .post(body);
            
            defaultHeaders.forEach(requestBuilder::addHeader);
            if (headers != null) {
                headers.forEach(requestBuilder::addHeader);
            }
            
            requestBuilder.header("Authorization", "Bearer " + apiKey);
            
            return executeRequest(requestBuilder.build(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("Multipart request failed: " + e.getMessage(), e);
        }
    }
    
    private <T> GroqResponse<T> executeHttpMethod(String method, String path, Object body, 
                                                Class<T> responseType,
                                                Map<String, Object> queryParams,
                                                Map<String, String> headers) {
        try {
            Request.Builder requestBuilder = buildRequest(method, path, queryParams, headers, body);
            return executeRequest(requestBuilder.build(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("HTTP " + method + " request failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Builder for creating configured GroqClient instances with validation.
     * 
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * GroqClient client = GroqClient.builder()
     *     .apiKey("your-key")
     *     .timeout(Duration.ofSeconds(30))
     *     .maxRetries(3)
     *     .build();
     * }</pre>
     */
    public static class Builder extends BaseClient.Builder<GroqClient> {
        /**
         * Builds and returns a configured GroqClient instance.
         * 
         * @return a new GroqClient instance
         * @throws IllegalStateException if API key is not provided
         * @throws IllegalArgumentException if API key is empty
         */
        @Override
        public GroqClient build() {
            validateAndSetDefaults();
            return new GroqClient(this);
        }
        
        /**
         * Validates builder parameters and sets default values.
         * 
         * @throws IllegalStateException if required parameters are missing
         * @throws IllegalArgumentException if parameters are invalid
         */
        private void validateAndSetDefaults() {
            if (apiKey == null) {
                String envApiKey = System.getenv("GROQ_API_KEY");
                if (envApiKey == null) {
                    throw new IllegalStateException(
                        "API key must be provided either via builder or GROQ_API_KEY environment variable"
                    );
                }
                this.apiKey = envApiKey;
            }
            
            if (this.apiKey.trim().isEmpty()) {
                throw new IllegalArgumentException("API key cannot be empty");
            }
            
            String envBaseUrl = System.getenv("GROQ_BASE_URL");
            if (envBaseUrl != null) {
                this.baseUrl = envBaseUrl;
            }
            
            if (this.baseUrl == null || this.baseUrl.trim().isEmpty()) {
                throw new IllegalStateException("Base URL cannot be null or empty");
            }
            
            this.defaultHeaders.put("Content-Type", "application/json");
            this.defaultHeaders.put("User-Agent", "Groq-Java-SDK/1.0.0");
        }
    }
    
    /**
     * Creates a new builder instance for configuring GroqClient.
     * 
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
}
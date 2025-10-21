package com.groq.sdk.models;

import java.util.List;
import java.util.Map;

/**
 * Generic response wrapper for all Groq API responses. Provides consistent
 * access to data, headers, status codes, and success status across all API calls.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
 * if (response.isSuccessful()) {
 *     ChatCompletion completion = response.getData();
 *     // Process completion data
 * } else {
 *     int statusCode = response.getStatusCode();
 *     // Handle error
 * }
 * }</pre>
 * 
 * @param <T> the type of data contained in the response
 * @author Debajit Kumar Phukan
 * @since 21-Aug-2025
 * @version 1.0.0
 */
public class GroqResponse<T> {
    private final T data;
    private final Map<String, List<String>> headers;
    private final int statusCode;

    /**
     * Constructs a new GroqResponse with the specified data, headers, and status code.
     * 
     * @param data the response data
     * @param headers the HTTP response headers
     * @param statusCode the HTTP status code
     */
    public GroqResponse(T data, Map<String, List<String>> headers, int statusCode) {
        this.data = data;
        this.headers = headers != null ? Map.copyOf(headers) : Map.of();
        this.statusCode = statusCode;
    }

    /**
     * Gets the response data if the request was successful.
     * 
     * @return the response data, or null if no data
     */
    public T getData() {
        return data;
    }

    /**
     * Gets the HTTP response headers.
     * 
     * @return an unmodifiable map of header names to header values
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Gets the HTTP status code of the response.
     * 
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Checks if the request was successful (status code 2xx).
     * 
     * @return true if status code is between 200-299, false otherwise
     */
    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * Gets a specific header value from the response.
     * 
     * @param name the header name
     * @return the first value of the header, or null if not present
     */
    public String getHeader(String name) {
        List<String> values = headers.get(name);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    /**
     * Returns a string representation of the response.
     * 
     * @return a string containing response data, status code, and success status
     */
    @Override
    public String toString() {
        return "GroqResponse{" + "data=" + data + ", statusCode=" + statusCode + ", successful=" + isSuccessful() + '}';
    }
}
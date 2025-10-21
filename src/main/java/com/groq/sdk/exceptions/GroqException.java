package com.groq.sdk.exceptions;

/**
 * Base exception for all Groq API errors.
 * Provides detailed error information including status code and response body for proper error handling.
 * 
 * <p>This exception is thrown when:</p>
 * <ul>
 *   <li>API returns non-2xx status codes</li>
 *   <li>Network errors occur</li>
 *   <li>Request timeouts happen</li>
 *   <li>Authentication fails</li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 21-Oct-2025
 * @version 1.0.0
 */
public class GroqException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;
    
    /**
     * Constructs a new GroqException with the specified message, status code, and response body.
     * 
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param responseBody the raw response body from the API
     */
    public GroqException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    /**
     * Constructs a new GroqException with the specified message and cause.
     * 
     * @param message the error message
     * @param cause the underlying cause of the exception
     */
    public GroqException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.responseBody = null;
    }
    
    /**
     * Gets the HTTP status code from the API response.
     * 
     * @return the HTTP status code, or 0 if not available
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Gets the raw response body from the API.
     * 
     * @return the response body, or null if not available
     */
    public String getResponseBody() {
        return responseBody;
    }
    
    /**
     * Returns a string representation of the exception.
     * 
     * @return a string containing the message, status code, and response body
     */
    @Override
    public String toString() {
        return "GroqException{" +
                "message='" + getMessage() + '\'' +
                ", statusCode=" + statusCode +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
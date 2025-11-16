package com.groq.sdk.resources;

import java.util.Collections;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.responses.Response;
import com.groq.sdk.models.responses.ResponseRequest;

/**
 * Provides access to the Groq Response API operations.
 * Handles creating responses with reasoning, code interpreter, browser search, and MCP tools.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * ResponseResource responses = client.responses();
 * ResponseRequest request = new ResponseRequest("openai/gpt-oss-20b", "How are AI models trained?");
 * request.setReasoning(new ReasoningConfig("low"));
 * GroqResponse<Response> response = responses.create(request);
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see GroqClient
 * @see ResponseRequest
 * @see Response
 */
public class ResponseResource {
    private final GroqClient client;
    
    /**
     * Constructs a new ResponseResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public ResponseResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Creates a response with the specified request parameters.
     * 
     * @param request the response request containing model, input, and parameters
     * @return the API response containing response results
     * @throws IllegalArgumentException if request is null, model is empty, or input is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Response> create(ResponseRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("ResponseRequest cannot be null");
            }
            if (request.getModel() == null || request.getModel().trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be null or empty");
            }
            if (request.getInput() == null) {
                throw new IllegalArgumentException("Input cannot be null");
            }
            
            return client.post("/openai/v1/responses", request, Response.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a simple response with text input.
     * 
     * @param model the model to use for responses
     * @param input the input text
     * @return the API response containing response results
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Response> create(String model, String input) {
        try {
            ResponseRequest request = new ResponseRequest(model, input);
            return create(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create simple response: " + e.getMessage(), e);
        }
    }
}
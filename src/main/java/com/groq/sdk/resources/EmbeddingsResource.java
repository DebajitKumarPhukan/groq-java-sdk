package com.groq.sdk.resources;

import java.util.Collections;
import java.util.List;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.embeddings.EmbeddingRequest;
import com.groq.sdk.models.embeddings.EmbeddingResponse;

/**
 * Provides access to embedding operations in the Groq API.
 * Handles generating text embeddings for various use cases with comprehensive error handling.
 * 
 * <p>Embeddings can be used for:</p>
 * <ul>
 *   <li>Semantic search</li>
 *   <li>Text classification</li>
 *   <li>Clustering</li>
 *   <li>Recommendation systems</li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 02-Oct-2025
 * @version 1.0.0
 * @see GroqClient
 * @see EmbeddingRequest
 * @see EmbeddingResponse
 */
public class EmbeddingsResource {
    private final GroqClient client;
    
    /**
     * Constructs a new EmbeddingsResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public EmbeddingsResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Creates embeddings for the given input text(s).
     * 
     * @param request the embedding request containing model and input texts
     * @return the API response containing generated embeddings
     * @throws IllegalArgumentException if request is null, model is empty, or input is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<EmbeddingResponse> create(EmbeddingRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("EmbeddingRequest cannot be null");
            }
            if (request.getModel() == null || request.getModel().trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be null or empty");
            }
            if (request.getInput() == null || request.getInput().isEmpty()) {
                throw new IllegalArgumentException("Input cannot be null or empty");
            }
            
            return client.post("/openai/v1/embeddings", request, EmbeddingResponse.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create embeddings: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates embeddings for a single text input.
     * 
     * @param model the model to use for embedding generation
     * @param input the input text to embed
     * @return the API response containing generated embeddings
     * @throws IllegalArgumentException if input text is null or empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<EmbeddingResponse> create(String model, String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Input text cannot be null or empty");
            }
            
            EmbeddingRequest request = new EmbeddingRequest(model, Collections.singletonList(input));
            return create(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create embedding for single input: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates embeddings for multiple text inputs.
     * 
     * @param model the model to use for embedding generation
     * @param inputs the list of input texts to embed
     * @return the API response containing generated embeddings
     * @throws IllegalArgumentException if input list is null, empty, or contains null/empty values
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<EmbeddingResponse> create(String model, List<String> inputs) {
        try {
            if (inputs == null || inputs.isEmpty()) {
                throw new IllegalArgumentException("Input list cannot be null or empty");
            }
            
            for (String input : inputs) {
                if (input == null || input.trim().isEmpty()) {
                    throw new IllegalArgumentException("Input text cannot be null or empty");
                }
            }
            
            EmbeddingRequest request = new EmbeddingRequest(model, inputs);
            return create(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create embeddings for multiple inputs: " + e.getMessage(), e);
        }
    }
}
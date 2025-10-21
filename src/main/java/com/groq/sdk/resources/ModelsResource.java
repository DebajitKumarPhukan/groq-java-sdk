package com.groq.sdk.resources;

import java.util.Collections;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.models.ModelList;

/**
 * Provides access to model operations in the Groq API.
 * Handles listing available models and retrieving model information with comprehensive error handling.
 * 
 * @author Debajit Kumar Phukan
 * @since 05-Oct-2025
 * @version 1.0.0
 * @see GroqClient
 * @see ModelList
 * @see com.groq.sdk.models.models.Model
 */
public class ModelsResource {
    private final GroqClient client;
    
    /**
     * Constructs a new ModelsResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public ModelsResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Lists all available models.
     * 
     * @return the API response containing the list of models
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<ModelList> list() {
        try {
            return client.get("/openai/v1/models", ModelList.class, 
                             Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list models: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a specific model by ID.
     * 
     * @param modelId the unique identifier of the model to retrieve
     * @return the API response containing the model details
     * @throws IllegalArgumentException if model ID is null or empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<com.groq.sdk.models.models.Model> retrieve(String modelId) {
        try {
            if (modelId == null || modelId.trim().isEmpty()) {
                throw new IllegalArgumentException("Model ID cannot be null or empty");
            }
            
            return client.get("/openai/v1/models/" + modelId, com.groq.sdk.models.models.Model.class, 
                             Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve model: " + e.getMessage(), e);
        }
    }
}
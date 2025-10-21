package com.groq.sdk.resources;

import java.util.Collections;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.batches.Batch;
import com.groq.sdk.models.batches.BatchCreateRequest;
import com.groq.sdk.models.batches.BatchList;

/**
 * Provides access to batch processing operations in the Groq API.
 * Handles creating, retrieving, listing, and canceling batch jobs with comprehensive error handling.
 * 
 * @author Debajit Kumar Phukan
 * @since 04-Oct-2025
 * @version 1.0.0
 * @see GroqClient
 * @see Batch
 * @see BatchCreateRequest
 * @see BatchList
 */
public class BatchesResource {
    private final GroqClient client;
    
    /**
     * Constructs a new BatchesResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public BatchesResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Creates a new batch processing job.
     * 
     * @param request the batch creation request containing input file and configuration
     * @return the API response containing the created batch job
     * @throws IllegalArgumentException if request is null, input file ID is empty, or endpoint is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Batch> create(BatchCreateRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("BatchCreateRequest cannot be null");
            }
            if (request.getInputFileId() == null || request.getInputFileId().trim().isEmpty()) {
                throw new IllegalArgumentException("Input file ID cannot be null or empty");
            }
            if (request.getEndpoint() == null || request.getEndpoint().trim().isEmpty()) {
                throw new IllegalArgumentException("Endpoint cannot be null or empty");
            }
            
            return client.post("/openai/v1/batches", request, Batch.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create batch: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a specific batch by ID.
     * 
     * @param batchId the unique identifier of the batch to retrieve
     * @return the API response containing the batch details
     * @throws IllegalArgumentException if batch ID is null or empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Batch> retrieve(String batchId) {
        try {
            if (batchId == null || batchId.trim().isEmpty()) {
                throw new IllegalArgumentException("Batch ID cannot be null or empty");
            }
            
            return client.get("/openai/v1/batches/" + batchId, Batch.class, 
                             Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve batch: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lists all available batches.
     * 
     * @return the API response containing the list of batches
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<BatchList> list() {
        try {
            return client.get("/openai/v1/batches", BatchList.class, 
                             Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list batches: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cancels a specific batch by ID.
     * 
     * @param batchId the unique identifier of the batch to cancel
     * @return the API response containing the cancelled batch details
     * @throws IllegalArgumentException if batch ID is null or empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Batch> cancel(String batchId) {
        try {
            if (batchId == null || batchId.trim().isEmpty()) {
                throw new IllegalArgumentException("Batch ID cannot be null or empty");
            }
            
            return client.post("/openai/v1/batches/" + batchId + "/cancel", null, Batch.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel batch: " + e.getMessage(), e);
        }
    }
}
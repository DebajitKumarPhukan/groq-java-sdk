package com.groq.sdk.models.batches;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Represents a request to create a batch processing job.
 * Contains the input file reference, processing endpoint, and batch configuration.
 * 
 * @author Debajit Kumar Phukan
 * @since 08-Sep-2025
 * @version 1.0.0
 * @see Batch
 */
public class BatchCreateRequest {
    @JsonProperty("input_file_id")
    private String inputFileId;
    
    private String endpoint;
    
    @JsonProperty("completion_window") 
    private String completionWindow;
    
    private Map<String, Object> metadata;
    
    /**
     * Gets the input file ID containing the batch requests.
     * 
     * @return the input file ID
     */
    public String getInputFileId() { 
        return inputFileId; 
    }
    
    /**
     * Sets the input file ID containing the batch requests.
     * 
     * @param inputFileId the input file ID
     */
    public void setInputFileId(String inputFileId) { 
        this.inputFileId = inputFileId; 
    }
    
    /**
     * Gets the API endpoint to process in the batch.
     * 
     * @return the endpoint URL
     */
    public String getEndpoint() { 
        return endpoint; 
    }
    
    /**
     * Sets the API endpoint to process in the batch.
     * 
     * @param endpoint the endpoint URL
     */
    public void setEndpoint(String endpoint) { 
        this.endpoint = endpoint; 
    }
    
    /**
     * Gets the completion window for batch processing.
     * 
     * @return the completion window (e.g., "24h")
     */
    public String getCompletionWindow() { 
        return completionWindow; 
    }
    
    /**
     * Sets the completion window for batch processing.
     * 
     * @param completionWindow the completion window
     */
    public void setCompletionWindow(String completionWindow) { 
        this.completionWindow = completionWindow; 
    }
    
    /**
     * Gets the metadata associated with the batch job.
     * 
     * @return the metadata map, or null if no metadata
     */
    public Map<String, Object> getMetadata() { 
        return metadata; 
    }
    
    /**
     * Sets the metadata associated with the batch job.
     * 
     * @param metadata the metadata map
     */
    public void setMetadata(Map<String, Object> metadata) { 
        this.metadata = metadata; 
    }
}
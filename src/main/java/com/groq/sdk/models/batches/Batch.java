package com.groq.sdk.models.batches;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Represents a batch processing job with its current status and metadata.
 * Tracks the progress and results of batch operations.
 * 
 * <p>Batch jobs are used for processing large volumes of requests asynchronously.
 * They can handle thousands of API requests in a single batch operation.</p>
 * 
 * @author Debajit Kumar Phukan
 * @since 08-Sep-2025
 * @version 1.0.0
 * @see BatchList
 * @see BatchCreateRequest
 * @see BatchRequestCounts
 */
public class Batch {
    private String id;
    private String object;
    private String endpoint;
    
    @JsonProperty("input_file_id") 
    private String inputFileId;
    
    @JsonProperty("completion_window") 
    private String completionWindow;
    
    private String status;
    
    @JsonProperty("output_file_id") 
    private String outputFileId;
    
    @JsonProperty("error_file_id") 
    private String errorFileId;
    
    @JsonProperty("created_at") 
    private Long createdAt;
    
    @JsonProperty("in_progress_at") 
    private Long inProgressAt;
    
    @JsonProperty("expires_at") 
    private Long expiresAt;
    
    @JsonProperty("finalizing_at") 
    private Long finalizingAt;
    
    @JsonProperty("completed_at") 
    private Long completedAt;
    
    @JsonProperty("failed_at") 
    private Long failedAt;
    
    @JsonProperty("expired_at") 
    private Long expiredAt;
    
    @JsonProperty("cancelling_at") 
    private Long cancellingAt;
    
    @JsonProperty("cancelled_at") 
    private Long cancelledAt;
    
    private Map<String, Object> metadata;
    private BatchRequestCounts requestCounts;
    
    /**
     * Gets the unique identifier for the batch job.
     * 
     * @return the batch ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique identifier for the batch job.
     * 
     * @param id the batch ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * Gets the object type (usually "batch").
     * 
     * @return the object type
     */
    public String getObject() { 
        return object; 
    }
    
    /**
     * Sets the object type.
     * 
     * @param object the object type
     */
    public void setObject(String object) { 
        this.object = object; 
    }
    
    /**
     * Gets the API endpoint being processed in this batch.
     * 
     * @return the endpoint URL
     */
    public String getEndpoint() { 
        return endpoint; 
    }
    
    /**
     * Sets the API endpoint being processed in this batch.
     * 
     * @param endpoint the endpoint URL
     */
    public void setEndpoint(String endpoint) { 
        this.endpoint = endpoint; 
    }
    
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
     * Gets the completion window for the batch processing.
     * 
     * @return the completion window (e.g., "24h")
     */
    public String getCompletionWindow() { 
        return completionWindow; 
    }
    
    /**
     * Sets the completion window for the batch processing.
     * 
     * @param completionWindow the completion window
     */
    public void setCompletionWindow(String completionWindow) { 
        this.completionWindow = completionWindow; 
    }
    
    /**
     * Gets the current status of the batch job.
     * 
     * @return the status (e.g., "validating", "in_progress", "completed", "failed")
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * Sets the current status of the batch job.
     * 
     * @param status the status
     */
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    /**
     * Gets the output file ID containing batch results.
     * 
     * @return the output file ID, or null if not available
     */
    public String getOutputFileId() { 
        return outputFileId; 
    }
    
    /**
     * Sets the output file ID containing batch results.
     * 
     * @param outputFileId the output file ID
     */
    public void setOutputFileId(String outputFileId) { 
        this.outputFileId = outputFileId; 
    }
    
    /**
     * Gets the error file ID containing batch errors.
     * 
     * @return the error file ID, or null if no errors
     */
    public String getErrorFileId() { 
        return errorFileId; 
    }
    
    /**
     * Sets the error file ID containing batch errors.
     * 
     * @param errorFileId the error file ID
     */
    public void setErrorFileId(String errorFileId) { 
        this.errorFileId = errorFileId; 
    }
    
    /**
     * Gets the timestamp when the batch was created.
     * 
     * @return the creation timestamp in seconds since epoch
     */
    public Long getCreatedAt() { 
        return createdAt; 
    }
    
    /**
     * Sets the timestamp when the batch was created.
     * 
     * @param createdAt the creation timestamp in seconds since epoch
     */
    public void setCreatedAt(Long createdAt) { 
        this.createdAt = createdAt; 
    }
    
    /**
     * Gets the timestamp when the batch started processing.
     * 
     * @return the in-progress timestamp in seconds since epoch, or null if not started
     */
    public Long getInProgressAt() { 
        return inProgressAt; 
    }
    
    /**
     * Sets the timestamp when the batch started processing.
     * 
     * @param inProgressAt the in-progress timestamp in seconds since epoch
     */
    public void setInProgressAt(Long inProgressAt) { 
        this.inProgressAt = inProgressAt; 
    }
    
    /**
     * Gets the timestamp when the batch expires.
     * 
     * @return the expiration timestamp in seconds since epoch
     */
    public Long getExpiresAt() { 
        return expiresAt; 
    }
    
    /**
     * Sets the timestamp when the batch expires.
     * 
     * @param expiresAt the expiration timestamp in seconds since epoch
     */
    public void setExpiresAt(Long expiresAt) { 
        this.expiresAt = expiresAt; 
    }
    
    /**
     * Gets the timestamp when the batch started finalizing.
     * 
     * @return the finalizing timestamp in seconds since epoch, or null if not finalizing
     */
    public Long getFinalizingAt() { 
        return finalizingAt; 
    }
    
    /**
     * Sets the timestamp when the batch started finalizing.
     * 
     * @param finalizingAt the finalizing timestamp in seconds since epoch
     */
    public void setFinalizingAt(Long finalizingAt) { 
        this.finalizingAt = finalizingAt; 
    }
    
    /**
     * Gets the timestamp when the batch completed.
     * 
     * @return the completion timestamp in seconds since epoch, or null if not completed
     */
    public Long getCompletedAt() { 
        return completedAt; 
    }
    
    /**
     * Sets the timestamp when the batch completed.
     * 
     * @param completedAt the completion timestamp in seconds since epoch
     */
    public void setCompletedAt(Long completedAt) { 
        this.completedAt = completedAt; 
    }
    
    /**
     * Gets the timestamp when the batch failed.
     * 
     * @return the failure timestamp in seconds since epoch, or null if not failed
     */
    public Long getFailedAt() { 
        return failedAt; 
    }
    
    /**
     * Sets the timestamp when the batch failed.
     * 
     * @param failedAt the failure timestamp in seconds since epoch
     */
    public void setFailedAt(Long failedAt) { 
        this.failedAt = failedAt; 
    }
    
    /**
     * Gets the timestamp when the batch expired.
     * 
     * @return the expiration timestamp in seconds since epoch, or null if not expired
     */
    public Long getExpiredAt() { 
        return expiredAt; 
    }
    
    /**
     * Sets the timestamp when the batch expired.
     * 
     * @param expiredAt the expiration timestamp in seconds since epoch
     */
    public void setExpiredAt(Long expiredAt) { 
        this.expiredAt = expiredAt; 
    }
    
    /**
     * Gets the timestamp when the batch cancellation started.
     * 
     * @return the cancellation start timestamp in seconds since epoch, or null if not cancelling
     */
    public Long getCancellingAt() { 
        return cancellingAt; 
    }
    
    /**
     * Sets the timestamp when the batch cancellation started.
     * 
     * @param cancellingAt the cancellation start timestamp in seconds since epoch
     */
    public void setCancellingAt(Long cancellingAt) { 
        this.cancellingAt = cancellingAt; 
    }
    
    /**
     * Gets the timestamp when the batch was cancelled.
     * 
     * @return the cancellation timestamp in seconds since epoch, or null if not cancelled
     */
    public Long getCancelledAt() { 
        return cancelledAt; 
    }
    
    /**
     * Sets the timestamp when the batch was cancelled.
     * 
     * @param cancelledAt the cancellation timestamp in seconds since epoch
     */
    public void setCancelledAt(Long cancelledAt) { 
        this.cancelledAt = cancelledAt; 
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
    
    /**
     * Gets the request counts for the batch job.
     * 
     * @return the request counts
     * @see BatchRequestCounts
     */
    public BatchRequestCounts getRequestCounts() { 
        return requestCounts; 
    }
    
    /**
     * Sets the request counts for the batch job.
     * 
     * @param requestCounts the request counts
     * @see BatchRequestCounts
     */
    public void setRequestCounts(BatchRequestCounts requestCounts) { 
        this.requestCounts = requestCounts; 
    }
}
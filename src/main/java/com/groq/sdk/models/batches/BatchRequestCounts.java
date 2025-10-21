package com.groq.sdk.models.batches;

/**
 * Represents the count of requests in a batch job.
 * Tracks total, completed, and failed request counts.
 * 
 * @author Debajit Kumar Phukan
 * @since 09-Sep-2025
 * @version 1.0.0
 * @see Batch
 */
public class BatchRequestCounts {
    private Integer total;
    private Integer completed;
    private Integer failed;
    
    /**
     * Gets the total number of requests in the batch.
     * 
     * @return the total request count
     */
    public Integer getTotal() { 
        return total; 
    }
    
    /**
     * Sets the total number of requests in the batch.
     * 
     * @param total the total request count
     */
    public void setTotal(Integer total) { 
        this.total = total; 
    }
    
    /**
     * Gets the number of completed requests in the batch.
     * 
     * @return the completed request count
     */
    public Integer getCompleted() { 
        return completed; 
    }
    
    /**
     * Sets the number of completed requests in the batch.
     * 
     * @param completed the completed request count
     */
    public void setCompleted(Integer completed) { 
        this.completed = completed; 
    }
    
    /**
     * Gets the number of failed requests in the batch.
     * 
     * @return the failed request count
     */
    public Integer getFailed() { 
        return failed; 
    }
    
    /**
     * Sets the number of failed requests in the batch.
     * 
     * @param failed the failed request count
     */
    public void setFailed(Integer failed) { 
        this.failed = failed; 
    }
}
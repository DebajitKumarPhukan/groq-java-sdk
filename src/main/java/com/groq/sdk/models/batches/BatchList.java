package com.groq.sdk.models.batches;

import java.util.List;

/**
 * Represents a list of batch jobs with pagination support.
 * Contains the batch objects and list metadata.
 * 
 * @author Debajit Kumar Phukan
 * @since 10-Sep-2025
 * @version 1.0.0
 * @see Batch
 */
public class BatchList {
    private String object;
    private List<Batch> data;
    
    /**
     * Gets the object type (usually "list").
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
     * Gets the list of batch objects.
     * 
     * @return the list of batches
     * @see Batch
     */
    public List<Batch> getData() { 
        return data; 
    }
    
    /**
     * Sets the list of batch objects.
     * 
     * @param data the list of batches
     * @see Batch
     */
    public void setData(List<Batch> data) { 
        this.data = data; 
    }
}
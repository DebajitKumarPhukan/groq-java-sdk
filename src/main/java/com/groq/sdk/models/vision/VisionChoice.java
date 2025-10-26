package com.groq.sdk.models.vision;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single choice in a vision response.
 * Contains the generated message and finish reason.
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Oct-2025
 * @version 1.0.0
 * @see VisionResponse
 */
public class VisionChoice {
    private Integer index;
    private VisionMessage message;
    
    @JsonProperty("finish_reason")
    private String finishReason;
    
    /**
     * Gets the index of the choice.
     * 
     * @return the choice index
     */
    public Integer getIndex() { 
        return index; 
    }
    
    /**
     * Sets the index of the choice.
     * 
     * @param index the choice index
     */
    public void setIndex(Integer index) { 
        this.index = index; 
    }
    
    /**
     * Gets the generated vision message.
     * 
     * @return the vision message
     * @see VisionMessage
     */
    public VisionMessage getMessage() { 
        return message; 
    }
    
    /**
     * Sets the generated vision message.
     * 
     * @param message the vision message
     * @see VisionMessage
     */
    public void setMessage(VisionMessage message) { 
        this.message = message; 
    }
    
    /**
     * Gets the finish reason.
     * 
     * @return the finish reason
     */
    public String getFinishReason() { 
        return finishReason; 
    }
    
    /**
     * Sets the finish reason.
     * 
     * @param finishReason the finish reason
     */
    public void setFinishReason(String finishReason) { 
        this.finishReason = finishReason; 
    }
}
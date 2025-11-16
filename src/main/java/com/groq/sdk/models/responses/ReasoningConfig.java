package com.groq.sdk.models.responses;

/**
 * Configuration for reasoning in Response API requests.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ResponseRequest
 */
public class ReasoningConfig {
    private String effort; // "low", "medium", "high"
    
    /**
     * Default constructor.
     */
    public ReasoningConfig() {}
    
    /**
     * Constructs a new ReasoningConfig with the specified effort.
     * 
     * @param effort the reasoning effort level
     */
    public ReasoningConfig(String effort) {
        this.effort = effort;
    }
    
    /**
     * Gets the reasoning effort level.
     * 
     * @return the effort level
     */
    public String getEffort() { 
        return effort; 
    }
    
    /**
     * Sets the reasoning effort level.
     * 
     * @param effort the effort level
     */
    public void setEffort(String effort) { 
        this.effort = effort; 
    }
}
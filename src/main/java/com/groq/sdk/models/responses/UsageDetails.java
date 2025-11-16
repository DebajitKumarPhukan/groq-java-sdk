package com.groq.sdk.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents detailed token usage statistics.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see Usage
 */
public class UsageDetails {
    @JsonProperty("cached_tokens")
    private Integer cachedTokens;
    
    @JsonProperty("reasoning_tokens")
    private Integer reasoningTokens;
    
    /**
     * Gets the number of cached tokens.
     * 
     * @return the cached token count
     */
    public Integer getCachedTokens() { 
        return cachedTokens; 
    }
    
    /**
     * Sets the number of cached tokens.
     * 
     * @param cachedTokens the cached token count
     */
    public void setCachedTokens(Integer cachedTokens) { 
        this.cachedTokens = cachedTokens; 
    }
    
    /**
     * Gets the number of reasoning tokens.
     * 
     * @return the reasoning token count
     */
    public Integer getReasoningTokens() { 
        return reasoningTokens; 
    }
    
    /**
     * Sets the number of reasoning tokens.
     * 
     * @param reasoningTokens the reasoning token count
     */
    public void setReasoningTokens(Integer reasoningTokens) { 
        this.reasoningTokens = reasoningTokens; 
    }
}
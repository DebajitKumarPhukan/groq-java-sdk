package com.groq.sdk.models.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents token usage statistics for an API call.
 * Tracks prompt, completion, and total tokens used in the request.
 * 
 * @author Debajit Kumar Phukan
 * @since 31-Aug-2025
 * @version 1.0.0
 * @see ChatCompletion
 */
public class Usage {
	public Usage() {
	}
	
    @JsonProperty("prompt_tokens")
    private Integer promptTokens;
    
    @JsonProperty("completion_tokens")
    private Integer completionTokens;
    
    @JsonProperty("total_tokens")
    private Integer totalTokens;
    
    /**
     * Gets the number of tokens used in the prompt.
     * 
     * @return the prompt token count
     */
    public Integer getPromptTokens() { 
        return promptTokens; 
    }
    
    /**
     * Sets the number of tokens used in the prompt.
     * 
     * @param promptTokens the prompt token count
     */
    public void setPromptTokens(Integer promptTokens) { 
        this.promptTokens = promptTokens; 
    }
    
    /**
     * Gets the number of tokens generated in the completion.
     * 
     * @return the completion token count
     */
    public Integer getCompletionTokens() { 
        return completionTokens; 
    }
    
    /**
     * Sets the number of tokens generated in the completion.
     * 
     * @param completionTokens the completion token count
     */
    public void setCompletionTokens(Integer completionTokens) { 
        this.completionTokens = completionTokens; 
    }
    
    /**
     * Gets the total number of tokens used (prompt + completion).
     * 
     * @return the total token count
     */
    public Integer getTotalTokens() { 
        return totalTokens; 
    }
    
    /**
     * Sets the total number of tokens used (prompt + completion).
     * 
     * @param totalTokens the total token count
     */
    public void setTotalTokens(Integer totalTokens) { 
        this.totalTokens = totalTokens; 
    }
}
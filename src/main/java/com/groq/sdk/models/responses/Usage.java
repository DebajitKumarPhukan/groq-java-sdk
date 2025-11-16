package com.groq.sdk.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents comprehensive token usage statistics for Response API.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see Response
 */
public class Usage {
    @JsonProperty("input_tokens")
    private Integer inputTokens;
    
    @JsonProperty("input_tokens_details")
    private UsageDetails inputTokensDetails;
    
    @JsonProperty("output_tokens")
    private Integer outputTokens;
    
    @JsonProperty("output_tokens_details")
    private UsageDetails outputTokensDetails;
    
    @JsonProperty("total_tokens")
    private Integer totalTokens;
    
    /**
     * Gets the number of input tokens.
     * 
     * @return the input token count
     */
    public Integer getInputTokens() { 
        return inputTokens; 
    }
    
    /**
     * Sets the number of input tokens.
     * 
     * @param inputTokens the input token count
     */
    public void setInputTokens(Integer inputTokens) { 
        this.inputTokens = inputTokens; 
    }
    
    /**
     * Gets the input token details.
     * 
     * @return the input token details
     */
    public UsageDetails getInputTokensDetails() { 
        return inputTokensDetails; 
    }
    
    /**
     * Sets the input token details.
     * 
     * @param inputTokensDetails the input token details
     */
    public void setInputTokensDetails(UsageDetails inputTokensDetails) { 
        this.inputTokensDetails = inputTokensDetails; 
    }
    
    /**
     * Gets the number of output tokens.
     * 
     * @return the output token count
     */
    public Integer getOutputTokens() { 
        return outputTokens; 
    }
    
    /**
     * Sets the number of output tokens.
     * 
     * @param outputTokens the output token count
     */
    public void setOutputTokens(Integer outputTokens) { 
        this.outputTokens = outputTokens; 
    }
    
    /**
     * Gets the output token details.
     * 
     * @return the output token details
     */
    public UsageDetails getOutputTokensDetails() { 
        return outputTokensDetails; 
    }
    
    /**
     * Sets the output token details.
     * 
     * @param outputTokensDetails the output token details
     */
    public void setOutputTokensDetails(UsageDetails outputTokensDetails) { 
        this.outputTokensDetails = outputTokensDetails; 
    }
    
    /**
     * Gets the total number of tokens.
     * 
     * @return the total token count
     */
    public Integer getTotalTokens() { 
        return totalTokens; 
    }
    
    /**
     * Sets the total number of tokens.
     * 
     * @param totalTokens the total token count
     */
    public void setTotalTokens(Integer totalTokens) { 
        this.totalTokens = totalTokens; 
    }
}
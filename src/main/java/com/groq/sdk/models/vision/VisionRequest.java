package com.groq.sdk.models.vision;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request for vision processing with image and text inputs.
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Oct-2025
 * @version 1.0.0
 */
public class VisionRequest {
    private String model;
    private List<VisionMessage> messages;
    
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    
    private Double temperature;
    
    @JsonProperty("top_p")
    private Double topP;
    
    private Boolean stream;
    
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;
    
    @JsonProperty("presence_penalty")
    private Double presencePenalty;
    
    /**
     * Default constructor.
     */
    public VisionRequest() {}
    
    /**
     * Constructs a new VisionRequest with the specified model and messages.
     * 
     * @param model the vision model to use
     * @param messages the list of vision messages
     */
    public VisionRequest(String model, List<VisionMessage> messages) {
        this.model = model;
        this.messages = messages;
    }
    
    /**
     * Gets the vision model to use for processing.
     * 
     * @return the model identifier
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the vision model to use for processing.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the list of vision messages for the conversation.
     * 
     * @return the list of vision messages
     */
    public List<VisionMessage> getMessages() { 
        return messages; 
    }
    
    /**
     * Sets the list of vision messages for the conversation.
     * 
     * @param messages the list of vision messages
     */
    public void setMessages(List<VisionMessage> messages) { 
        this.messages = messages; 
    }
    
    /**
     * Gets the maximum number of tokens to generate.
     * 
     * @return the maximum token count, or null if not set
     */
    public Integer getMaxTokens() { 
        return maxTokens; 
    }
    
    /**
     * Sets the maximum number of tokens to generate.
     * 
     * @param maxTokens the maximum token count
     */
    public void setMaxTokens(Integer maxTokens) { 
        this.maxTokens = maxTokens; 
    }
    
    /**
     * Gets the sampling temperature.
     * 
     * @return the temperature value, or null if not set
     */
    public Double getTemperature() { 
        return temperature; 
    }
    
    /**
     * Sets the sampling temperature.
     * 
     * @param temperature the temperature value
     */
    public void setTemperature(Double temperature) { 
        this.temperature = temperature; 
    }
    
    /**
     * Gets the top-p sampling parameter.
     * 
     * @return the top-p value, or null if not set
     */
    public Double getTopP() { 
        return topP; 
    }
    
    /**
     * Sets the top-p sampling parameter.
     * 
     * @param topP the top-p value
     */
    public void setTopP(Double topP) { 
        this.topP = topP; 
    }
    
    /**
     * Gets whether to stream the response.
     * 
     * @return true if streaming is enabled, false otherwise, or null if not set
     */
    public Boolean getStream() { 
        return stream; 
    }
    
    /**
     * Sets whether to stream the response.
     * 
     * @param stream true to enable streaming, false to disable
     */
    public void setStream(Boolean stream) { 
        this.stream = stream; 
    }
    
    /**
     * Gets the frequency penalty parameter.
     * 
     * @return the frequency penalty value, or null if not set
     */
    public Double getFrequencyPenalty() { 
        return frequencyPenalty; 
    }
    
    /**
     * Sets the frequency penalty parameter.
     * 
     * @param frequencyPenalty the frequency penalty value
     */
    public void setFrequencyPenalty(Double frequencyPenalty) { 
        this.frequencyPenalty = frequencyPenalty; 
    }
    
    /**
     * Gets the presence penalty parameter.
     * 
     * @return the presence penalty value, or null if not set
     */
    public Double getPresencePenalty() { 
        return presencePenalty; 
    }
    
    /**
     * Sets the presence penalty parameter.
     * 
     * @param presencePenalty the presence penalty value
     */
    public void setPresencePenalty(Double presencePenalty) { 
        this.presencePenalty = presencePenalty; 
    }
}
package com.groq.sdk.models.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single choice in a chat completion response.
 * Contains the generated message and finish reason for the choice.
 * 
 * @author Debajit Kumar Phukan
 * @since 03-Sep-2025
 * @version 1.0.0
 * @see ChatCompletion
 * @see ChatMessage
 */
public class ChatChoice {
    private Integer index;
    private ChatMessage message;
    
    @JsonProperty("finish_reason")
    private String finishReason;
    
    /**
     * Gets the finish reason for the completion.
     * 
     * @return the finish reason (e.g., "stop", "length", "tool_calls", "content_filter")
     */
    public String getFinishReason() { 
        return finishReason; 
    }
    
    /**
     * Sets the finish reason for the completion.
     * 
     * @param finishReason the finish reason
     */
    public void setFinishReason(String finishReason) { 
        this.finishReason = finishReason; 
    }
    
    /**
     * Gets the index of the choice in the list of choices.
     * 
     * @return the choice index
     */
    public Integer getIndex() { 
        return index; 
    }
    
    /**
     * Sets the index of the choice in the list of choices.
     * 
     * @param index the choice index
     */
    public void setIndex(Integer index) { 
        this.index = index; 
    }
    
    /**
     * Gets the generated message for this choice.
     * 
     * @return the chat message
     * @see ChatMessage
     */
    public ChatMessage getMessage() { 
        return message; 
    }
    
    /**
     * Sets the generated message for this choice.
     * 
     * @param message the chat message
     * @see ChatMessage
     */
    public void setMessage(ChatMessage message) { 
        this.message = message; 
    }
}
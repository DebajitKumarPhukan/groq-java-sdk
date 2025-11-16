package com.groq.sdk.models.responses;

import java.util.List;

/**
 * Represents message output in Response API responses.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ResponseOutput
 */
public class MessageOutput extends ResponseOutput {
    private String role;
    private List<MessageContent> content;
    
    /**
     * Default constructor.
     */
    public MessageOutput() {
        setType("message");
    }
    
    /**
     * Gets the message role.
     * 
     * @return the message role
     */
    public String getRole() { 
        return role; 
    }
    
    /**
     * Sets the message role.
     * 
     * @param role the message role
     */
    public void setRole(String role) { 
        this.role = role; 
    }
    
    /**
     * Gets the message content.
     * 
     * @return the message content
     */
    public List<MessageContent> getContent() { 
        return content; 
    }
    
    /**
     * Sets the message content.
     * 
     * @param content the message content
     */
    public void setContent(List<MessageContent> content) { 
        this.content = content; 
    }
}
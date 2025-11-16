package com.groq.sdk.models.responses;

/**
 * Represents a message input for the Response API.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ResponseRequest
 */
public class MessageInput {
    private String type = "message";
    private String role;
    private String content;
    
    /**
     * Default constructor.
     */
    public MessageInput() {}
    
    /**
     * Constructs a new MessageInput with the specified role and content.
     * 
     * @param role the message role
     * @param content the message content
     */
    public MessageInput(String role, String content) {
        this.role = role;
        this.content = content;
    }
    
    /**
     * Gets the input type.
     * 
     * @return the input type
     */
    public String getType() { 
        return type; 
    }
    
    /**
     * Sets the input type.
     * 
     * @param type the input type
     */
    public void setType(String type) { 
        this.type = type; 
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
    public String getContent() { 
        return content; 
    }
    
    /**
     * Sets the message content.
     * 
     * @param content the message content
     */
    public void setContent(String content) { 
        this.content = content; 
    }
}
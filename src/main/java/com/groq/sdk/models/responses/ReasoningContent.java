package com.groq.sdk.models.responses;

/**
 * Represents reasoning content in Response API responses.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ReasoningOutput
 */
public class ReasoningContent {
    private String type;
    private String text;
    
    /**
     * Default constructor.
     */
    public ReasoningContent() {}
    
    /**
     * Gets the content type.
     * 
     * @return the content type
     */
    public String getType() { 
        return type; 
    }
    
    /**
     * Sets the content type.
     * 
     * @param type the content type
     */
    public void setType(String type) { 
        this.type = type; 
    }
    
    /**
     * Gets the reasoning text.
     * 
     * @return the reasoning text
     */
    public String getText() { 
        return text; 
    }
    
    /**
     * Sets the reasoning text.
     * 
     * @param text the reasoning text
     */
    public void setText(String text) { 
        this.text = text; 
    }
}
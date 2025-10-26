package com.groq.sdk.models.audio;

/**
 * Represents Groq metadata in API responses.
 * Contains request identification information.
 * 
 * @author Debajit Kumar Phukan
 * @since 26-Oct-2025
 * @version 1.0.0
 */
public class GroqMetadata {
    private String id;
    
    /**
     * Default constructor.
     */
    public GroqMetadata() {}
    
    /**
     * Constructs a new GroqMetadata with the specified ID.
     * 
     * @param id the request ID
     */
    public GroqMetadata(String id) {
        this.id = id;
    }
    
    /**
     * Gets the request ID.
     * 
     * @return the request ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the request ID.
     * 
     * @param id the request ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
}
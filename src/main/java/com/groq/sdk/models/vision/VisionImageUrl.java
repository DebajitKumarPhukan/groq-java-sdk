package com.groq.sdk.models.vision;

/**
 * Represents an image URL for vision processing.
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Oct-2025
 * @version 1.0.0
 */
public class VisionImageUrl {
    private String url;
    
    /**
     * Default constructor.
     */
    public VisionImageUrl() {}
    
    /**
     * Constructs a new VisionImageUrl with the specified URL.
     * 
     * @param url the image URL
     */
    public VisionImageUrl(String url) {
        this.url = url;
    }
    
    /**
     * Gets the image URL.
     * 
     * @return the image URL
     */
    public String getUrl() { 
        return url; 
    }
    
    /**
     * Sets the image URL.
     * 
     * @param url the image URL
     */
    public void setUrl(String url) { 
        this.url = url; 
    }
}
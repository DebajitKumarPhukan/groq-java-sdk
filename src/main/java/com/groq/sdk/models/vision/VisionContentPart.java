package com.groq.sdk.models.vision;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a content part in a vision message, which can be either text or image.
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Oct-2025
 * @version 1.0.0
 */
public class VisionContentPart {
    private String type;
    private String text;
    
    @JsonProperty("image_url")
    private VisionImageUrl imageUrl;
    
    /**
     * Default constructor.
     */
    public VisionContentPart() {}
    
    /**
     * Creates a text content part.
     * 
     * @param text the text content
     * @return a new VisionContentPart of type text
     */
    public static VisionContentPart createTextPart(String text) {
        VisionContentPart part = new VisionContentPart();
        part.setType("text");
        part.setText(text);
        return part;
    }
    
    /**
     * Creates an image content part with URL.
     * 
     * @param imageUrl the image URL
     * @return a new VisionContentPart of type image_url
     */
    public static VisionContentPart createImagePart(String imageUrl) {
        VisionContentPart part = new VisionContentPart();
        part.setType("image_url");
        part.setImageUrl(new VisionImageUrl(imageUrl));
        return part;
    }
    
    /**
     * Creates an image content part with detailed URL configuration.
     * 
     * @param imageUrl the VisionImageUrl object
     * @return a new VisionContentPart of type image_url
     */
    public static VisionContentPart createImagePart(VisionImageUrl imageUrl) {
        VisionContentPart part = new VisionContentPart();
        part.setType("image_url");
        part.setImageUrl(imageUrl);
        return part;
    }
    
    /**
     * Gets the type of content part.
     * 
     * @return the type (text or image_url)
     */
    public String getType() { 
        return type; 
    }
    
    /**
     * Sets the type of content part.
     * 
     * @param type the type (text or image_url)
     */
    public void setType(String type) { 
        this.type = type; 
    }
    
    /**
     * Gets the text content (for text type).
     * 
     * @return the text content, or null if not text type
     */
    public String getText() { 
        return text; 
    }
    
    /**
     * Sets the text content (for text type).
     * 
     * @param text the text content
     */
    public void setText(String text) { 
        this.text = text; 
    }
    
    /**
     * Gets the image URL (for image_url type).
     * 
     * @return the image URL, or null if not image_url type
     */
    public VisionImageUrl getImageUrl() { 
        return imageUrl; 
    }
    
    /**
     * Sets the image URL (for image_url type).
     * 
     * @param imageUrl the image URL
     */
    public void setImageUrl(VisionImageUrl imageUrl) { 
        this.imageUrl = imageUrl; 
    }
}
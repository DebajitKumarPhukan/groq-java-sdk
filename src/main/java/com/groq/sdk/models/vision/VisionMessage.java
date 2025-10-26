package com.groq.sdk.models.vision;

import java.util.List;

/**
 * Represents a message in a vision conversation containing multimodal content.
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Oct-2025
 * @version 1.0.0
 */
public class VisionMessage {
    private String role;
    private List<VisionContentPart> content;
    
    /**
     * Default constructor.
     */
    public VisionMessage() {}
    
    /**
     * Constructs a new VisionMessage with the specified role and content parts.
     * 
     * @param role the role of the message sender
     * @param content the list of content parts
     */
    public VisionMessage(String role, List<VisionContentPart> content) {
        this.role = role;
        this.content = content;
    }
    
    /**
     * Creates a user message with text and image content.
     * 
     * @param text the text content
     * @param imageUrl the image URL
     * @return a new VisionMessage with user role
     */
    public static VisionMessage createUserMessage(String text, String imageUrl) {
        VisionContentPart textPart = VisionContentPart.createTextPart(text);
        VisionContentPart imagePart = VisionContentPart.createImagePart(imageUrl);
        return new VisionMessage("user", List.of(textPart, imagePart));
    }
    
    /**
     * Creates a user message with text only.
     * 
     * @param text the text content
     * @return a new VisionMessage with user role
     */
    public static VisionMessage createUserMessage(String text) {
        VisionContentPart textPart = VisionContentPart.createTextPart(text);
        return new VisionMessage("user", List.of(textPart));
    }
    
    /**
     * Gets the role of the message sender.
     * 
     * @return the role (user, system, assistant)
     */
    public String getRole() { 
        return role; 
    }
    
    /**
     * Sets the role of the message sender.
     * 
     * @param role the role (user, system, assistant)
     */
    public void setRole(String role) { 
        this.role = role; 
    }
    
    /**
     * Gets the content parts.
     * 
     * @return the list of content parts
     */
    public List<VisionContentPart> getContent() { 
        return content; 
    }
    
    /**
     * Sets the content parts.
     * 
     * @param content the list of content parts
     */
    public void setContent(List<VisionContentPart> content) { 
        this.content = content; 
    }
}
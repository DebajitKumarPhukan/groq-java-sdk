package com.groq.sdk.models.vision;

import java.util.List;

import com.groq.sdk.models.chat.Usage;

/**
 * Represents the response from a vision processing request.
 * Contains the generated responses and usage statistics.
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Oct-2025
 * @version 1.0.0
 * @see VisionRequest
 */
public class VisionResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<VisionChoice> choices;
    private Usage usage;
    
    /**
     * Gets the unique identifier for the vision completion.
     * 
     * @return the completion ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique identifier for the vision completion.
     * 
     * @param id the completion ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * Gets the object type.
     * 
     * @return the object type
     */
    public String getObject() { 
        return object; 
    }
    
    /**
     * Sets the object type.
     * 
     * @param object the object type
     */
    public void setObject(String object) { 
        this.object = object; 
    }
    
    /**
     * Gets the creation timestamp.
     * 
     * @return the creation timestamp in seconds since epoch
     */
    public Long getCreated() { 
        return created; 
    }
    
    /**
     * Sets the creation timestamp.
     * 
     * @param created the creation timestamp in seconds since epoch
     */
    public void setCreated(Long created) { 
        this.created = created; 
    }
    
    /**
     * Gets the model used for vision processing.
     * 
     * @return the model identifier
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model used for vision processing.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the list of vision choices.
     * 
     * @return the list of vision choices
     * @see VisionChoice
     */
    public List<VisionChoice> getChoices() { 
        return choices; 
    }
    
    /**
     * Sets the list of vision choices.
     * 
     * @param choices the list of vision choices
     * @see VisionChoice
     */
    public void setChoices(List<VisionChoice> choices) { 
        this.choices = choices; 
    }
    
    /**
     * Gets the token usage statistics.
     * 
     * @return the usage statistics
     * @see Usage
     */
    public Usage getUsage() { 
        return usage; 
    }
    
    /**
     * Sets the token usage statistics.
     * 
     * @param usage the usage statistics
     * @see Usage
     */
    public void setUsage(Usage usage) { 
        this.usage = usage; 
    }
}
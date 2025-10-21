package com.groq.sdk.models.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an AI model available in the Groq API.
 * Contains model metadata including ID, creation time, and ownership information.
 * 
 * @author Debajit Kumar Phukan
 * @since 21-Sep-2025
 * @version 1.0.0
 * @see ModelList
 */
public class Model {
    private String id;
    private String object;
    private Long created;
    private String ownedBy;
    
    /**
     * Default constructor.
     */
    public Model() {}
    
    /**
     * Constructs a new Model with the specified properties.
     * 
     * @param id the model ID
     * @param object the object type
     * @param created the creation timestamp
     * @param ownedBy the owner of the model
     */
    public Model(String id, String object, Long created, String ownedBy) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.ownedBy = ownedBy;
    }
    
    /**
     * Gets the unique identifier for the model.
     * 
     * @return the model ID (e.g., "openai/gpt-oss-20b", "mixtral-8x7b-32768")
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique identifier for the model.
     * 
     * @param id the model ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * Gets the object type (usually "model").
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
     * Gets the timestamp when the model was created.
     * 
     * @return the creation timestamp in seconds since epoch
     */
    public Long getCreated() { 
        return created; 
    }
    
    /**
     * Sets the timestamp when the model was created.
     * 
     * @param created the creation timestamp in seconds since epoch
     */
    public void setCreated(Long created) { 
        this.created = created; 
    }
    
    /**
     * Gets the owner of the model.
     * 
     * @return the model owner
     */
    @JsonProperty("owned_by")
    public String getOwnedBy() { 
        return ownedBy; 
    }
    
    /**
     * Sets the owner of the model.
     * 
     * @param ownedBy the model owner
     */
    public void setOwnedBy(String ownedBy) { 
        this.ownedBy = ownedBy; 
    }
    
    /**
     * Returns a string representation of the model.
     * 
     * @return a string containing model ID, object type, creation time, and owner
     */
    @Override
    public String toString() {
        return "Model{id='" + id + "', object='" + object + "', created=" + created + ", ownedBy='" + ownedBy + "'}";
    }
}
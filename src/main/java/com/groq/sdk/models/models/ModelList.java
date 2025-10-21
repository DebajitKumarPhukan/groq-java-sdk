package com.groq.sdk.models.models;

import java.util.List;

/**
 * Represents a list of available models with pagination support.
 * Contains model objects and list metadata for model discovery.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Sep-2025
 * @version 1.0.0
 * @see Model
 */
public class ModelList {
    private String object;
    private List<Model> data;
    
    /**
     * Default constructor.
     */
    public ModelList() {}
    
    /**
     * Constructs a new ModelList with the specified object type and data.
     * 
     * @param object the object type
     * @param data the list of model objects
     */
    public ModelList(String object, List<Model> data) {
        this.object = object;
        this.data = data;
    }
    
    /**
     * Gets the object type (usually "list").
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
     * Gets the list of model objects.
     * 
     * @return the list of models
     * @see Model
     */
    public List<Model> getData() { 
        return data; 
    }
    
    /**
     * Sets the list of model objects.
     * 
     * @param data the list of models
     * @see Model
     */
    public void setData(List<Model> data) { 
        this.data = data; 
    }
}
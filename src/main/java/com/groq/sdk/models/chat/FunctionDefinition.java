package com.groq.sdk.models.chat;

import java.util.Map;

/**
 * Represents a function definition that can be called by the model.
 * Contains the function name, description, and parameters schema.
 * 
 * @author Debajit Kumar Phukan
 * @since 31-Aug-2025
 * @version 1.0.0
 * @see ChatTool
 */
public class FunctionDefinition {
    private String name;
    private String description;
    private Map<String, Object> parameters;
    
    /**
     * Default constructor.
     */
    public FunctionDefinition() {}
    
    /**
     * Constructs a new FunctionDefinition with the specified name.
     * 
     * @param name the function name
     */
    public FunctionDefinition(String name) {
        this.name = name;
    }
    
    /**
     * Constructs a new FunctionDefinition with all fields.
     * 
     * @param name the function name
     * @param description the function description
     * @param parameters the function parameters schema
     */
    public FunctionDefinition(String name, String description, Map<String, Object> parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }
    
    /**
     * Gets the name of the function.
     * 
     * @return the function name
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Sets the name of the function.
     * 
     * @param name the function name
     */
    public void setName(String name) { 
        this.name = name; 
    }
    
    /**
     * Gets the description of what the function does.
     * 
     * @return the function description
     */
    public String getDescription() { 
        return description; 
    }
    
    /**
     * Sets the description of what the function does.
     * 
     * @param description the function description
     */
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    /**
     * Gets the parameters schema as a JSON Schema object.
     * 
     * @return the parameters schema
     */
    public Map<String, Object> getParameters() { 
        return parameters; 
    }
    
    /**
     * Sets the parameters schema as a JSON Schema object.
     * 
     * @param parameters the parameters schema
     */
    public void setParameters(Map<String, Object> parameters) { 
        this.parameters = parameters; 
    }
}
package com.groq.sdk.models.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a specific named tool choice that forces the model to call a particular tool.
 * 
 * @author Debajit Kumar Phukan
 * @since 31-Aug-2025
 * @version 1.0.0
 * @see ChatCompletionRequest
 */
public class ChatNamedToolChoice {
    private String type = "function";
    private NamedToolFunction function;
    
    /**
     * Default constructor.
     */
    public ChatNamedToolChoice() {}
    
    /**
     * Constructs a new ChatNamedToolChoice with the specified function name.
     * 
     * @param functionName the name of the function to call
     */
    public ChatNamedToolChoice(String functionName) {
        this.function = new NamedToolFunction(functionName);
    }
    
    /**
     * Gets the type of the tool (always "function").
     * 
     * @return the tool type
     */
    @JsonProperty("type")
    public String getType() { 
        return type; 
    }
    
    /**
     * Sets the type of the tool.
     * 
     * @param type the tool type
     */
    public void setType(String type) { 
        this.type = type; 
    }
    
    /**
     * Gets the function specification.
     * 
     * @return the function specification
     */
    @JsonProperty("function")
    public NamedToolFunction getFunction() { 
        return function; 
    }
    
    /**
     * Sets the function specification.
     * 
     * @param function the function specification
     */
    public void setFunction(NamedToolFunction function) { 
        this.function = function; 
    }
    
    /**
     * Represents the function specification within a named tool choice.
     */
    public static class NamedToolFunction {
        private String name;
        
        /**
         * Default constructor.
         */
        public NamedToolFunction() {}
        
        /**
         * Constructs a new NamedToolFunction with the specified name.
         * 
         * @param name the function name
         */
        public NamedToolFunction(String name) {
            this.name = name;
        }
        
        /**
         * Gets the name of the function to call.
         * 
         * @return the function name
         */
        @JsonProperty("name")
        public String getName() { 
            return name; 
        }
        
        /**
         * Sets the name of the function to call.
         * 
         * @param name the function name
         */
        public void setName(String name) { 
            this.name = name; 
        }
    }
}
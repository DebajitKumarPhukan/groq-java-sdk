package com.groq.sdk.models.chat;

/**
 * Represents a tool that can be called by the model.
 * Currently supports function tools with browser_search and code_interpreter types.
 * 
 * @author Debajit Kumar Phukan
 * @since 31-Aug-2025
 * @version 1.0.0
 * @see ChatCompletionRequest
 * @see FunctionDefinition
 */
public class ChatTool {
    private String type;
    private FunctionDefinition function;
    
    /**
     * Default constructor.
     */
    public ChatTool() {}
    
    /**
     * Constructs a new ChatTool with the specified type and function.
     * 
     * @param type the tool type (function, browser_search, code_interpreter)
     * @param function the function definition
     */
    public ChatTool(String type, FunctionDefinition function) {
        this.type = type;
        this.function = function;
    }
    
    /**
     * Gets the type of the tool.
     * 
     * @return the tool type
     */
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
     * Gets the function definition for function tools.
     * 
     * @return the function definition
     * @see FunctionDefinition
     */
    public FunctionDefinition getFunction() { 
        return function; 
    }
    
    /**
     * Sets the function definition for function tools.
     * 
     * @param function the function definition
     * @see FunctionDefinition
     */
    public void setFunction(FunctionDefinition function) { 
        this.function = function; 
    }
}
package com.groq.sdk.models.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groq.sdk.models.responses.ResponseOutput;

/**
 * Represents MCP call output in Response API responses.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ResponseOutput
 */
public class MCPCallOutput extends ResponseOutput {
    
    @JsonProperty("server_label")
    private String serverLabel;
    
    private String name;
    private String arguments;
    private String output;
    
    /**
     * Default constructor.
     */
    public MCPCallOutput() {
        setType("mcp_call");
    }
    
    /**
     * Gets the server label.
     * 
     * @return the server label
     */
    @JsonProperty("server_label")
    public String getServerLabel() { 
        return serverLabel; 
    }
    
    /**
     * Sets the server label.
     * 
     * @param serverLabel the server label
     */
    @JsonProperty("server_label")
    public void setServerLabel(String serverLabel) { 
        this.serverLabel = serverLabel; 
    }
    
    /**
     * Gets the tool name.
     * 
     * @return the tool name
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Sets the tool name.
     * 
     * @param name the tool name
     */
    public void setName(String name) { 
        this.name = name; 
    }
    
    /**
     * Gets the tool arguments.
     * 
     * @return the tool arguments as JSON string
     */
    public String getArguments() { 
        return arguments; 
    }
    
    /**
     * Sets the tool arguments.
     * 
     * @param arguments the tool arguments as JSON string
     */
    public void setArguments(String arguments) { 
        this.arguments = arguments; 
    }
    
    /**
     * Gets the tool output.
     * 
     * @return the tool output
     */
    public String getOutput() { 
        return output; 
    }
    
    /**
     * Sets the tool output.
     * 
     * @param output the tool output
     */
    public void setOutput(String output) { 
        this.output = output; 
    }
}
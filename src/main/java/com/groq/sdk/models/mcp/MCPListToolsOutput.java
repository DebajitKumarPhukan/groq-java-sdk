package com.groq.sdk.models.mcp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groq.sdk.models.responses.ResponseOutput;

/**
 * Represents MCP tools list output in Response API responses.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ResponseOutput
 */
public class MCPListToolsOutput extends ResponseOutput {
    
    @JsonProperty("server_label")
    private String serverLabel;
    
    private List<MCPTool> tools;
    
    /**
     * Default constructor.
     */
    public MCPListToolsOutput() {
        setType("mcp_list_tools");
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
     * Gets the list of MCP tools.
     * 
     * @return the list of MCP tools
     */
    public List<MCPTool> getTools() { 
        return tools; 
    }
    
    /**
     * Sets the list of MCP tools.
     * 
     * @param tools the list of MCP tools
     */
    public void setTools(List<MCPTool> tools) { 
        this.tools = tools; 
    }
}
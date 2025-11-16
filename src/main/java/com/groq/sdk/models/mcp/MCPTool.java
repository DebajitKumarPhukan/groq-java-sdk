package com.groq.sdk.models.mcp;

/**
 * Represents an MCP tool that can be called by the model through the Response API.
 * Contains the tool name and description for MCP servers.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * MCPTool tool = new MCPTool("firecrawl_scrape", 
 *     "Scrape content from a single URL with advanced options");
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see MCPListToolsOutput
 * @see com.groq.sdk.models.responses.Response
 */
public class MCPTool {
    private String name;
    private String description;
    
    /**
     * Default constructor.
     */
    public MCPTool() {}
    
    /**
     * Constructs a new MCPTool with the specified name and description.
     * 
     * @param name the tool name
     * @param description the tool description
     */
    public MCPTool(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * Gets the name of the tool.
     * 
     * @return the tool name
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Sets the name of the tool.
     * 
     * @param name the tool name
     */
    public void setName(String name) { 
        this.name = name; 
    }
    
    /**
     * Gets the description of what the tool does.
     * 
     * @return the tool description
     */
    public String getDescription() { 
        return description; 
    }
    
    /**
     * Sets the description of what the tool does.
     * 
     * @param description the tool description
     */
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    /**
     * Returns a string representation of the MCP tool.
     * 
     * @return a string containing the tool name and description
     */
    @Override
    public String toString() {
        return "MCPTool{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MCPTool mcpTool = (MCPTool) obj;
        
        if (name != null ? !name.equals(mcpTool.name) : mcpTool.name != null) return false;
        return description != null ? description.equals(mcpTool.description) : mcpTool.description == null;
    }
    
    /**
     * Returns a hash code value for the object.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
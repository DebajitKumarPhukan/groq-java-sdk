package com.groq.sdk.models.responses;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.groq.sdk.models.mcp.MCPCallOutput;
import com.groq.sdk.models.mcp.MCPListToolsOutput;

/**
 * Base class for Response API output types.
 * Uses Jackson polymorphism to handle different output types.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see Response
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ReasoningOutput.class, name = "reasoning"),
    @JsonSubTypes.Type(value = MessageOutput.class, name = "message"),
    @JsonSubTypes.Type(value = MCPListToolsOutput.class, name = "mcp_list_tools"), // NEW
    @JsonSubTypes.Type(value = MCPCallOutput.class, name = "mcp_call") // NEW
})
public abstract class ResponseOutput {
    private String type;
    private String id;
    private String status;
    
    /**
     * Gets the output type.
     * 
     * @return the output type
     */
    public String getType() { 
        return type; 
    }
    
    /**
     * Sets the output type.
     * 
     * @param type the output type
     */
    public void setType(String type) { 
        this.type = type; 
    }
    
    /**
     * Gets the output ID.
     * 
     * @return the output ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the output ID.
     * 
     * @param id the output ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * Gets the output status.
     * 
     * @return the output status
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * Sets the output status.
     * 
     * @param status the output status
     */
    public void setStatus(String status) { 
        this.status = status; 
    }
}
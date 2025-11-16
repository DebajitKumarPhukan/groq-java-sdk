package com.groq.sdk.models.mcp;

import java.util.List;

import com.groq.sdk.models.responses.ReasoningContent;

/**
 * Represents reasoning output in MCP responses.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see MCPOutput
 */
public class ReasoningOutput {
    private String type = "reasoning";
    private List<ReasoningContent> content;
    
    /**
     * Default constructor.
     */
    public ReasoningOutput() {}
    
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
     * Gets the reasoning content.
     * 
     * @return the reasoning content
     */
    public List<ReasoningContent> getContent() { 
        return content; 
    }
    
    /**
     * Sets the reasoning content.
     * 
     * @param content the reasoning content
     */
    public void setContent(List<ReasoningContent> content) { 
        this.content = content; 
    }
}
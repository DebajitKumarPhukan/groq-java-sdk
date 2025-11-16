package com.groq.sdk.models.responses;

import java.util.List;

/**
 * Represents reasoning output in Response API responses.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ResponseOutput
 */
public class ReasoningOutput extends ResponseOutput {
    private List<ReasoningContent> content;
    private List<Object> summary;
    
    /**
     * Default constructor.
     */
    public ReasoningOutput() {
        setType("reasoning");
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
    
    /**
     * Gets the reasoning summary.
     * 
     * @return the reasoning summary
     */
    public List<Object> getSummary() { 
        return summary; 
    }
    
    /**
     * Sets the reasoning summary.
     * 
     * @param summary the reasoning summary
     */
    public void setSummary(List<Object> summary) { 
        this.summary = summary; 
    }
}
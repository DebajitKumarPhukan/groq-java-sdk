package com.groq.sdk.models.responses;

import java.util.List;

/**
 * Represents message content in Response API responses.
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see MessageOutput
 */
public class MessageContent {
    private String type;
    private String text;
    private List<Object> annotations;
    private Object logprobs;
    
    /**
     * Default constructor.
     */
    public MessageContent() {}
    
    /**
     * Gets the content type.
     * 
     * @return the content type
     */
    public String getType() { 
        return type; 
    }
    
    /**
     * Sets the content type.
     * 
     * @param type the content type
     */
    public void setType(String type) { 
        this.type = type; 
    }
    
    /**
     * Gets the message text.
     * 
     * @return the message text
     */
    public String getText() { 
        return text; 
    }
    
    /**
     * Sets the message text.
     * 
     * @param text the message text
     */
    public void setText(String text) { 
        this.text = text; 
    }
    
    /**
     * Gets the content annotations.
     * 
     * @return the annotations
     */
    public List<Object> getAnnotations() { 
        return annotations; 
    }
    
    /**
     * Sets the content annotations.
     * 
     * @param annotations the annotations
     */
    public void setAnnotations(List<Object> annotations) { 
        this.annotations = annotations; 
    }
    
    /**
     * Gets the log probabilities.
     * 
     * @return the log probabilities
     */
    public Object getLogprobs() { 
        return logprobs; 
    }
    
    /**
     * Sets the log probabilities.
     * 
     * @param logprobs the log probabilities
     */
    public void setLogprobs(Object logprobs) { 
        this.logprobs = logprobs; 
    }
}
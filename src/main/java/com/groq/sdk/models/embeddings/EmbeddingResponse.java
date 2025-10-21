package com.groq.sdk.models.embeddings;

import java.util.List;
import com.groq.sdk.models.chat.Usage;

/**
 * Represents the response from an embedding generation request.
 * Contains the generated embeddings and usage statistics.
 * 
 * @author Debajit Kumar Phukan
 * @since 04-Sep-2025
 * @version 1.0.0
 * @see EmbeddingRequest
 * @see EmbeddingData
 * @see Usage
 */
public class EmbeddingResponse {
    private String object;
    private List<EmbeddingData> data;
    private String model;
    private Usage usage;
    
    /**
     * Gets the object type (usually "list").
     * 
     * @return the object type
     */
    public String getObject() { 
        return object; 
    }
    
    /**
     * Sets the object type.
     * 
     * @param object the object type
     */
    public void setObject(String object) { 
        this.object = object; 
    }
    
    /**
     * Gets the list of generated embedding data.
     * 
     * @return the list of embedding data
     * @see EmbeddingData
     */
    public List<EmbeddingData> getData() { 
        return data; 
    }
    
    /**
     * Sets the list of generated embedding data.
     * 
     * @param data the list of embedding data
     * @see EmbeddingData
     */
    public void setData(List<EmbeddingData> data) { 
        this.data = data; 
    }
    
    /**
     * Gets the model used for embedding generation.
     * 
     * @return the model identifier
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model used for embedding generation.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the token usage statistics for the embedding generation.
     * 
     * @return the usage statistics
     * @see Usage
     */
    public Usage getUsage() { 
        return usage; 
    }
    
    /**
     * Sets the token usage statistics for the embedding generation.
     * 
     * @param usage the usage statistics
     * @see Usage
     */
    public void setUsage(Usage usage) { 
        this.usage = usage; 
    }
}
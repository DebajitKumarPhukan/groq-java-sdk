package com.groq.sdk.models.embeddings;

import java.util.List;

/**
 * Represents a single embedding vector with metadata.
 * Contains the embedding vector and its position in the input sequence.
 * 
 * @author Debajit Kumar Phukan
 * @since 05-Sep-2025
 * @version 1.0.0
 * @see EmbeddingResponse
 */
public class EmbeddingData {
    private String object;
    private List<Double> embedding;
    private Integer index;
    
    /**
     * Gets the object type (usually "embedding").
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
     * Gets the embedding vector as a list of numerical values.
     * 
     * @return the embedding vector
     */
    public List<Double> getEmbedding() { 
        return embedding; 
    }
    
    /**
     * Sets the embedding vector as a list of numerical values.
     * 
     * @param embedding the embedding vector
     */
    public void setEmbedding(List<Double> embedding) { 
        this.embedding = embedding; 
    }
    
    /**
     * Gets the index of this embedding in the input sequence.
     * 
     * @return the embedding index
     */
    public Integer getIndex() { 
        return index; 
    }
    
    /**
     * Sets the index of this embedding in the input sequence.
     * 
     * @param index the embedding index
     */
    public void setIndex(Integer index) { 
        this.index = index; 
    }
}
package com.groq.sdk.models.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a request for generating text embeddings.
 * Contains the input texts and model parameters for embedding generation.
 * 
 * <p>Embeddings are numerical representations of text that capture semantic meaning
 * and can be used for similarity search, clustering, and other NLP tasks.</p>
 * 
 * @author Debajit Kumar Phukan
 * @since 04-Sep-2025
 * @version 1.0.0
 * @see EmbeddingResponse
 * @see EmbeddingData
 */
public class EmbeddingRequest {
    private String model;
    private List<String> input;
    
    @JsonProperty("encoding_format")
    private String encodingFormat;
    
    private String user;
    
    /**
     * Default constructor.
     */
    public EmbeddingRequest() {}
    
    /**
     * Constructs a new EmbeddingRequest with the specified model and input texts.
     * 
     * @param model the model to use for embedding generation
     * @param input the list of input texts to embed
     */
    public EmbeddingRequest(String model, List<String> input) {
        this.model = model;
        this.input = input;
    }
    
    /**
     * Gets the model to use for embedding generation.
     * 
     * @return the model identifier (e.g., "text-embedding-ada-002")
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model to use for embedding generation.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the list of input texts to generate embeddings for.
     * 
     * @return the list of input texts
     */
    public List<String> getInput() { 
        return input; 
    }
    
    /**
     * Sets the list of input texts to generate embeddings for.
     * 
     * @param input the list of input texts
     */
    public void setInput(List<String> input) { 
        this.input = input; 
    }
    
    /**
     * Gets the encoding format for the embeddings.
     * 
     * @return the encoding format (e.g., "float", "base64")
     */
    public String getEncodingFormat() { 
        return encodingFormat; 
    }
    
    /**
     * Sets the encoding format for the embeddings.
     * 
     * @param encodingFormat the encoding format
     */
    public void setEncodingFormat(String encodingFormat) { 
        this.encodingFormat = encodingFormat; 
    }
    
    /**
     * Gets the optional user identifier for tracking purposes.
     * 
     * @return the user identifier, or null if not set
     */
    public String getUser() { 
        return user; 
    }
    
    /**
     * Sets the optional user identifier for tracking purposes.
     * 
     * @param user the user identifier
     */
    public void setUser(String user) { 
        this.user = user; 
    }
}
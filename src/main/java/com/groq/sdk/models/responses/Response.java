package com.groq.sdk.models.responses;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the complete response from the Groq Response API.
 * Contains outputs, usage statistics, and metadata about the response.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * GroqResponse<Response> response = client.responses().create(request);
 * if (response.isSuccessful()) {
 *     Response resp = response.getData();
 *     List<ResponseOutput> outputs = resp.getOutput();
 *     // Process outputs
 * }
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see ResponseRequest
 * @see ResponseOutput
 */
public class Response {
    private String id;
    private String object;
    private String status;
    
    @JsonProperty("created_at")
    private Long createdAt;
    
    private List<ResponseOutput> output;
    
    @JsonProperty("previous_response_id")
    private String previousResponseId;
    
    private String model;
    private ReasoningConfig reasoning;
    private Usage usage;
    private Map<String, Object> metadata;
    
    @JsonProperty("incomplete_details")
    private Object incompleteDetails;
    
    private Object error;
    
    /**
     * Default constructor.
     */
    public Response() {}
    
    /**
     * Gets the unique identifier for the response.
     * 
     * @return the response ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique identifier for the response.
     * 
     * @param id the response ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * Gets the object type (usually "response").
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
     * Gets the status of the response.
     * 
     * @return the status (e.g., "completed", "in_progress")
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * Sets the status of the response.
     * 
     * @param status the status
     */
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    /**
     * Gets the creation timestamp.
     * 
     * @return the creation timestamp in seconds since epoch
     */
    public Long getCreatedAt() { 
        return createdAt; 
    }
    
    /**
     * Sets the creation timestamp.
     * 
     * @param createdAt the creation timestamp in seconds since epoch
     */
    public void setCreatedAt(Long createdAt) { 
        this.createdAt = createdAt; 
    }
    
    /**
     * Gets the list of response outputs.
     * 
     * @return the list of outputs
     */
    public List<ResponseOutput> getOutput() { 
        return output; 
    }
    
    /**
     * Sets the list of response outputs.
     * 
     * @param output the list of outputs
     */
    public void setOutput(List<ResponseOutput> output) { 
        this.output = output; 
    }
    
    /**
     * Gets the previous response ID.
     * 
     * @return the previous response ID
     */
    public String getPreviousResponseId() { 
        return previousResponseId; 
    }
    
    /**
     * Sets the previous response ID.
     * 
     * @param previousResponseId the previous response ID
     */
    public void setPreviousResponseId(String previousResponseId) { 
        this.previousResponseId = previousResponseId; 
    }
    
    /**
     * Gets the model used for the response.
     * 
     * @return the model identifier
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model used for the response.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the reasoning configuration.
     * 
     * @return the reasoning configuration
     */
    public ReasoningConfig getReasoning() { 
        return reasoning; 
    }
    
    /**
     * Sets the reasoning configuration.
     * 
     * @param reasoning the reasoning configuration
     */
    public void setReasoning(ReasoningConfig reasoning) { 
        this.reasoning = reasoning; 
    }
    
    /**
     * Gets the token usage statistics.
     * 
     * @return the usage statistics
     */
    public Usage getUsage() { 
        return usage; 
    }
    
    /**
     * Sets the token usage statistics.
     * 
     * @param usage the usage statistics
     */
    public void setUsage(Usage usage) { 
        this.usage = usage; 
    }
    
    /**
     * Gets the response metadata.
     * 
     * @return the metadata
     */
    public Map<String, Object> getMetadata() { 
        return metadata; 
    }
    
    /**
     * Sets the response metadata.
     * 
     * @param metadata the metadata
     */
    public void setMetadata(Map<String, Object> metadata) { 
        this.metadata = metadata; 
    }
    
    /**
     * Gets the incomplete details.
     * 
     * @return the incomplete details
     */
    public Object getIncompleteDetails() { 
        return incompleteDetails; 
    }
    
    /**
     * Sets the incomplete details.
     * 
     * @param incompleteDetails the incomplete details
     */
    public void setIncompleteDetails(Object incompleteDetails) { 
        this.incompleteDetails = incompleteDetails; 
    }
    
    /**
     * Gets the error information.
     * 
     * @return the error information
     */
    public Object getError() { 
        return error; 
    }
    
    /**
     * Sets the error information.
     * 
     * @param error the error information
     */
    public void setError(Object error) { 
        this.error = error; 
    }
}
package com.groq.sdk.models.chat;

import java.util.List;

/**
 * Represents the response from a chat completion request.
 * Contains the generated responses, usage statistics, and metadata about the completion.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
 * if (response.isSuccessful()) {
 *     ChatCompletion completion = response.getData();
 *     String content = completion.getChoices().get(0).getMessage().getContent();
 *     System.out.println("Response: " + content);
 * }
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 03-Sep-2025
 * @version 1.0.0
 * @see ChatCompletionRequest
 * @see ChatChoice
 * @see ChatMessage
 * @see Usage
 */
public class ChatCompletion {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
    
    /**
     * Gets the unique identifier for the chat completion.
     * 
     * @return the completion ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique identifier for the chat completion.
     * 
     * @param id the completion ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * Gets the object type (usually "chat.completion").
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
     * Gets the timestamp when the completion was created.
     * 
     * @return the creation timestamp in seconds since epoch
     */
    public Long getCreated() { 
        return created; 
    }
    
    /**
     * Sets the timestamp when the completion was created.
     * 
     * @param created the creation timestamp in seconds since epoch
     */
    public void setCreated(Long created) { 
        this.created = created; 
    }
    
    /**
     * Gets the model used for the completion.
     * 
     * @return the model identifier
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model used for the completion.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the list of completion choices.
     * 
     * @return the list of chat choices
     * @see ChatChoice
     */
    public List<ChatChoice> getChoices() { 
        return choices; 
    }
    
    /**
     * Sets the list of completion choices.
     * 
     * @param choices the list of chat choices
     * @see ChatChoice
     */
    public void setChoices(List<ChatChoice> choices) { 
        this.choices = choices; 
    }
    
    /**
     * Gets the token usage statistics for the completion.
     * 
     * @return the usage statistics
     * @see Usage
     */
    public Usage getUsage() { 
        return usage; 
    }
    
    /**
     * Sets the token usage statistics for the completion.
     * 
     * @param usage the usage statistics
     * @see Usage
     */
    public void setUsage(Usage usage) { 
        this.usage = usage; 
    }
}
package com.groq.sdk.resources;

import java.util.Collections;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.chat.ChatCompletion;
import com.groq.sdk.models.chat.ChatCompletionRequest;
import com.groq.sdk.models.chat.ChatMessage;

/**
 * Provides access to chat completion operations in the Groq API.
 * Handles creating chat completions with various models and parameters with comprehensive error handling.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * ChatResource chat = client.chat();
 * ChatCompletionRequest request = new ChatCompletionRequest("openai/gpt-oss-20b", messages);
 * GroqResponse<ChatCompletion> response = chat.createCompletion(request);
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 01-Oct-2025
 * @version 1.0.0
 * @see GroqClient
 * @see ChatCompletionRequest
 * @see ChatCompletion
 */
public class ChatResource {
    private final GroqClient client;
    
    /**
     * Constructs a new ChatResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public ChatResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Creates a chat completion with the specified request parameters.
     * 
     * @param request the chat completion request containing model, messages, and parameters
     * @return the API response containing chat completion results
     * @throws IllegalArgumentException if request is null, model is empty, or messages are empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<ChatCompletion> createCompletion(ChatCompletionRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("ChatCompletionRequest cannot be null");
            }
            if (request.getModel() == null || request.getModel().trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be null or empty");
            }
            if (request.getMessages() == null || request.getMessages().isEmpty()) {
                throw new IllegalArgumentException("Messages cannot be null or empty");
            }
            
            return client.post("/openai/v1/chat/completions", request, ChatCompletion.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create chat completion: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a simple chat completion with a single message.
     * 
     * @param model the model to use for completion
     * @param message the user message content
     * @return the API response containing chat completion results
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<ChatCompletion> createCompletion(String model, String message) {
        try {
            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setModel(model);
            request.setMessages(java.util.Collections.singletonList(
                new ChatMessage("user", message)
            ));
            return createCompletion(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create simple chat completion: " + e.getMessage(), e);
        }
    }
}
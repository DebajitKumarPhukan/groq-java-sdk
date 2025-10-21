package com.groq.sdk.models.chat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request for chat completion to the Groq API.
 * Contains all parameters for generating chat responses including model selection,
 * message history, tool definitions, and generation parameters.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * ChatMessage message = new ChatMessage("user", "Hello, how are you?");
 * ChatCompletionRequest request = new ChatCompletionRequest("openai/gpt-oss-20b", List.of(message));
 * request.setMaxTokens(100);
 * request.setTemperature(0.7);
 * }</pre>
 * 
 * <p><strong>Example usage with tools:</strong></p>
 * <pre>{@code
 * ChatMessage message = new ChatMessage("user", "What's the weather in San Francisco?");
 * ChatCompletionRequest request = new ChatCompletionRequest("llama-3.1-8b-instant", List.of(message));
 * 
 * // Define a tool
 * FunctionDefinition function = new FunctionDefinition("get_weather", "Get weather for a location");
 * ChatTool tool = new ChatTool("function", function);
 * request.setTools(List.of(tool));
 * request.setToolChoice("auto");
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Aug-2025
 * @version 1.0.0
 * @see ChatCompletion
 * @see ChatMessage
 * @see ChatTool
 */
public class ChatCompletionRequest {
    private String model;
    private List<ChatMessage> messages;
    
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    
    private Double temperature;
    
    @JsonProperty("top_p")
    private Double topP;
    
    private Boolean stream;
    
    private String stop;
    
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;
    
    @JsonProperty("presence_penalty")
    private Double presencePenalty;
    
    private List<ChatTool> tools;
    
    @JsonProperty("tool_choice")
    private Object toolChoice;
    
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;
    
    /**
     * Default constructor.
     */
    public ChatCompletionRequest() {}
    
    /**
     * Constructs a new ChatCompletionRequest with the specified model and messages.
     * 
     * @param model the model to use for completion
     * @param messages the list of chat messages
     */
    public ChatCompletionRequest(String model, List<ChatMessage> messages) {
        this.model = model;
        this.messages = messages;
    }
    
    /**
     * Gets the model to use for completion (e.g., "llama2-70b-4096").
     * 
     * @return the model identifier
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model to use for completion.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the list of messages for the conversation.
     * 
     * @return the list of chat messages
     * @see ChatMessage
     */
    public List<ChatMessage> getMessages() { 
        return messages; 
    }
    
    /**
     * Sets the list of messages for the conversation.
     * 
     * @param messages the list of chat messages
     * @see ChatMessage
     */
    public void setMessages(List<ChatMessage> messages) { 
        this.messages = messages; 
    }
    
    /**
     * Gets the maximum number of tokens to generate in the completion.
     * 
     * @return the maximum token count, or null if not set
     */
    public Integer getMaxTokens() { 
        return maxTokens; 
    }
    
    /**
     * Sets the maximum number of tokens to generate in the completion.
     * 
     * @param maxTokens the maximum token count
     */
    public void setMaxTokens(Integer maxTokens) { 
        this.maxTokens = maxTokens; 
    }
    
    /**
     * Gets the sampling temperature (0.0 to 1.0).
     * Higher values make the output more random, lower values more deterministic.
     * 
     * @return the temperature value, or null if not set
     */
    public Double getTemperature() { 
        return temperature; 
    }
    
    /**
     * Sets the sampling temperature (0.0 to 1.0).
     * Higher values make the output more random, lower values more deterministic.
     * 
     * @param temperature the temperature value
     */
    public void setTemperature(Double temperature) { 
        this.temperature = temperature; 
    }
    
    /**
     * Gets the top-p sampling parameter (0.0 to 1.0).
     * Only the most likely tokens with cumulative probability >= top_p are considered.
     * 
     * @return the top-p value, or null if not set
     */
    public Double getTopP() { 
        return topP; 
    }
    
    /**
     * Sets the top-p sampling parameter (0.0 to 1.0).
     * Only the most likely tokens with cumulative probability >= top_p are considered.
     * 
     * @param topP the top-p value
     */
    public void setTopP(Double topP) { 
        this.topP = topP; 
    }
    
    /**
     * Gets whether to stream the response incrementally.
     * 
     * @return true if streaming is enabled, false otherwise, or null if not set
     */
    public Boolean getStream() { 
        return stream; 
    }
    
    /**
     * Sets whether to stream the response incrementally.
     * 
     * @param stream true to enable streaming, false to disable
     */
    public void setStream(Boolean stream) { 
        this.stream = stream; 
    }
    
    /**
     * Gets the stop sequence that will cause the model to stop generating.
     * 
     * @return the stop sequence, or null if not set
     */
    public String getStop() { 
        return stop; 
    }
    
    /**
     * Sets the stop sequence that will cause the model to stop generating.
     * 
     * @param stop the stop sequence
     */
    public void setStop(String stop) { 
        this.stop = stop; 
    }
    
    /**
     * Gets the frequency penalty parameter (-2.0 to 2.0).
     * Positive values penalize new tokens based on their existing frequency.
     * 
     * @return the frequency penalty value, or null if not set
     */
    public Double getFrequencyPenalty() { 
        return frequencyPenalty; 
    }
    
    /**
     * Sets the frequency penalty parameter (-2.0 to 2.0).
     * Positive values penalize new tokens based on their existing frequency.
     * 
     * @param frequencyPenalty the frequency penalty value
     */
    public void setFrequencyPenalty(Double frequencyPenalty) { 
        this.frequencyPenalty = frequencyPenalty; 
    }
    
    /**
     * Gets the presence penalty parameter (-2.0 to 2.0).
     * Positive values penalize new tokens based on whether they appear in the text so far.
     * 
     * @return the presence penalty value, or null if not set
     */
    public Double getPresencePenalty() { 
        return presencePenalty; 
    }
    
    /**
     * Sets the presence penalty parameter (-2.0 to 2.0).
     * Positive values penalize new tokens based on whether they appear in the text so far.
     * 
     * @param presencePenalty the presence penalty value
     */
    public void setPresencePenalty(Double presencePenalty) { 
        this.presencePenalty = presencePenalty; 
    }
    
    /**
     * Gets the tools the model may call.
     * Currently, only functions are supported as a tool. Use this to provide a list of
     * functions the model may generate JSON inputs for. A max of 128 functions are supported.
     * 
     * @return the list of tools, or null if not set
     * @see ChatTool
     */
    public List<ChatTool> getTools() { 
        return tools; 
    }
    
    /**
     * Sets the tools the model may call.
     * Currently, only functions are supported as a tool. Use this to provide a list of
     * functions the model may generate JSON inputs for. A max of 128 functions are supported.
     * 
     * @param tools the list of tools
     * @see ChatTool
     */
    public void setTools(List<ChatTool> tools) { 
        this.tools = tools; 
    }
    
    /**
     * Gets the tool choice configuration.
     * Controls which (if any) tool is called by the model. `none` means the model will
     * not call any tool and instead generates a message. `auto` means the model can
     * pick between generating a message or calling one or more tools. `required` means
     * the model must call one or more tools. Specifying a particular tool via
     * `ChatNamedToolChoice` forces the model to call that tool.
     * 
     * `none` is the default when no tools are present. `auto` is the default if tools are present.
     * 
     * @return the tool choice, or null if not set
     */
    public Object getToolChoice() { 
        return toolChoice; 
    }
    
    /**
     * Sets the tool choice configuration.
     * Controls which (if any) tool is called by the model. `none` means the model will
     * not call any tool and instead generates a message. `auto` means the model can
     * pick between generating a message or calling one or more tools. `required` means
     * the model must call one or more tools. Specifying a particular tool via
     * `ChatNamedToolChoice` forces the model to call that tool.
     * 
     * `none` is the default when no tools are present. `auto` is the default if tools are present.
     * 
     * @param toolChoice the tool choice (String or ChatNamedToolChoice)
     */
    public void setToolChoice(Object toolChoice) { 
        this.toolChoice = toolChoice; 
    }
    
    /**
     * Sets the tool choice as a string ("none", "auto", or "required").
     * This is a convenience method for simple tool choice configurations.
     * 
     * @param toolChoice the tool choice as string
     */
    public void setToolChoice(String toolChoice) {
        this.toolChoice = toolChoice;
    }
    
    /**
     * Sets the tool choice as a named tool choice.
     * This is a convenience method for forcing a specific tool.
     * 
     * @param toolChoice the named tool choice
     */
    public void setToolChoice(ChatNamedToolChoice toolChoice) {
        this.toolChoice = toolChoice;
    }
    
    /**
     * Gets whether parallel tool calls are enabled.
     * When enabled, the model can make multiple tool calls in parallel.
     * 
     * @return true if parallel tool calls are enabled, false otherwise, or null if not set
     */
    public Boolean getParallelToolCalls() { 
        return parallelToolCalls; 
    }
    
    /**
     * Sets whether parallel tool calls are enabled.
     * When enabled, the model can make multiple tool calls in parallel.
     * 
     * @param parallelToolCalls true to enable parallel tool calls, false to disable
     */
    public void setParallelToolCalls(Boolean parallelToolCalls) { 
        this.parallelToolCalls = parallelToolCalls; 
    }
}
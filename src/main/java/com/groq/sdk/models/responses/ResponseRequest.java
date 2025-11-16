package com.groq.sdk.models.responses;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groq.sdk.models.chat.ChatTool;
import com.groq.sdk.models.mcp.MCPToolDefinition;

/**
 * Represents a request for the Groq Response API. Supports reasoning, code
 * interpreter, browser search, and MCP tools with comprehensive configuration.
 * 
 * <p>
 * <strong>Example usage:</strong>
 * </p>
 * 
 * <pre>{@code
 * ResponseRequest request = new ResponseRequest("openai/gpt-oss-20b", "How are AI models trained?");
 * request.setReasoning(new ReasoningConfig("low"));
 * GroqResponse<Response> response = client.responses().create(request);
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.0.0
 * @see Response
 * @see ReasoningConfig
 */
public class ResponseRequest {
	private String model;
	private Object input; // Can be String or List<MessageInput>
	private ReasoningConfig reasoning;

	@JsonProperty("max_output_tokens")
	private Integer maxOutputTokens;

	@JsonProperty("tool_choice")
	private Object toolChoice;

	private List<Object> tools; // Can be List<ChatTool> or List<MCPToolDefinition>

	private Double temperature;

	@JsonProperty("top_p")
	private Double topP;

	private Boolean stream;
	private String truncation;
	private Map<String, Object> metadata;
	private String user;

	@JsonProperty("service_tier")
	private String serviceTier;

	private Boolean background;

	@JsonProperty("parallel_tool_calls")
	private Boolean parallelToolCalls;

	private Boolean store;

	@JsonProperty("top_logprobs")
	private Integer topLogprobs;

	@JsonProperty("max_tool_calls")
	private Integer maxToolCalls;

	/**
	 * Default constructor.
	 */
	public ResponseRequest() {
	}

	/**
	 * Constructs a new ResponseRequest with the specified model and input text.
	 * 
	 * @param model the model to use for responses
	 * @param input the input text
	 */
	public ResponseRequest(String model, String input) {
		this.model = model;
		this.input = input;
	}

	/**
	 * Constructs a new ResponseRequest with the specified model and input messages.
	 * 
	 * @param model the model to use for responses
	 * @param input the list of message inputs
	 */
	public ResponseRequest(String model, List<MessageInput> input) {
		this.model = model;
		this.input = input;
	}

	// Getters and Setters
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Object getInput() {
		return input;
	}

	public void setInput(Object input) {
		this.input = input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setInput(List<MessageInput> input) {
		this.input = input;
	}

	public ReasoningConfig getReasoning() {
		return reasoning;
	}

	public void setReasoning(ReasoningConfig reasoning) {
		this.reasoning = reasoning;
	}

	public Integer getMaxOutputTokens() {
		return maxOutputTokens;
	}

	public void setMaxOutputTokens(Integer maxOutputTokens) {
		this.maxOutputTokens = maxOutputTokens;
	}

	public Object getToolChoice() {
		return toolChoice;
	}

	public void setToolChoice(Object toolChoice) {
		this.toolChoice = toolChoice;
	}

	public void setToolChoice(String toolChoice) {
		this.toolChoice = toolChoice;
	}

	public List<Object> getTools() {
		return tools;
	}

	public void setTools(List<Object> tools) {
		this.tools = tools;
	}

	/**
	 * Sets the tools as ChatTool instances.
	 * 
	 * @param tools the list of ChatTool instances
	 */
	public void setChatTools(List<ChatTool> tools) {
		this.tools = tools != null ? new java.util.ArrayList<Object>(tools) : null;
	}

	/**
	 * Sets the tools as MCPToolDefinition instances.
	 * 
	 * @param tools the list of MCPToolDefinition instances
	 */
	public void setMCPTools(List<MCPToolDefinition> tools) {
		this.tools = tools != null ? new java.util.ArrayList<Object>(tools) : null;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getTopP() {
		return topP;
	}

	public void setTopP(Double topP) {
		this.topP = topP;
	}

	public Boolean getStream() {
		return stream;
	}

	public void setStream(Boolean stream) {
		this.stream = stream;
	}

	public String getTruncation() {
		return truncation;
	}

	public void setTruncation(String truncation) {
		this.truncation = truncation;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getServiceTier() {
		return serviceTier;
	}

	public void setServiceTier(String serviceTier) {
		this.serviceTier = serviceTier;
	}

	public Boolean getBackground() {
		return background;
	}

	public void setBackground(Boolean background) {
		this.background = background;
	}

	public Boolean getParallelToolCalls() {
		return parallelToolCalls;
	}

	public void setParallelToolCalls(Boolean parallelToolCalls) {
		this.parallelToolCalls = parallelToolCalls;
	}

	public Boolean getStore() {
		return store;
	}

	public void setStore(Boolean store) {
		this.store = store;
	}

	public Integer getTopLogprobs() {
		return topLogprobs;
	}

	public void setTopLogprobs(Integer topLogprobs) {
		this.topLogprobs = topLogprobs;
	}

	public Integer getMaxToolCalls() {
		return maxToolCalls;
	}

	public void setMaxToolCalls(Integer maxToolCalls) {
		this.maxToolCalls = maxToolCalls;
	}
}
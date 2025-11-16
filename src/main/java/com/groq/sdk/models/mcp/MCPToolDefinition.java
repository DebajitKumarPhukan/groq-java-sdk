package com.groq.sdk.models.mcp;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an MCP tool definition in the request. Used within the Response
 * API tools array.
 * 
 * <p>
 * <strong>Example usage:</strong>
 * </p>
 * 
 * <pre>{@code
 * MCPToolDefinition stripeTool = new MCPToolDefinition("Stripe", "Payment processing and invoicing",
 * 		"https://mcp.stripe.com", "never");
 * stripeTool.setHeaders(Map.of("Authorization", "Bearer <STRIPE_TOKEN>"));
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 1.1.0
 * @see com.groq.sdk.models.responses.ResponseRequest
 */
public class MCPToolDefinition {
	private String type = "mcp";

	@JsonProperty("server_label")
	private String serverLabel;

	@JsonProperty("server_description")
	private String serverDescription;

	@JsonProperty("server_url")
	private String serverUrl;

	@JsonProperty("require_approval")
	private String requireApproval;

	@JsonProperty("headers")
	private Map<String, String> headers;

	/**
	 * Default constructor.
	 */
	public MCPToolDefinition() {
	}

	/**
	 * Constructs a new MCPToolDefinition with server details.
	 * 
	 * @param serverLabel       the server label
	 * @param serverDescription the server description
	 * @param serverUrl         the server URL
	 * @param requireApproval   the approval requirement
	 */
	public MCPToolDefinition(String serverLabel, String serverDescription, String serverUrl, String requireApproval) {
		this.serverLabel = serverLabel;
		this.serverDescription = serverDescription;
		this.serverUrl = serverUrl;
		this.requireApproval = requireApproval;
	}

	/**
	 * Constructs a new MCPToolDefinition with server details and headers.
	 * 
	 * @param serverLabel       the server label
	 * @param serverDescription the server description
	 * @param serverUrl         the server URL
	 * @param requireApproval   the approval requirement
	 * @param headers           the HTTP headers for authentication
	 */
	public MCPToolDefinition(String serverLabel, String serverDescription, String serverUrl, String requireApproval,
			Map<String, String> headers) {
		this.serverLabel = serverLabel;
		this.serverDescription = serverDescription;
		this.serverUrl = serverUrl;
		this.requireApproval = requireApproval;
		this.headers = headers;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("server_label")
	public String getServerLabel() {
		return serverLabel;
	}

	@JsonProperty("server_label")
	public void setServerLabel(String serverLabel) {
		this.serverLabel = serverLabel;
	}

	@JsonProperty("server_description")
	public String getServerDescription() {
		return serverDescription;
	}

	@JsonProperty("server_description")
	public void setServerDescription(String serverDescription) {
		this.serverDescription = serverDescription;
	}

	@JsonProperty("server_url")
	public String getServerUrl() {
		return serverUrl;
	}

	@JsonProperty("server_url")
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	@JsonProperty("require_approval")
	public String getRequireApproval() {
		return requireApproval;
	}

	@JsonProperty("require_approval")
	public void setRequireApproval(String requireApproval) {
		this.requireApproval = requireApproval;
	}

	/**
	 * Gets the HTTP headers for MCP server authentication.
	 * 
	 * @return the headers map
	 */
	@JsonProperty("headers")
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Sets the HTTP headers for MCP server authentication.
	 * 
	 * @param headers the headers map
	 */
	@JsonProperty("headers")
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * Adds a single header to the headers map.
	 * 
	 * @param key   the header name
	 * @param value the header value
	 */
	public void addHeader(String key, String value) {
		if (this.headers == null) {
			this.headers = new java.util.HashMap<>();
		}
		this.headers.put(key, value);
	}

	/**
	 * Sets the Authorization header with a Bearer token.
	 * 
	 * @param token the bearer token
	 */
	public void setBearerToken(String token) {
		addHeader("Authorization", "Bearer " + token);
	}

	/**
	 * Sets the Authorization header with a Basic auth token.
	 * 
	 * @param credentials the base64 encoded credentials
	 */
	public void setBasicAuth(String credentials) {
		addHeader("Authorization", "Basic " + credentials);
	}

	/**
	 * Sets the Authorization header with an API key.
	 * 
	 * @param apiKey the API key
	 */
	public void setApiKey(String apiKey) {
		addHeader("Authorization", apiKey);
	}

	/**
	 * Sets the Content-Type header.
	 * 
	 * @param contentType the content type (e.g., "application/json")
	 */
	public void setContentType(String contentType) {
		addHeader("Content-Type", contentType);
	}

	/**
	 * Returns a string representation of the MCP tool definition.
	 * 
	 * @return a string containing the tool definition details
	 */
	@Override
	public String toString() {
		return "MCPToolDefinition{" + "type='" + type + '\'' + ", serverLabel='" + serverLabel + '\''
				+ ", serverDescription='" + serverDescription + '\'' + ", serverUrl='" + serverUrl + '\''
				+ ", requireApproval='" + requireApproval + '\'' + ", headers=" + (headers != null ? headers : "null")
				+ '}';
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj the reference object with which to compare
	 * @return true if this object is the same as the obj argument; false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		MCPToolDefinition that = (MCPToolDefinition) obj;

		if (type != null ? !type.equals(that.type) : that.type != null)
			return false;
		if (serverLabel != null ? !serverLabel.equals(that.serverLabel) : that.serverLabel != null)
			return false;
		if (serverDescription != null ? !serverDescription.equals(that.serverDescription)
				: that.serverDescription != null)
			return false;
		if (serverUrl != null ? !serverUrl.equals(that.serverUrl) : that.serverUrl != null)
			return false;
		if (requireApproval != null ? !requireApproval.equals(that.requireApproval) : that.requireApproval != null)
			return false;
		return headers != null ? headers.equals(that.headers) : that.headers == null;
	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (serverLabel != null ? serverLabel.hashCode() : 0);
		result = 31 * result + (serverDescription != null ? serverDescription.hashCode() : 0);
		result = 31 * result + (serverUrl != null ? serverUrl.hashCode() : 0);
		result = 31 * result + (requireApproval != null ? requireApproval.hashCode() : 0);
		result = 31 * result + (headers != null ? headers.hashCode() : 0);
		return result;
	}
}
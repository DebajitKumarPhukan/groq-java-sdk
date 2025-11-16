package com.groq.sdk.resources;

import java.util.Arrays;
import java.util.List;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.mcp.MCPToolDefinition;
import com.groq.sdk.models.responses.MessageInput;
import com.groq.sdk.models.responses.Response;
import com.groq.sdk.models.responses.ResponseRequest;

/**
 * Provides access to MCP (Model Context Protocol) operations using the Response API.
 * Handles creating MCP responses with tool integration using the unified Response API.
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * MCPResource mcp = client.mcp();
 * MCPToolDefinition tool = new MCPToolDefinition("firecrawl", "Web scraping", "https://mcp.firecrawl.dev/v2/mcp", "never");
 * GroqResponse<Response> response = mcp.createResponse("openai/gpt-oss-120b", "What are production models?", tool);
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 * @version 2.0.0
 * @see GroqClient
 * @see ResponseRequest
 * @see Response
 */
public class MCPResource {
    private final GroqClient client;
    
    /**
     * Constructs a new MCPResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public MCPResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Creates an MCP response with the specified parameters.
     * 
     * @param model the model to use for responses
     * @param message the user message content
     * @param mcpTool the MCP tool definition
     * @return the API response containing MCP results
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Response> createResponse(String model, String message, MCPToolDefinition mcpTool) {
        try {
            MessageInput input = new MessageInput("user", message);
            ResponseRequest request = new ResponseRequest(model, Arrays.asList(input));
            request.setTools(Arrays.asList(mcpTool));
            request.setStream(false);
            
            return client.responses().create(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MCP response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates an MCP response with multiple tools.
     * 
     * @param model the model to use for responses
     * @param message the user message content
     * @param mcpTools the list of MCP tool definitions
     * @return the API response containing MCP results
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Response> createResponse(String model, String message, List<MCPToolDefinition> mcpTools) {
        try {
            MessageInput input = new MessageInput("user", message);
            ResponseRequest request = new ResponseRequest(model, Arrays.asList(input));
            request.setTools(Arrays.asList(mcpTools.toArray(new MCPToolDefinition[0])));
            request.setStream(false);
            
            return client.responses().create(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MCP response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates an MCP response with custom request configuration.
     * 
     * @param request the configured ResponseRequest with MCP tools
     * @return the API response containing MCP results
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Response> createResponse(ResponseRequest request) {
        try {
            return client.responses().create(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MCP response: " + e.getMessage(), e);
        }
    }
}
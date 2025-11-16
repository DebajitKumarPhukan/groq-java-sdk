package com.groq.sdk.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.mcp.MCPToolDefinition;
import com.groq.sdk.models.responses.MessageInput;
import com.groq.sdk.models.responses.Response;
import com.groq.sdk.models.responses.ResponseRequest;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
*
* @author Debajit Kumar Phukan
* @since 16-Nov-2025
*
*/
class MCPResourceTest {
    private MockWebServer mockWebServer;
    private MCPResource mcpResource;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        GroqClient client = GroqClient.builder()
                .apiKey("test-api-key")
                .baseUrl(mockWebServer.url("/").toString())
                .timeout(Duration.ofSeconds(5))
                .build();

        mcpResource = new MCPResource(client);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testCreateResponseWithSingleTool() {
        String responseBody = """
                {
                    "id": "resp_123",
                    "object": "response",
                    "model": "openai/gpt-oss-120b",
                    "output": [
                        {
                            "type": "mcp_call",
                            "server_label": "firecrawl",
                            "name": "scrape_url",
                            "arguments": "{\\"url\\":\\"https://example.com\\"}",
                            "output": "Scraped content"
                        }
                    ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        MCPToolDefinition tool = new MCPToolDefinition(
                "firecrawl", 
                "Web scraping", 
                "https://mcp.firecrawl.dev/v2/mcp", 
                "never"
        );

        GroqResponse<Response> response = mcpResource.createResponse(
                "openai/gpt-oss-120b", 
                "What are production models?", 
                tool
        );

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("resp_123", response.getData().getId());
        assertEquals("response", response.getData().getObject());
    }

    @Test
    void testCreateResponseWithMultipleTools() {
        String responseBody = """
                {
                    "id": "resp_456",
                    "object": "response",
                    "model": "openai/gpt-oss-120b",
                    "output": []
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        MCPToolDefinition tool1 = new MCPToolDefinition(
                "firecrawl", 
                "Web scraping", 
                "https://mcp.firecrawl.dev/v2/mcp", 
                "never"
        );

        MCPToolDefinition tool2 = new MCPToolDefinition(
                "stripe", 
                "Payment processing", 
                "https://mcp.stripe.com", 
                "always"
        );

        List<MCPToolDefinition> tools = Arrays.asList(tool1, tool2);

        GroqResponse<Response> response = mcpResource.createResponse(
                "openai/gpt-oss-120b", 
                "Process payment and scrape website", 
                tools
        );

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("resp_456", response.getData().getId());
    }

    @Test
    void testCreateResponseWithCustomRequest() {
        String responseBody = """
                {
                    "id": "resp_789",
                    "object": "response",
                    "model": "openai/gpt-oss-120b",
                    "output": [
                        {
                            "type": "reasoning",
                            "content": [
                                {
                                    "type": "text",
                                    "text": "Reasoning about the request"
                                }
                            ]
                        }
                    ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        MessageInput input = new MessageInput("user", "Analyze this data");
        ResponseRequest request = new ResponseRequest("openai/gpt-oss-120b", Arrays.asList(input));
        request.setStream(false);

        MCPToolDefinition tool = new MCPToolDefinition(
                "analytics", 
                "Data analysis", 
                "https://mcp.analytics.com", 
                "never"
        );
        request.setTools(Arrays.asList(tool));

        GroqResponse<Response> response = mcpResource.createResponse(request);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("resp_789", response.getData().getId());
    }

    @Test
    void testCreateResponseWithError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        MCPToolDefinition tool = new MCPToolDefinition(
                "firecrawl", 
                "Web scraping", 
                "https://mcp.firecrawl.dev/v2/mcp", 
                "never"
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mcpResource.createResponse("openai/gpt-oss-120b", "Test message", tool);
        });

        assertTrue(exception.getMessage().contains("Failed to create MCP response"));
    }
}
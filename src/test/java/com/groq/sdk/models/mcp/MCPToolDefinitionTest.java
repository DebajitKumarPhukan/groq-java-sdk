package com.groq.sdk.models.mcp;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025 
 *
 */
class MCPToolDefinitionTest {

    @Test
    void testDefaultConstructor() {
        MCPToolDefinition tool = new MCPToolDefinition();
        
        assertEquals("mcp", tool.getType());
        assertNull(tool.getServerLabel());
        assertNull(tool.getServerDescription());
        assertNull(tool.getServerUrl());
        assertNull(tool.getRequireApproval());
        assertNull(tool.getHeaders());
    }

    @Test
    void testConstructorWithServerDetails() {
        MCPToolDefinition tool = new MCPToolDefinition(
            "firecrawl", 
            "Web scraping", 
            "https://mcp.firecrawl.dev/v2/mcp", 
            "never"
        );

        assertEquals("mcp", tool.getType());
        assertEquals("firecrawl", tool.getServerLabel());
        assertEquals("Web scraping", tool.getServerDescription());
        assertEquals("https://mcp.firecrawl.dev/v2/mcp", tool.getServerUrl());
        assertEquals("never", tool.getRequireApproval());
        assertNull(tool.getHeaders());
    }

    @Test
    void testConstructorWithHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token123");
        
        MCPToolDefinition tool = new MCPToolDefinition(
            "stripe", 
            "Payment processing", 
            "https://mcp.stripe.com", 
            "always",
            headers
        );

        assertEquals("stripe", tool.getServerLabel());
        assertEquals("Payment processing", tool.getServerDescription());
        assertEquals("https://mcp.stripe.com", tool.getServerUrl());
        assertEquals("always", tool.getRequireApproval());
        assertEquals(headers, tool.getHeaders());
    }

    @Test
    void testSettersAndGetters() {
        MCPToolDefinition tool = new MCPToolDefinition();
        
        tool.setType("custom_type");
        tool.setServerLabel("custom_label");
        tool.setServerDescription("custom_description");
        tool.setServerUrl("https://custom.url");
        tool.setRequireApproval("sometimes");
        
        Map<String, String> headers = new HashMap<>();
        headers.put("X-API-Key", "test-key");
        tool.setHeaders(headers);

        assertEquals("custom_type", tool.getType());
        assertEquals("custom_label", tool.getServerLabel());
        assertEquals("custom_description", tool.getServerDescription());
        assertEquals("https://custom.url", tool.getServerUrl());
        assertEquals("sometimes", tool.getRequireApproval());
        assertEquals(headers, tool.getHeaders());
    }

    @Test
    void testAddHeader() {
        MCPToolDefinition tool = new MCPToolDefinition();
        
        tool.addHeader("Authorization", "Bearer token123");
        tool.addHeader("Content-Type", "application/json");

        assertNotNull(tool.getHeaders());
        assertEquals(2, tool.getHeaders().size());
        assertEquals("Bearer token123", tool.getHeaders().get("Authorization"));
        assertEquals("application/json", tool.getHeaders().get("Content-Type"));
    }

    @Test
    void testSetBearerToken() {
        MCPToolDefinition tool = new MCPToolDefinition();
        
        tool.setBearerToken("test-token-123");

        assertNotNull(tool.getHeaders());
        assertEquals("Bearer test-token-123", tool.getHeaders().get("Authorization"));
    }

    @Test
    void testSetBasicAuth() {
        MCPToolDefinition tool = new MCPToolDefinition();
        
        tool.setBasicAuth("dGVzdDp0b2tlbg==");

        assertNotNull(tool.getHeaders());
        assertEquals("Basic dGVzdDp0b2tlbg==", tool.getHeaders().get("Authorization"));
    }

    @Test
    void testSetApiKey() {
        MCPToolDefinition tool = new MCPToolDefinition();
        
        tool.setApiKey("api-key-123");

        assertNotNull(tool.getHeaders());
        assertEquals("api-key-123", tool.getHeaders().get("Authorization"));
    }

    @Test
    void testSetContentType() {
        MCPToolDefinition tool = new MCPToolDefinition();
        
        tool.setContentType("application/json");

        assertNotNull(tool.getHeaders());
        assertEquals("application/json", tool.getHeaders().get("Content-Type"));
    }

    @Test
    void testToString() {
        MCPToolDefinition tool = new MCPToolDefinition("test", "description", "https://test.com", "never");
        
        String toString = tool.toString();
        
        assertTrue(toString.contains("test"));
        assertTrue(toString.contains("description"));
        assertTrue(toString.contains("https://test.com"));
        assertTrue(toString.contains("never"));
    }

    @Test
    void testEqualsAndHashCode() {
        MCPToolDefinition tool1 = new MCPToolDefinition("test", "description", "https://test.com", "never");
        MCPToolDefinition tool2 = new MCPToolDefinition("test", "description", "https://test.com", "never");
        MCPToolDefinition tool3 = new MCPToolDefinition("different", "description", "https://test.com", "never");

        assertEquals(tool1, tool2);
        assertNotEquals(tool1, tool3);
        assertEquals(tool1.hashCode(), tool2.hashCode());
        assertNotEquals(tool1.hashCode(), tool3.hashCode());
    }

    @Test
    void testEqualsWithNull() {
        MCPToolDefinition tool = new MCPToolDefinition("test", "description", "https://test.com", "never");
        
        assertNotEquals(null, tool);
    }

    @Test
    void testEqualsWithDifferentClass() {
        MCPToolDefinition tool = new MCPToolDefinition("test", "description", "https://test.com", "never");
        
        assertNotEquals("string", tool);
    }
}
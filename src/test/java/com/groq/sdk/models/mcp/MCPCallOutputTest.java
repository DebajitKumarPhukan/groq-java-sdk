package com.groq.sdk.models.mcp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 *
 */
class MCPCallOutputTest {

    @Test
    void testDefaultConstructor() {
        MCPCallOutput output = new MCPCallOutput();
        
        assertEquals("mcp_call", output.getType());
        assertNull(output.getServerLabel());
        assertNull(output.getName());
        assertNull(output.getArguments());
        assertNull(output.getOutput());
    }

    @Test
    void testSettersAndGetters() {
        MCPCallOutput output = new MCPCallOutput();
        
        output.setServerLabel("firecrawl");
        output.setName("scrape_url");
        output.setArguments("{\"url\":\"https://example.com\"}");
        output.setOutput("Scraped content");

        assertEquals("firecrawl", output.getServerLabel());
        assertEquals("scrape_url", output.getName());
        assertEquals("{\"url\":\"https://example.com\"}", output.getArguments());
        assertEquals("Scraped content", output.getOutput());
    }
}
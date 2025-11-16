package com.groq.sdk.models.mcp;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025 
 *
 */
class MCPListToolsOutputTest {

    @Test
    void testDefaultConstructor() {
        MCPListToolsOutput output = new MCPListToolsOutput();
        
        assertEquals("mcp_list_tools", output.getType());
        assertNull(output.getServerLabel());
        assertNull(output.getTools());
    }

    @Test
    void testSettersAndGetters() {
        MCPListToolsOutput output = new MCPListToolsOutput();
        
        output.setServerLabel("firecrawl");
        
        MCPTool tool1 = new MCPTool("scrape_url", "Scrape URL content");
        MCPTool tool2 = new MCPTool("search_web", "Search the web");
        List<MCPTool> tools = Arrays.asList(tool1, tool2);
        output.setTools(tools);

        assertEquals("firecrawl", output.getServerLabel());
        assertEquals(2, output.getTools().size());
        assertEquals("scrape_url", output.getTools().get(0).getName());
        assertEquals("search_web", output.getTools().get(1).getName());
    }
}
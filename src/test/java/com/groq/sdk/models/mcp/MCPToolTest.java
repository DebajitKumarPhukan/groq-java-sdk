package com.groq.sdk.models.mcp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 16-Nov-2025
 *
 */
class MCPToolTest {

    @Test
    void testDefaultConstructor() {
        MCPTool tool = new MCPTool();
        
        assertNull(tool.getName());
        assertNull(tool.getDescription());
    }

    @Test
    void testConstructorWithNameAndDescription() {
        MCPTool tool = new MCPTool("scrape_url", "Scrape URL content");
        
        assertEquals("scrape_url", tool.getName());
        assertEquals("Scrape URL content", tool.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        MCPTool tool = new MCPTool();
        
        tool.setName("search_web");
        tool.setDescription("Search the web for information");

        assertEquals("search_web", tool.getName());
        assertEquals("Search the web for information", tool.getDescription());
    }

    @Test
    void testToString() {
        MCPTool tool = new MCPTool("test_tool", "Test description");
        
        String toString = tool.toString();
        
        assertTrue(toString.contains("test_tool"));
        assertTrue(toString.contains("Test description"));
    }

    @Test
    void testEqualsAndHashCode() {
        MCPTool tool1 = new MCPTool("test_tool", "Test description");
        MCPTool tool2 = new MCPTool("test_tool", "Test description");
        MCPTool tool3 = new MCPTool("different_tool", "Test description");

        assertEquals(tool1, tool2);
        assertNotEquals(tool1, tool3);
        assertEquals(tool1.hashCode(), tool2.hashCode());
        assertNotEquals(tool1.hashCode(), tool3.hashCode());
    }

    @Test
    void testEqualsWithNull() {
        MCPTool tool = new MCPTool("test_tool", "Test description");
        
        assertNotEquals(null, tool);
    }

    @Test
    void testEqualsWithDifferentClass() {
        MCPTool tool = new MCPTool("test_tool", "Test description");
        
        assertNotEquals("string", tool);
    }
}
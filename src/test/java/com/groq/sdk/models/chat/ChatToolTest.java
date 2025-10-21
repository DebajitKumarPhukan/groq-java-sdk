package com.groq.sdk.models.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Debajit Kumar Phukan
 * @version 1.0.0
 */
class ChatToolTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should create ChatTool with constructor")
    void testConstructor() {
        String type = "function";
        FunctionDefinition function = new FunctionDefinition("test_function");
        ChatTool tool = new ChatTool(type, function);
        assertEquals(type, tool.getType());
        assertEquals(function, tool.getFunction());
    }

    @Test
    @DisplayName("Should set and get properties")
    void testSettersAndGetters() {
        ChatTool tool = new ChatTool();
        String type = "browser_search";
        FunctionDefinition function = new FunctionDefinition("search_function");
        tool.setType(type);
        tool.setFunction(function);
        assertEquals(type, tool.getType());
        assertEquals(function, tool.getFunction());
    }

    @Test
    @DisplayName("Should handle null function")
    void testNullFunction() {
        ChatTool tool = new ChatTool();
        tool.setType("function");
        tool.setFunction(null);
        assertNull(tool.getFunction());
    }

    @Test
    @DisplayName("Should serialize to JSON correctly")
    void testSerialization() throws Exception {
        ChatTool tool = new ChatTool("function", 
            new FunctionDefinition("test_function", "Test description", null));
        
        String json = objectMapper.writeValueAsString(tool);
        assertTrue(json.contains("\"type\":\"function\""));
        assertTrue(json.contains("\"function\""));
        assertTrue(json.contains("\"name\":\"test_function\""));
    }

    @Test
    @DisplayName("Should deserialize from JSON correctly")
    void testDeserialization() throws Exception {
        // Given
        String json = """
            {
                "type": "function",
                "function": {
                    "name": "test_function",
                    "description": "Test function"
                }
            }
            """;
        
        ChatTool tool = objectMapper.readValue(json, ChatTool.class);        
        assertEquals("function", tool.getType());
        assertNotNull(tool.getFunction());
        assertEquals("test_function", tool.getFunction().getName());
        assertEquals("Test function", tool.getFunction().getDescription());
    }

    @Test
    @DisplayName("Should support different tool types")
    void testDifferentToolTypes() {
        // Given
        String[] toolTypes = {"function", "browser_search", "code_interpreter"};
        
        for (String type : toolTypes) {
            ChatTool tool = new ChatTool(type, new FunctionDefinition("test"));
            assertEquals(type, tool.getType());
        }
    }
}
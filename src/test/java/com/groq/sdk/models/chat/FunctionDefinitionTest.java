package com.groq.sdk.models.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Debajit Kumar Phukan
 * @version 1.0.0
 */
class FunctionDefinitionTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should create FunctionDefinition with name constructor")
    void testNameConstructor() {
        String name = "test_function";
        FunctionDefinition function = new FunctionDefinition(name);
        assertEquals(name, function.getName());
        assertNull(function.getDescription());
        assertNull(function.getParameters());
    }

    @Test
    @DisplayName("Should create FunctionDefinition with all parameters")
    void testFullConstructor() {
        String name = "test_function";
        String description = "Test function description";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");        
        FunctionDefinition function = new FunctionDefinition(name, description, parameters);
        assertEquals(name, function.getName());
        assertEquals(description, function.getDescription());
        assertEquals(parameters, function.getParameters());
    }

    @Test
    @DisplayName("Should set and get properties")
    void testSettersAndGetters() {
        FunctionDefinition function = new FunctionDefinition();
        String name = "test_function";
        String description = "Test description";
        Map<String, Object> parameters = Map.of("type", "object");
        function.setName(name);
        function.setDescription(description);
        function.setParameters(parameters);
        assertEquals(name, function.getName());
        assertEquals(description, function.getDescription());
        assertEquals(parameters, function.getParameters());
    }

    @Test
    @DisplayName("Should handle null parameters")
    void testNullParameters() {
        FunctionDefinition function = new FunctionDefinition("test", "desc", null);        
        assertNull(function.getParameters());
    }

    @Test
    @DisplayName("Should serialize to JSON correctly")
    void testSerialization() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        parameters.put("properties", Map.of("param1", Map.of("type", "string")));
        
        FunctionDefinition function = new FunctionDefinition(
            "test_function", 
            "Test description", 
            parameters
        );
        
        String json = objectMapper.writeValueAsString(function);
        
        assertTrue(json.contains("\"name\":\"test_function\""));
        assertTrue(json.contains("\"description\":\"Test description\""));
        assertTrue(json.contains("\"parameters\""));
        assertTrue(json.contains("\"type\":\"object\""));
    }

    @Test
    @DisplayName("Should deserialize from JSON correctly")
    void testDeserialization() throws Exception {
        String json = """
            {
                "name": "test_function",
                "description": "Test function description",
                "parameters": {
                    "type": "object",
                    "properties": {
                        "param1": {"type": "string"}
                    }
                }
            }
            """;
        
        FunctionDefinition function = objectMapper.readValue(json, FunctionDefinition.class);
        
        assertEquals("test_function", function.getName());
        assertEquals("Test function description", function.getDescription());
        assertNotNull(function.getParameters());
        assertEquals("object", ((Map<?, ?>) function.getParameters()).get("type"));
    }

    @Test
    @DisplayName("Should handle complex parameter schemas")
    void testComplexParameters() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", Map.of("type", "string"));
        properties.put("age", Map.of("type", "integer", "minimum", 0));
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        parameters.put("properties", properties);
        parameters.put("required", java.util.List.of("name"));
        
        FunctionDefinition function = new FunctionDefinition("user_function", "User function", parameters);
        
        assertNotNull(function.getParameters());
        Map<?, ?> params = (Map<?, ?>) function.getParameters();
        assertEquals("object", params.get("type"));
        assertTrue(params.containsKey("properties"));
        assertTrue(params.containsKey("required"));
    }
}
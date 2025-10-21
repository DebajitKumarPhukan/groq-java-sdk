package com.groq.sdk.models.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Debajit Kumar Phukan
 * @version 1.0.0
 */
class ChatNamedToolChoiceTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("Should create ChatNamedToolChoice with function name")
	void testConstructorWithFunctionName() {
		String functionName = "test_function";
		ChatNamedToolChoice toolChoice = new ChatNamedToolChoice(functionName);
		assertEquals("function", toolChoice.getType());
		assertNotNull(toolChoice.getFunction());
		assertEquals(functionName, toolChoice.getFunction().getName());
	}

	@Test
	@DisplayName("Should set and get properties")
	void testSettersAndGetters() {
		ChatNamedToolChoice toolChoice = new ChatNamedToolChoice();
		String type = "function";
		ChatNamedToolChoice.NamedToolFunction function = new ChatNamedToolChoice.NamedToolFunction("test_function");
		toolChoice.setType(type);
		toolChoice.setFunction(function);
		assertEquals(type, toolChoice.getType());
		assertEquals(function, toolChoice.getFunction());
		assertEquals("test_function", toolChoice.getFunction().getName());
	}

	@Test
	@DisplayName("Should handle NamedToolFunction setters and getters")
	void testNamedToolFunction() {
		ChatNamedToolChoice.NamedToolFunction function = new ChatNamedToolChoice.NamedToolFunction();
		String functionName = "test_function";
		function.setName(functionName);
		assertEquals(functionName, function.getName());
	}

	@Test
	@DisplayName("Should serialize to JSON correctly")
	void testSerialization() throws Exception {
		ChatNamedToolChoice toolChoice = new ChatNamedToolChoice("test_function");
		String json = objectMapper.writeValueAsString(toolChoice);
		assertTrue(json.contains("\"type\":\"function\""));
		assertTrue(json.contains("\"function\""));
		assertTrue(json.contains("\"name\":\"test_function\""));
	}

	@Test
	@DisplayName("Should deserialize from JSON correctly")
	void testDeserialization() throws Exception {
		String json = """
				{
				    "type": "function",
				    "function": {
				        "name": "test_function"
				    }
				}
				""";
		ChatNamedToolChoice toolChoice = objectMapper.readValue(json, ChatNamedToolChoice.class);
		assertEquals("function", toolChoice.getType());
		assertNotNull(toolChoice.getFunction());
		assertEquals("test_function", toolChoice.getFunction().getName());
	}

	@Test
	@DisplayName("Should create with NamedToolFunction constructor")
	void testNamedToolFunctionConstructor() {
		String functionName = "test_function";
		ChatNamedToolChoice.NamedToolFunction function = new ChatNamedToolChoice.NamedToolFunction(functionName);
		assertEquals(functionName, function.getName());
	}

	@Test
	@DisplayName("Should handle different function names")
	void testDifferentFunctionNames() {
		String[] functionNames = { "get_weather", "calculate", "get_current_time" };
		for (String functionName : functionNames) {
			ChatNamedToolChoice toolChoice = new ChatNamedToolChoice(functionName);
			assertEquals(functionName, toolChoice.getFunction().getName());
		}
	}
}
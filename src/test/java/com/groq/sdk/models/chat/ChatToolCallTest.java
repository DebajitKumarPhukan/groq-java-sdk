package com.groq.sdk.models.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Debajit Kumar Phukan
 * @version 1.0.0
 */
class ChatToolCallTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("Should set and get properties")
	void testSettersAndGetters() {
		ChatToolCall toolCall = new ChatToolCall();
		String id = "call_123";
		String type = "function";
		ChatToolCall.ToolCallFunction function = new ChatToolCall.ToolCallFunction();
		function.setName("test_function");
		function.setArguments("{\"param\":\"value\"}");

		toolCall.setId(id);
		toolCall.setType(type);
		toolCall.setFunction(function);

		assertEquals(id, toolCall.getId());
		assertEquals(type, toolCall.getType());
		assertEquals(function, toolCall.getFunction());
		assertEquals("test_function", toolCall.getFunction().getName());
		assertEquals("{\"param\":\"value\"}", toolCall.getFunction().getArguments());
	}

	@Test
	@DisplayName("Should handle ToolCallFunction setters and getters")
	void testToolCallFunction() {
		ChatToolCall.ToolCallFunction function = new ChatToolCall.ToolCallFunction();
		String name = "test_function";
		String arguments = "{\"param1\":\"value1\", \"param2\":123}";

		function.setName(name);
		function.setArguments(arguments);

		assertEquals(name, function.getName());
		assertEquals(arguments, function.getArguments());
	}

	@Test
	@DisplayName("Should serialize to JSON correctly")
	void testSerialization() throws Exception {
		ChatToolCall toolCall = new ChatToolCall();
		toolCall.setId("call_123");
		toolCall.setType("function");

		ChatToolCall.ToolCallFunction function = new ChatToolCall.ToolCallFunction();
		function.setName("test_function");
		function.setArguments("{\"param\":\"value\"}");
		toolCall.setFunction(function);

		String json = objectMapper.writeValueAsString(toolCall);
		assertTrue(json.contains("\"id\":\"call_123\""));
		assertTrue(json.contains("\"type\":\"function\""));
		assertTrue(json.contains("\"function\""));
		assertTrue(json.contains("\"name\":\"test_function\""));
		assertTrue(json.contains("\"arguments\":\"{\\\"param\\\":\\\"value\\\"}\""));
	}

	@Test
	@DisplayName("Should deserialize from JSON correctly")
	void testDeserialization() throws Exception {
		String json = """
				{
				    "id": "call_123",
				    "type": "function",
				    "function": {
				        "name": "test_function",
				        "arguments": "{\\"param\\":\\"value\\"}"
				    }
				}
				""";

		ChatToolCall toolCall = objectMapper.readValue(json, ChatToolCall.class);

		assertEquals("call_123", toolCall.getId());
		assertEquals("function", toolCall.getType());
		assertNotNull(toolCall.getFunction());
		assertEquals("test_function", toolCall.getFunction().getName());
		assertEquals("{\"param\":\"value\"}", toolCall.getFunction().getArguments());
	}

	@Test
	@DisplayName("Should handle complex function arguments")
	void testComplexFunctionArguments() {
		ChatToolCall toolCall = new ChatToolCall();
		ChatToolCall.ToolCallFunction function = new ChatToolCall.ToolCallFunction();
		String complexArguments = """
				{
				    "location": "San Francisco, CA",
				    "unit": "celsius",
				    "include_forecast": true
				}
				""";

		function.setName("get_weather");
		function.setArguments(complexArguments);
		toolCall.setFunction(function);

		assertEquals("get_weather", toolCall.getFunction().getName());
		assertEquals(complexArguments, toolCall.getFunction().getArguments());
	}

	@Test
	@DisplayName("Should handle multiple tool calls with different IDs")
	void testMultipleToolCalls() {
		String[] ids = { "call_1", "call_2", "call_3" };

		for (String id : ids) {
			ChatToolCall toolCall = new ChatToolCall();
			toolCall.setId(id);
			toolCall.setType("function");

			ChatToolCall.ToolCallFunction function = new ChatToolCall.ToolCallFunction();
			function.setName("test_function");
			function.setArguments("{}");
			toolCall.setFunction(function);

			assertEquals(id, toolCall.getId());
			assertEquals("function", toolCall.getType());
			assertEquals("test_function", toolCall.getFunction().getName());
		}
	}
}
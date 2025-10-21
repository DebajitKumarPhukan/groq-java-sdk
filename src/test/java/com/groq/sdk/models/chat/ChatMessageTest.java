package com.groq.sdk.models.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 10-Oct-2025
 *
 */
class ChatMessageTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testDefaultConstructor() {

		ChatMessage message = new ChatMessage();

		assertNull(message.getRole());
		assertNull(message.getContent());
		assertNull(message.getName());
	}

	@Test
	void testParameterizedConstructor() {

		ChatMessage message = new ChatMessage("assistant", "Hello, how can I help you?");

		assertEquals("assistant", message.getRole());
		assertEquals("Hello, how can I help you?", message.getContent());
		assertNull(message.getName());
	}

	@Test
	void testSetters() {

		ChatMessage message = new ChatMessage();

		message.setRole("system");
		message.setContent("You are a helpful assistant");
		message.setName("system_bot");

		assertEquals("system", message.getRole());
		assertEquals("You are a helpful assistant", message.getContent());
		assertEquals("system_bot", message.getName());
	}

	@ParameterizedTest
	@ValueSource(strings = { "user", "assistant", "system" })
	void testValidRoles(String role) {

		ChatMessage message = new ChatMessage(role, "test content");

		assertEquals(role, message.getRole());
	}

	@Test
	void testCreateToolMessage() {

		String toolCallId = "call_123";
		String content = "Tool execution result";

		ChatMessage toolMessage = ChatMessage.createToolMessage(toolCallId, content);

		assertEquals("tool", toolMessage.getRole());
		assertEquals(content, toolMessage.getContent());
		assertEquals(toolCallId, toolMessage.getToolCallId());
		assertNull(toolMessage.getToolCalls());
	}

	@Test
	void testToolCalls() {
		ChatMessage message = new ChatMessage();
		message.setRole("assistant");

		ChatToolCall toolCall1 = new ChatToolCall();
		toolCall1.setId("call_1");

		ChatToolCall toolCall2 = new ChatToolCall();
		toolCall2.setId("call_2");

		List<ChatToolCall> toolCalls = Arrays.asList(toolCall1, toolCall2);

		message.setToolCalls(toolCalls);

		assertNotNull(message.getToolCalls());
		assertEquals(2, message.getToolCalls().size());
		assertEquals("call_1", message.getToolCalls().get(0).getId());
		assertEquals("call_2", message.getToolCalls().get(1).getId());
	}

	@Test
	void testToolCallId() {

		ChatMessage message = new ChatMessage();
		message.setRole("tool");
		String toolCallId = "call_123";

		message.setToolCallId(toolCallId);

		assertEquals(toolCallId, message.getToolCallId());
	}

	@Test
	void testSerializeAssistantWithToolCalls() throws Exception {

		ChatMessage message = new ChatMessage();
		message.setRole("assistant");
		message.setContent(null);

		ChatToolCall toolCall = new ChatToolCall();
		toolCall.setId("call_123");
		toolCall.setType("function");

		ChatToolCall.ToolCallFunction function = new ChatToolCall.ToolCallFunction();
		function.setName("test_function");
		function.setArguments("{\"param\":\"value\"}");
		toolCall.setFunction(function);

		message.setToolCalls(Arrays.asList(toolCall));

		String json = objectMapper.writeValueAsString(message);

		assertTrue(json.contains("\"role\":\"assistant\""));
		assertTrue(json.contains("\"tool_calls\""));
		assertTrue(json.contains("\"id\":\"call_123\""));
		assertTrue(json.contains("\"name\":\"test_function\""));
	}

	@Test
	void testSerializeToolMessage() throws Exception {
		ChatMessage message = ChatMessage.createToolMessage("call_123", "Tool result");

		String json = objectMapper.writeValueAsString(message);

		assertTrue(json.contains("\"role\":\"tool\""));
		assertTrue(json.contains("\"content\":\"Tool result\""));
		assertTrue(json.contains("\"tool_call_id\":\"call_123\""));
	}

	@Test
	void testDeserializeAssistantWithToolCalls() throws Exception {

		String json = """
				{
				    "role": "assistant",
				    "content": null,
				    "tool_calls": [
				        {
				            "id": "call_123",
				            "type": "function",
				            "function": {
				                "name": "test_function",
				                "arguments": "{\\"param\\":\\"value\\"}"
				            }
				        }
				    ]
				}
				""";

		ChatMessage message = objectMapper.readValue(json, ChatMessage.class);

		assertEquals("assistant", message.getRole());
		assertNull(message.getContent());
		assertNotNull(message.getToolCalls());
		assertEquals(1, message.getToolCalls().size());
		assertEquals("call_123", message.getToolCalls().get(0).getId());
		assertEquals("test_function", message.getToolCalls().get(0).getFunction().getName());
	}

	@Test
	void testDeserializeToolMessage() throws Exception {
		String json = """
				{
				    "role": "tool",
				    "content": "Tool execution result",
				    "tool_call_id": "call_123"
				}
				""";

		ChatMessage message = objectMapper.readValue(json, ChatMessage.class);

		assertEquals("tool", message.getRole());
		assertEquals("Tool execution result", message.getContent());
		assertEquals("call_123", message.getToolCallId());
	}

	@Test
	void testMixedMessageTypes() {
		ChatMessage userMessage = new ChatMessage("user", "Hello");
		assertEquals("user", userMessage.getRole());
		assertEquals("Hello", userMessage.getContent());

		ChatMessage assistantMessage = new ChatMessage("assistant", "Hello there!");
		assertEquals("assistant", assistantMessage.getRole());
		assertEquals("Hello there!", assistantMessage.getContent());

		ChatMessage toolMessage = ChatMessage.createToolMessage("call_1", "42");
		assertEquals("tool", toolMessage.getRole());
		assertEquals("42", toolMessage.getContent());
		assertEquals("call_1", toolMessage.getToolCallId());
	}
}
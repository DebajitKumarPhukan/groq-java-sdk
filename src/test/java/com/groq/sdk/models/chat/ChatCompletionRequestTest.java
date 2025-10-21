package com.groq.sdk.models.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 10-Oct-2025
 *
 */
class ChatCompletionRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testConstructorAndGetters() {

		ChatMessage message = new ChatMessage("user", "Hello");
		List<ChatMessage> messages = Arrays.asList(message);

		ChatCompletionRequest request = new ChatCompletionRequest("test-model", messages);

		assertEquals("test-model", request.getModel());
		assertEquals(1, request.getMessages().size());
		assertEquals("user", request.getMessages().get(0).getRole());
		assertEquals("Hello", request.getMessages().get(0).getContent());
	}

	@Test
	void testSetters() {

		ChatCompletionRequest request = new ChatCompletionRequest();
		ChatMessage message = new ChatMessage("system", "You are a helpful assistant");

		request.setModel("openai/gpt-oss-20b");
		request.setMessages(Arrays.asList(message));
		request.setMaxTokens(100);
		request.setTemperature(0.7);
		request.setTopP(0.9);
		request.setStream(false);
		request.setStop("\n");
		request.setFrequencyPenalty(0.5);
		request.setPresencePenalty(0.5);

		assertEquals("openai/gpt-oss-20b", request.getModel());
		assertEquals(1, request.getMessages().size());
		assertEquals(100, request.getMaxTokens());
		assertEquals(0.7, request.getTemperature());
		assertEquals(0.9, request.getTopP());
		assertFalse(request.getStream());
		assertEquals("\n", request.getStop());
		assertEquals(0.5, request.getFrequencyPenalty());
		assertEquals(0.5, request.getPresencePenalty());
	}

	@Test
	void testJsonSerialization() throws JsonProcessingException {

		ChatCompletionRequest request = new ChatCompletionRequest();
		request.setModel("test-model");
		request.setMessages(Arrays.asList(new ChatMessage("user", "Hello")));
		request.setMaxTokens(50);

		String json = objectMapper.writeValueAsString(request);

		assertTrue(json.contains("test-model"));
		assertTrue(json.contains("user"));
		assertTrue(json.contains("Hello"));
		assertTrue(json.contains("max_tokens"));
	}

	@Test
	void testJsonDeserialization() throws JsonProcessingException {

		String json = """
				{
				    "model": "test-model",
				    "messages": [
				        {
				            "role": "user",
				            "content": "Hello World"
				        }
				    ],
				    "max_tokens": 100,
				    "temperature": 0.7
				}
				""";

		ChatCompletionRequest request = objectMapper.readValue(json, ChatCompletionRequest.class);

		assertEquals("test-model", request.getModel());
		assertEquals(1, request.getMessages().size());
		assertEquals("user", request.getMessages().get(0).getRole());
		assertEquals("Hello World", request.getMessages().get(0).getContent());
		assertEquals(100, request.getMaxTokens());
		assertEquals(0.7, request.getTemperature());
	}

	@Test
	void testRequestWithTools() {

		String model = "test-model";
		List<ChatMessage> messages = Arrays.asList(new ChatMessage("user", "Hello"));

		ChatTool tool = new ChatTool("function", new FunctionDefinition("test_function"));

		ChatCompletionRequest request = new ChatCompletionRequest(model, messages);

		request.setTools(Arrays.asList(tool));
		request.setToolChoice("auto");
		request.setParallelToolCalls(true);

		assertEquals(model, request.getModel());
		assertEquals(messages, request.getMessages());
		assertNotNull(request.getTools());
		assertEquals(1, request.getTools().size());
		assertEquals("auto", request.getToolChoice());
		assertTrue(request.getParallelToolCalls());
	}

	@Test
	void testSetToolChoiceAsString() {

		ChatCompletionRequest request = new ChatCompletionRequest();
		String[] toolChoices = { "none", "auto", "required" };

		for (String toolChoice : toolChoices) {

			request.setToolChoice(toolChoice);

			assertEquals(toolChoice, request.getToolChoice());
		}
	}

	@Test
	void testSetToolChoiceAsNamedToolChoice() {

		ChatCompletionRequest request = new ChatCompletionRequest();
		ChatNamedToolChoice namedChoice = new ChatNamedToolChoice("specific_function");

		request.setToolChoice(namedChoice);

		assertEquals(namedChoice, request.getToolChoice());
	}

	@Test
	void testSerializationWithTools() throws Exception {

		ChatCompletionRequest request = new ChatCompletionRequest();
		request.setModel("test-model");
		request.setMessages(Arrays.asList(new ChatMessage("user", "Hello")));

		ChatTool tool = new ChatTool("function", new FunctionDefinition("test_function", "Test function", null));
		request.setTools(Arrays.asList(tool));
		request.setToolChoice("auto");
		request.setParallelToolCalls(true);

		String json = objectMapper.writeValueAsString(request);

		assertTrue(json.contains("\"model\":\"test-model\""));
		assertTrue(json.contains("\"tools\""));
		assertTrue(json.contains("\"tool_choice\":\"auto\""));
		assertTrue(json.contains("\"parallel_tool_calls\":true"));
		assertTrue(json.contains("\"name\":\"test_function\""));
	}

	@Test
	void testSerializationWithNamedToolChoice() throws Exception {

		ChatCompletionRequest request = new ChatCompletionRequest();
		request.setModel("test-model");
		request.setMessages(Arrays.asList(new ChatMessage("user", "Hello")));

		ChatNamedToolChoice namedChoice = new ChatNamedToolChoice("specific_function");
		request.setToolChoice(namedChoice);

		String json = objectMapper.writeValueAsString(request);

		assertTrue(json.contains("\"tool_choice\""));
		assertTrue(json.contains("\"type\":\"function\""));
		assertTrue(json.contains("\"name\":\"specific_function\""));
	}

	@Test
	void testDeserializationWithTools() throws Exception {

		String json = """
				{
				    "model": "test-model",
				    "messages": [
				        {"role": "user", "content": "Hello"}
				    ],
				    "tools": [
				        {
				            "type": "function",
				            "function": {
				                "name": "test_function",
				                "description": "Test function"
				            }
				        }
				    ],
				    "tool_choice": "auto",
				    "parallel_tool_calls": true
				}
				""";

		ChatCompletionRequest request = objectMapper.readValue(json, ChatCompletionRequest.class);

		assertEquals("test-model", request.getModel());
		assertNotNull(request.getMessages());
		assertEquals(1, request.getMessages().size());
		assertNotNull(request.getTools());
		assertEquals(1, request.getTools().size());
		assertEquals("auto", request.getToolChoice());
		assertTrue(request.getParallelToolCalls());
	}

	@Test
	void testMultipleTools() {

		ChatCompletionRequest request = new ChatCompletionRequest();

		ChatTool tool1 = new ChatTool("function", new FunctionDefinition("function1"));
		ChatTool tool2 = new ChatTool("function", new FunctionDefinition("function2"));
		ChatTool tool3 = new ChatTool("browser_search", new ChatTool("browser_search", null).getFunction()); // This
																												// would
																												// need
																												// proper
																												// setup

		List<ChatTool> tools = Arrays.asList(tool1, tool2, tool3);

		request.setTools(tools);

		assertNotNull(request.getTools());
		assertEquals(3, request.getTools().size());
	}

	@Test
	void testBackwardCompatibility() {

		ChatCompletionRequest request = new ChatCompletionRequest("test-model",
				Arrays.asList(new ChatMessage("user", "Hello")));

		request.setMaxTokens(100);
		request.setTemperature(0.7);
		request.setTopP(0.9);
		request.setStream(false);

		assertEquals(100, request.getMaxTokens());
		assertEquals(0.7, request.getTemperature(), 0.001);
		assertEquals(0.9, request.getTopP(), 0.001);
		assertFalse(request.getStream());

		assertNull(request.getTools());
		assertNull(request.getToolChoice());
		assertNull(request.getParallelToolCalls());
	}

	@Test
	void testNullToolChoice() {

		ChatCompletionRequest request = new ChatCompletionRequest();

		request.setToolChoice((String) null);
		request.setToolChoice((ChatNamedToolChoice) null);

		assertNull(request.getToolChoice());
	}
}
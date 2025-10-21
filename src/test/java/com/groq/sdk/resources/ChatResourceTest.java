package com.groq.sdk.resources;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.chat.ChatCompletion;
import com.groq.sdk.models.chat.ChatCompletionRequest;
import com.groq.sdk.models.chat.ChatMessage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 10-Oct-2025
 *
 */
class ChatResourceTest {
	private MockWebServer mockWebServer;
	private ChatResource chatResource;

	@BeforeEach
	void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		GroqClient client = GroqClient.builder().apiKey("test-api-key").baseUrl(mockWebServer.url("/").toString())
				.timeout(Duration.ofSeconds(5)).build();

		chatResource = new ChatResource(client);
	}

	@AfterEach
	void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void testCreateCompletionSuccess() {

		String responseBody = """
				{
				    "id": "chatcmpl-123",
				    "object": "chat.completion",
				    "created": 1677652288,
				    "model": "openai/gpt-oss-20b",
				    "choices": [{
				        "index": 0,
				        "message": {
				            "role": "assistant",
				            "content": "Hello there!"
				        },
				        "finish_reason": "stop"
				    }],
				    "usage": {
				        "prompt_tokens": 9,
				        "completion_tokens": 12,
				        "total_tokens": 21
				    }
				}
				""";

		mockWebServer.enqueue(new MockResponse().setBody(responseBody).setResponseCode(200).addHeader("Content-Type",
				"application/json"));

		ChatMessage message = new ChatMessage("user", "Hello");
		ChatCompletionRequest request = new ChatCompletionRequest("openai/gpt-oss-20b", Arrays.asList(message));

		GroqResponse<ChatCompletion> response = chatResource.createCompletion(request);

		assertTrue(response.isSuccessful());
		assertEquals(200, response.getStatusCode());
		assertNotNull(response.getData());
		assertEquals("chatcmpl-123", response.getData().getId());
		assertEquals("chat.completion", response.getData().getObject());
		assertEquals(1, response.getData().getChoices().size());
		assertEquals("assistant", response.getData().getChoices().get(0).getMessage().getRole());
		assertEquals("Hello there!", response.getData().getChoices().get(0).getMessage().getContent());
	}

	@Test
	void testCreateCompletionWithNullRequest() {
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			chatResource.createCompletion(null);
		});

		assertEquals("Failed to create chat completion: ChatCompletionRequest cannot be null", exception.getMessage());
	}

	@Test
	void testCreateCompletionWithNullModel() {
		ChatCompletionRequest request = new ChatCompletionRequest();
		request.setMessages(Arrays.asList(new ChatMessage("user", "Hello")));
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			chatResource.createCompletion(request);
		});

		assertEquals("Failed to create chat completion: Model cannot be null or empty", exception.getMessage());
	}

	@Test
	void testCreateCompletionWithNullMessages() {
		ChatCompletionRequest request = new ChatCompletionRequest();
		request.setModel("openai/gpt-oss-20b");
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			chatResource.createCompletion(request);
		});

		assertEquals("Failed to create chat completion: Messages cannot be null or empty", exception.getMessage());
	}

	@Test
	void testCreateSimpleCompletion() {
		String responseBody = """
				{
				    "id": "chatcmpl-123",
				    "object": "chat.completion",
				    "choices": [{
				        "message": {
				            "role": "assistant",
				            "content": "Simple response"
				        }
				    }]
				}
				""";

		mockWebServer.enqueue(new MockResponse().setBody(responseBody).setResponseCode(200));

		GroqResponse<ChatCompletion> response = chatResource.createCompletion("openai/gpt-oss-20b", "Hello");

		assertTrue(response.isSuccessful());
		assertEquals("Simple response", response.getData().getChoices().get(0).getMessage().getContent());
	}
}
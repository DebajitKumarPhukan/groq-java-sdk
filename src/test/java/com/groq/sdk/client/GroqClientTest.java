package com.groq.sdk.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.models.ModelList;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 10-Oct-2025
 *
 */
class GroqClientTest {
	private MockWebServer mockWebServer;
	private GroqClient client;

	@BeforeEach
	void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		client = GroqClient.builder().apiKey("test-api-key").baseUrl(mockWebServer.url("/").toString())
				.timeout(Duration.ofSeconds(5)).maxRetries(2).build();
	}

	@AfterEach
	void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void testBuilderWithValidApiKey() {
		GroqClient client = GroqClient.builder().apiKey("valid-key").build();

		assertNotNull(client);
	}

	@Test
	void testBuilderWithNullApiKey() {
		assertThrows(IllegalStateException.class, () -> {
			GroqClient.builder().build();
		});
	}

	@Test
	void testSuccessfulApiCall() throws InterruptedException {
		String responseBody = """
				{
				    "object": "list",
				    "data": []
				}
				""";
		mockWebServer.enqueue(new MockResponse().setBody(responseBody).setResponseCode(200).addHeader("Content-Type",
				"application/json"));

		GroqResponse<ModelList> response = client.models().list();

		assertTrue(response.isSuccessful());
		assertEquals(200, response.getStatusCode());
		assertNotNull(response.getData());

		// Verify request
		RecordedRequest request = mockWebServer.takeRequest();
		assertEquals("GET", request.getMethod());
		assertEquals("//openai/v1/models", request.getPath());
		assertEquals("Bearer test-api-key", request.getHeader("Authorization"));
	}

	@Test
	void testApiCallWithError() {

		mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

		assertThrows(RuntimeException.class, () -> {
			client.models().list();
		});
	}

	@Test
	void testResourceAccess() {
		assertNotNull(client.chat());
		assertNotNull(client.models());
		assertNotNull(client.embeddings());
		assertNotNull(client.audio());
		assertNotNull(client.batches());
		assertNotNull(client.files());
	}
}
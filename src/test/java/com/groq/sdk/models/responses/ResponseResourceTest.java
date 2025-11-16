package com.groq.sdk.models.responses;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.responses.MessageInput;
import com.groq.sdk.models.responses.Response;
import com.groq.sdk.models.responses.ResponseRequest;
import com.groq.sdk.resources.ResponseResource;

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
 * @since 16-Nov-2025 
 *
 */
class ResponseResourceTest {
    private MockWebServer mockWebServer;
    private ResponseResource responseResource;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        GroqClient client = GroqClient.builder()
                .apiKey("test-api-key")
                .baseUrl(mockWebServer.url("/").toString())
                .timeout(Duration.ofSeconds(5))
                .build();

        responseResource = new ResponseResource(client);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testCreateResponseSuccess() {
        String responseBody = """
                {
                    "id": "resp_123",
                    "object": "response",
                    "model": "openai/gpt-oss-20b",
                    "output": [
                        {
                            "type": "message",
                            "content": [
                                {
                                    "type": "text",
                                    "text": "Hello! How can I help you today?"
                                }
                            ]
                        }
                    ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        MessageInput input = new MessageInput("user", "Hello");
        ResponseRequest request = new ResponseRequest("openai/gpt-oss-20b", Arrays.asList(input));

        GroqResponse<Response> response = responseResource.create(request);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("resp_123", response.getData().getId());
        assertEquals("response", response.getData().getObject());
        assertEquals("openai/gpt-oss-20b", response.getData().getModel());
    }

    @Test
    void testCreateSimpleResponse() {
        String responseBody = """
                {
                    "id": "resp_456",
                    "object": "response",
                    "model": "openai/gpt-oss-20b",
                    "output": [
                        {
                            "type": "message",
                            "content": [
                                {
                                    "type": "text", 
                                    "text": "Simple response"
                                }
                            ]
                        }
                    ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        GroqResponse<Response> response = responseResource.create("openai/gpt-oss-20b", "Simple input");

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("resp_456", response.getData().getId());
    }

    @Test
    void testCreateResponseWithNullRequest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseResource.create(null);
        });

        assertEquals("Failed to create response: ResponseRequest cannot be null", exception.getMessage());
    }

    @Test
    void testCreateResponseWithNullModel() {
        ResponseRequest request = new ResponseRequest();
        request.setInput(Arrays.asList(new MessageInput("user", "Hello")));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseResource.create(request);
        });

        assertEquals("Failed to create response: Model cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateResponseWithNullInput() {
        ResponseRequest request = new ResponseRequest();
        request.setModel("openai/gpt-oss-20b");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseResource.create(request);
        });

        assertEquals("Failed to create response: Input cannot be null", exception.getMessage());
    }

    @Test
    void testCreateResponseWithError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("Bad Request"));

        MessageInput input = new MessageInput("user", "Test message");
        ResponseRequest request = new ResponseRequest("openai/gpt-oss-20b", Arrays.asList(input));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseResource.create(request);
        });

        assertTrue(exception.getMessage().contains("Failed to create response"));
    }
}
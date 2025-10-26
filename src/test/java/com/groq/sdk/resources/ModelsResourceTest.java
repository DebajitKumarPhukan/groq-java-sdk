package com.groq.sdk.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.models.ModelList;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Unit tests for ModelsResource class.
 * 
 * @author Debajit Kumar Phukan
 * @since 26-Oct-2025
 */
class ModelsResourceTest {
    private MockWebServer mockWebServer;
    private ModelsResource modelsResource;
    private GroqClient client;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        client = GroqClient.builder()
                .apiKey("test-api-key")
                .baseUrl(mockWebServer.url("/").toString())
                .timeout(Duration.ofSeconds(5))
                .build();

        modelsResource = new ModelsResource(client);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testListSuccess() throws InterruptedException {
        String responseBody = """
                {
                    "object": "list",
                    "data": [
                        {
                            "id": "model1",
                            "object": "model",
                            "created": 1677652288,
                            "owned_by": "organization-1"
                        },
                        {
                            "id": "model2",
                            "object": "model",
                            "created": 1677652290,
                            "owned_by": "organization-2"
                        }
                    ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        GroqResponse<ModelList> response = modelsResource.list();

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("list", response.getData().getObject());
        assertNotNull(response.getData().getData());
        assertEquals(2, response.getData().getData().size());
        assertEquals("model1", response.getData().getData().get(0).getId());
        assertEquals("model2", response.getData().getData().get(1).getId());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("//openai/v1/models", recordedRequest.getPath());
        assertEquals("Bearer test-api-key", recordedRequest.getHeader("Authorization"));
    }

    @Test
    void testListWithApiError() {
        String responseBody = """
                {
                    "error": {
                        "message": "API error occurred",
                        "type": "api_error",
                        "code": 500
                    }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(500)
                .addHeader("Content-Type", "application/json"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            modelsResource.list();
        });

        assertTrue(exception.getMessage().contains("Failed to list models"));
    }

    @Test
    void testRetrieveSuccess() throws InterruptedException {
        String modelId = "model1";
        String responseBody = """
                {
                    "id": "model1",
                    "object": "model",
                    "created": 1677652288,
                    "owned_by": "organization-1"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        GroqResponse<com.groq.sdk.models.models.Model> response = modelsResource.retrieve(modelId);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("model1", response.getData().getId());
        assertEquals("model", response.getData().getObject());
        assertEquals(1677652288, response.getData().getCreated());
        assertEquals("organization-1", response.getData().getOwnedBy());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("//openai/v1/models/model1", recordedRequest.getPath());
        assertEquals("Bearer test-api-key", recordedRequest.getHeader("Authorization"));
    }

    @Test
    void testRetrieveWithNullModelId() {
    	RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            modelsResource.retrieve(null);
        });

        assertEquals("Failed to retrieve model: Model ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testRetrieveWithEmptyModelId() {
    	RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            modelsResource.retrieve("");
        });

        assertEquals("Failed to retrieve model: Model ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testRetrieveWithApiError() {
        String modelId = "nonexistent-model";
        String responseBody = """
                {
                    "error": {
                        "message": "Model not found",
                        "type": "invalid_request_error",
                        "code": 404
                    }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            modelsResource.retrieve(modelId);
        });

        assertTrue(exception.getMessage().contains("Failed to retrieve model"));
    }

    @Test
    void testModelsResourceConstructor() {
        ModelsResource resource = new ModelsResource(client);
        assertNotNull(resource);
    }
}
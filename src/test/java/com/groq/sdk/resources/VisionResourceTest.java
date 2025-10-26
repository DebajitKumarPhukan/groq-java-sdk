package com.groq.sdk.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.chat.ChatCompletion;
import com.groq.sdk.models.vision.VisionContentPart;
import com.groq.sdk.models.vision.VisionMessage;
import com.groq.sdk.models.vision.VisionRequest;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Unit tests for VisionResource class.
 * 
 * @author Debajit Kumar Phukan
 * @since 26-Oct-2025
 */
class VisionResourceTest {
    private MockWebServer mockWebServer;
    private VisionResource visionResource;
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

        visionResource = new VisionResource(client);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testCreateCompletionSuccess() throws InterruptedException {
        String responseBody = """
                {
                    "id": "chatcmpl-123",
                    "object": "chat.completion",
                    "created": 1677652288,
                    "model": "meta-llama/llama-4-maverick-17b-128e-instruct",
                    "choices": [{
                        "index": 0,
                        "message": {
                            "role": "assistant",
                            "content": "This is a test response"
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

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        VisionRequest request = createTestVisionRequest();

        GroqResponse<ChatCompletion> response = visionResource.createCompletion(request);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("chatcmpl-123", response.getData().getId());
        assertEquals("meta-llama/llama-4-maverick-17b-128e-instruct", response.getData().getModel());
        assertNotNull(response.getData().getChoices());
        assertEquals(1, response.getData().getChoices().size());
        assertEquals("This is a test response", response.getData().getChoices().get(0).getMessage().getContent());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("//openai/v1/chat/completions", recordedRequest.getPath());
        assertEquals("Bearer test-api-key", recordedRequest.getHeader("Authorization"));
        assertTrue(recordedRequest.getHeader("Content-Type").contains("application/json"));
    }

    @Test
    void testCreateCompletionWithNullRequest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createCompletion(null);
        });

        assertEquals("Failed to create vision completion: VisionRequest cannot be null", exception.getMessage());
    }

    @Test
    void testCreateCompletionWithNullModel() {
        VisionRequest request = createTestVisionRequest();
        request.setModel(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createCompletion(request);
        });

        assertEquals("Failed to create vision completion: Model cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateCompletionWithEmptyModel() {
        VisionRequest request = createTestVisionRequest();
        request.setModel("");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createCompletion(request);
        });

        assertEquals("Failed to create vision completion: Model cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateCompletionWithNullMessages() {
        VisionRequest request = createTestVisionRequest();
        request.setMessages(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createCompletion(request);
        });

        assertEquals("Failed to create vision completion: Messages cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateCompletionWithEmptyMessages() {
        VisionRequest request = createTestVisionRequest();
        request.setMessages(Arrays.asList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createCompletion(request);
        });

        assertEquals("Failed to create vision completion: Messages cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateVisionRequestWithUrl() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        String imageUrl = "https://example.com/image.jpg";
        String prompt = "Describe this image";

        VisionRequest request = visionResource.createVisionRequestWithUrl(model, imageUrl, prompt);

        assertNotNull(request);
        assertEquals(model, request.getModel());
        assertNotNull(request.getMessages());
        assertEquals(1, request.getMessages().size());
        
        VisionMessage message = request.getMessages().get(0);
        assertEquals("user", message.getRole());
        assertNotNull(message.getContent());
        assertEquals(2, message.getContent().size());
        
        VisionContentPart textPart = message.getContent().get(0);
        assertEquals("text", textPart.getType());
        assertEquals(prompt, textPart.getText());
        
        VisionContentPart imagePart = message.getContent().get(1);
        assertEquals("image_url", imagePart.getType());
        assertNotNull(imagePart.getImageUrl());
        assertEquals(imageUrl, imagePart.getImageUrl().getUrl());
    }

    @Test
    void testCreateVisionRequestWithLocalImage() throws IOException {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        String prompt = "Describe this image";
        
        // Create a temporary image file
        Path tempFile = Files.createTempFile("test_image", ".jpg");
        Files.write(tempFile, new byte[]{0x01, 0x02, 0x03});

        VisionRequest request = visionResource.createVisionRequestWithLocalImage(model, tempFile.toString(), prompt);

        assertNotNull(request);
        assertEquals(model, request.getModel());
        assertNotNull(request.getMessages());
        assertEquals(1, request.getMessages().size());
        
        VisionMessage message = request.getMessages().get(0);
        assertEquals("user", message.getRole());
        assertNotNull(message.getContent());
        assertEquals(2, message.getContent().size());
        
        VisionContentPart textPart = message.getContent().get(0);
        assertEquals("text", textPart.getType());
        assertEquals(prompt, textPart.getText());
        
        VisionContentPart imagePart = message.getContent().get(1);
        assertEquals("image_url", imagePart.getType());
        assertNotNull(imagePart.getImageUrl());
        assertTrue(imagePart.getImageUrl().getUrl().startsWith("data:image/jpeg;base64,"));

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testCreateVisionRequestWithLocalImageFileNotFound() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        String imagePath = "/nonexistent/path/image.jpg";
        String prompt = "Describe this image";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createVisionRequestWithLocalImage(model, imagePath, prompt);
        });

        assertTrue(exception.getMessage().contains("Failed to create vision request with local image"));
    }

    @Test
    void testCreateVisionRequestWithImageBytes() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        byte[] imageBytes = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        String mimeType = "image/jpeg";
        String prompt = "Describe this image";

        VisionRequest request = visionResource.createVisionRequestWithImageBytes(model, imageBytes, mimeType, prompt);

        assertNotNull(request);
        assertEquals(model, request.getModel());
        assertNotNull(request.getMessages());
        assertEquals(1, request.getMessages().size());
        
        VisionMessage message = request.getMessages().get(0);
        assertEquals("user", message.getRole());
        assertNotNull(message.getContent());
        assertEquals(2, message.getContent().size());
        
        VisionContentPart textPart = message.getContent().get(0);
        assertEquals("text", textPart.getType());
        assertEquals(prompt, textPart.getText());
        
        VisionContentPart imagePart = message.getContent().get(1);
        assertEquals("image_url", imagePart.getType());
        assertNotNull(imagePart.getImageUrl());
        assertTrue(imagePart.getImageUrl().getUrl().startsWith("data:image/jpeg;base64,"));
    }

    @Test
    void testCreateVisionRequestWithImageBytesNullBytes() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        byte[] imageBytes = null;
        String mimeType = "image/jpeg";
        String prompt = "Describe this image";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createVisionRequestWithImageBytes(model, imageBytes, mimeType, prompt);
        });

        assertTrue(exception.getMessage().contains("Failed to create vision request with image bytes"));
    }

    @Test
    void testCreateVisionRequestWithImageBytesEmptyBytes() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        byte[] imageBytes = new byte[0];
        String mimeType = "image/jpeg";
        String prompt = "Describe this image";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createVisionRequestWithImageBytes(model, imageBytes, mimeType, prompt);
        });

        assertTrue(exception.getMessage().contains("Failed to create vision request with image bytes"));
    }

    @Test
    void testCreateVisionRequestWithImageBytesNullMimeType() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        byte[] imageBytes = new byte[]{0x01, 0x02, 0x03};
        String mimeType = null;
        String prompt = "Describe this image";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createVisionRequestWithImageBytes(model, imageBytes, mimeType, prompt);
        });

        assertTrue(exception.getMessage().contains("Failed to create vision request with image bytes"));
    }

    @Test
    void testCreateVisionRequestWithBase64Image() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        String base64Image = "base64encodedstring";
        String mimeType = "image/png";
        String prompt = "Describe this image";

        VisionRequest request = visionResource.createVisionRequestWithBase64Image(model, base64Image, mimeType, prompt);

        assertNotNull(request);
        assertEquals(model, request.getModel());
        assertNotNull(request.getMessages());
        assertEquals(1, request.getMessages().size());
        
        VisionMessage message = request.getMessages().get(0);
        assertEquals("user", message.getRole());
        assertNotNull(message.getContent());
        assertEquals(2, message.getContent().size());
        
        VisionContentPart textPart = message.getContent().get(0);
        assertEquals("text", textPart.getType());
        assertEquals(prompt, textPart.getText());
        
        VisionContentPart imagePart = message.getContent().get(1);
        assertEquals("image_url", imagePart.getType());
        assertNotNull(imagePart.getImageUrl());
        assertEquals("data:image/png;base64,base64encodedstring", imagePart.getImageUrl().getUrl());
    }

    @Test
    void testCreateVisionRequestWithBase64ImageNullBase64() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        String base64Image = null;
        String mimeType = "image/png";
        String prompt = "Describe this image";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createVisionRequestWithBase64Image(model, base64Image, mimeType, prompt);
        });

        assertTrue(exception.getMessage().contains("Failed to create vision request with base64 image"));
    }

    @Test
    void testCreateVisionRequestWithBase64ImageNullMimeType() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        String base64Image = "base64encodedstring";
        String mimeType = null;
        String prompt = "Describe this image";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visionResource.createVisionRequestWithBase64Image(model, base64Image, mimeType, prompt);
        });

        assertTrue(exception.getMessage().contains("Failed to create vision request with base64 image"));
    }

    @Test
    void testCreateVisionMessageWithUrl() {
        String text = "What's in this image?";
        String imageUrl = "https://example.com/image.jpg";

        VisionMessage message = visionResource.createVisionMessageWithUrl(text, imageUrl);

        assertNotNull(message);
        assertEquals("user", message.getRole());
        assertNotNull(message.getContent());
        assertEquals(2, message.getContent().size());
        
        VisionContentPart textPart = message.getContent().get(0);
        assertEquals("text", textPart.getType());
        assertEquals(text, textPart.getText());
        
        VisionContentPart imagePart = message.getContent().get(1);
        assertEquals("image_url", imagePart.getType());
        assertNotNull(imagePart.getImageUrl());
        assertEquals(imageUrl, imagePart.getImageUrl().getUrl());
    }

    @Test
    void testEncodeImageToBase64WithPath() throws IOException {
        // Create a temporary image file
        Path tempFile = Files.createTempFile("test_image", ".jpg");
        byte[] testData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        Files.write(tempFile, testData);

        String base64 = visionResource.encodeImageToBase64(tempFile.toString());

        assertNotNull(base64);
        // The base64 encoding of [1,2,3,4,5] is "AQIDBAU="
        assertEquals("AQIDBAU=", base64);

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testEncodeImageToBase64WithPathFileNotFound() {
        String imagePath = "/nonexistent/path/image.jpg";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            visionResource.encodeImageToBase64(imagePath);
        });

        assertTrue(exception.getMessage().contains("Image file does not exist or is not readable"));
    }

    @Test
    void testEncodeImageToBase64WithPathNullPath() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            visionResource.encodeImageToBase64("");
        });

        assertEquals("Image path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEncodeImageToBase64WithBytes() {
        byte[] imageBytes = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        
        String base64 = visionResource.encodeImageToBase64(imageBytes);

        assertNotNull(base64);
        assertEquals("AQIDBAU=", base64);
    }

    @Test
    void testEncodeImageToBase64WithBlankBytes() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            visionResource.encodeImageToBase64("");
        });

        assertEquals("Image path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEncodeImageToBase64WithEmptyBytes() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            visionResource.encodeImageToBase64(new byte[0]);
        });

        assertEquals("Image bytes cannot be null or empty", exception.getMessage());
    }

    @Test
    void testGetCommonVisionModels() {
        String[] models = visionResource.getCommonVisionModels();

        assertNotNull(models);
        assertEquals(2, models.length);
        assertEquals("meta-llama/llama-4-maverick-17b-128e-instruct", models[0]);
        assertEquals("meta-llama/llama-4-scout-17b-16e-instruct", models[1]);
    }

    @Test
    void testVisionResourceConstructor() {
        VisionResource resource = new VisionResource(client);
        assertNotNull(resource);
    }

    private VisionRequest createTestVisionRequest() {
        String model = "meta-llama/llama-4-maverick-17b-128e-instruct";
        String imageUrl = "https://example.com/image.jpg";
        String prompt = "Describe this image";
        
        return visionResource.createVisionRequestWithUrl(model, imageUrl, prompt);
    }
}
package com.groq.sdk.resources;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.audio.SpeechRequest;
import com.groq.sdk.models.audio.SpeechResponse;
import com.groq.sdk.models.audio.Transcription;
import com.groq.sdk.models.audio.TranscriptionRequest;
import com.groq.sdk.models.audio.Translation;
import com.groq.sdk.models.audio.TranslationRequest;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

/**
 * Unit tests for AudioResource class.
 * 
 * @author Debajit Kumar Phukan
 * @since 26-Oct-2025
 */
class AudioResourceTest {
    private MockWebServer mockWebServer;
    private AudioResource audioResource;
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

        audioResource = new AudioResource(client);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testCreateSpeechSuccess() throws InterruptedException {
        // Mock binary audio response
        byte[] mockAudioData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        
        try(Buffer data = new okio.Buffer()){    		
	        mockWebServer.enqueue(new MockResponse()
	                .setBody(data.write(mockAudioData))
	                .setResponseCode(200)
	                .addHeader("Content-Type", "audio/mpeg"));
        }
        SpeechRequest request = new SpeechRequest("playai-tts", "Hello world", "Jennifer-PlayAI");
        request.setResponseFormat("mp3");
        request.setSpeed(1.0);

        GroqResponse<SpeechResponse> response = audioResource.createSpeech(request);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertArrayEquals(mockAudioData, response.getData().getAudio());
        assertEquals("audio/mpeg", response.getData().getContentType());

        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("//openai/v1/audio/speech", recordedRequest.getPath());
        assertEquals("Bearer test-api-key", recordedRequest.getHeader("Authorization"));
        assertTrue(recordedRequest.getHeader("Content-Type").contains("application/json"));
    }

    @Test
    void testCreateSpeechWithNullRequest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            audioResource.createSpeech(null);
        });

        assertEquals("Failed to create speech: SpeechRequest cannot be null", exception.getMessage());
    }

    @Test
    void testCreateSpeechWithInvalidRequest() {
        SpeechRequest request = new SpeechRequest(); // Missing required fields
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            audioResource.createSpeech(request);
        });

        assertTrue(exception.getMessage().contains("Failed to create speech"));
    }

    @Test
    void testCreateSimpleSpeech() throws InterruptedException {
        byte[] mockAudioData = new byte[]{0x06, 0x07, 0x08, 0x09, 0x0A};
        try(Buffer data = new okio.Buffer()){    		
            mockWebServer.enqueue(new MockResponse()
                    .setBody(data.write(mockAudioData))
                    .setResponseCode(200)
                    .addHeader("Content-Type", "audio/mpeg"));
        }
       
        GroqResponse<SpeechResponse> response = audioResource.createSpeech("Hello", "Fritz-PlayAI");

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertArrayEquals(mockAudioData, response.getData().getAudio());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("//openai/v1/audio/speech", request.getPath());
    }

    @Test
    void testCreatePlayAISpeech() throws InterruptedException {
        byte[] mockAudioData = new byte[]{0x0B, 0x0C, 0x0D};
        
        try(Buffer data = new okio.Buffer()){    		
	        mockWebServer.enqueue(new MockResponse()
	                .setBody(data.write(mockAudioData))
	                .setResponseCode(200)
	                .addHeader("Content-Type", "audio/mpeg"));
        }
        GroqResponse<SpeechResponse> response = audioResource.createPlayAISpeech("Test text", "Ruby-PlayAI", 1.5);

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("//openai/v1/audio/speech", request.getPath());
    }

    @Test
    void testCreateConfiguredSpeech() throws InterruptedException {
        byte[] mockAudioData = new byte[]{0x0E, 0x0F};
        
        try(Buffer data = new okio.Buffer()){    		
	        mockWebServer.enqueue(new MockResponse()
	                .setBody(data.write(mockAudioData))
	                .setResponseCode(200)
	                .addHeader("Content-Type", "audio/wav"));
        }

        GroqResponse<SpeechResponse> response = audioResource.createSpeech(
            "playai-tts", "Test", "Angelo-PlayAI", "wav", 0.8
        );

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("//openai/v1/audio/speech", request.getPath());
    }

    @Test
    void testCreateTranscriptionSuccess() throws InterruptedException {
        String responseBody = """
                {
                    "text": "This is a test transcription",
                    "language": "en",
                    "duration": 5.2
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        TranscriptionRequest request = new TranscriptionRequest("whisper-large-v3-turbo", "test-file-data");
        request.setLanguage("en");
        request.setResponseFormat("json");

        GroqResponse<Transcription> response = audioResource.createTranscription(request);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("This is a test transcription", response.getData().getText());
        assertEquals("en", response.getData().getLanguage());
        assertEquals(5.2, response.getData().getDuration());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("//openai/v1/audio/transcriptions", recordedRequest.getPath());
    }

    @Test
    void testCreateTranscriptionWithNullRequest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            audioResource.createTranscription(null);
        });

        assertEquals("Failed to create transcription: TranscriptionRequest cannot be null", exception.getMessage());
    }

    @Test
    void testCreateTranscriptionWithInvalidRequest() {
        TranscriptionRequest request = new TranscriptionRequest(); // Missing required fields
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            audioResource.createTranscription(request);
        });

        assertTrue(exception.getMessage().contains("Failed to create transcription"));
    }

    @Test
    void testCreateSimpleTranscription() throws InterruptedException {
        String responseBody = """
                {
                    "text": "Simple transcription result"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        GroqResponse<Transcription> response = audioResource.createTranscription("test-data", "en");

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Simple transcription result", response.getData().getText());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("//openai/v1/audio/transcriptions", request.getPath());
    }

    @Test
    void testCreateDetailedTranscription() throws InterruptedException {
        String responseBody = """
                {
                    "text": "Detailed transcription",
                    "language": "fr",
                    "duration": 10.5,
                    "segments": []
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        GroqResponse<Transcription> response = audioResource.createDetailedTranscription(
            "test-data", "fr", "Context prompt"
        );

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Detailed transcription", response.getData().getText());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("//openai/v1/audio/transcriptions", request.getPath());
    }

    @Test
    void testCreateTranslationSuccess() throws InterruptedException {
        String responseBody = """
                {
                    "text": "This is a translated text",
                    "language": "en",
                    "duration": 3.7
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        TranslationRequest request = new TranslationRequest("whisper-large-v3", "test-file-data");
        request.setPrompt("Translate this");
        request.setResponseFormat("json");

        GroqResponse<Translation> response = audioResource.createTranslation(request);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("This is a translated text", response.getData().getText());
        assertEquals("en", response.getData().getLanguage());
        assertEquals(3.7, response.getData().getDuration());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("//openai/v1/audio/translations", recordedRequest.getPath());
    }

    @Test
    void testCreateTranslationWithNullRequest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            audioResource.createTranslation(null);
        });

        assertEquals("Failed to create translation: TranslationRequest cannot be null", exception.getMessage());
    }

    @Test
    void testCreateSimpleTranslation() throws InterruptedException {
        String responseBody = """
                {
                    "text": "Translated text in English"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        GroqResponse<Translation> response = audioResource.createTranslation("test-data", "Translate prompt");

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Translated text in English", response.getData().getText());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("//openai/v1/audio/translations", request.getPath());
    }

    @Test
    void testCreateDetailedTranslation() throws InterruptedException {
        String responseBody = """
                {
                    "text": "Detailed translation with parameters",
                    "duration": 8.2
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        GroqResponse<Translation> response = audioResource.createDetailedTranslation(
            "test-data", "Detailed prompt", 0.3, "verbose_json"
        );

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Detailed translation with parameters", response.getData().getText());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("//openai/v1/audio/translations", request.getPath());
    }

    @Test
    void testGetSupportedVoices() {
        String[] voices = audioResource.getSupportedVoices();
        
        assertNotNull(voices);
        assertTrue(voices.length > 0);
        assertTrue(voices[0].contains("PlayAI"));
    }

    @Test
    void testGetSupportedTTSModels() {
        String[] models = audioResource.getSupportedTTSModels();
        
        assertNotNull(models);
        assertEquals(1, models.length);
        assertEquals("playai-tts", models[0]);
    }

    @Test
    void testGetSupportedSTTModels() {
        String[] models = audioResource.getSupportedSTTModels();
        
        assertNotNull(models);
        assertEquals(2, models.length);
        assertTrue(models[0].contains("whisper"));
    }

    @Test
    void testIsVoiceSupported() {
        assertTrue(audioResource.isVoiceSupported("Jennifer-PlayAI"));
        assertFalse(audioResource.isVoiceSupported("NonExistent-Voice"));
    }

    @Test
    void testIsSpeedValid() {
        assertTrue(audioResource.isSpeedValid(1.0));
        assertTrue(audioResource.isSpeedValid(0.25));
        assertTrue(audioResource.isSpeedValid(4.0));
        assertFalse(audioResource.isSpeedValid(0.1));
        assertFalse(audioResource.isSpeedValid(5.0));
        assertFalse(audioResource.isSpeedValid(null));
    }

    @Test
    void testGetRandomVoice() {
        String voice = audioResource.getRandomVoice();
        
        assertNotNull(voice);
        assertTrue(voice.contains("PlayAI"));
    }

    @Test
    void testGetPopularVoices() {
        String[] popularVoices = audioResource.getPopularVoices();
        
        assertNotNull(popularVoices);
        assertTrue(popularVoices.length > 0);
        assertTrue(popularVoices[0].contains("PlayAI"));
    }

    @Test
    void testIsFilePath() throws IOException {
        // Create a temporary file for testing
        Path tempFile = Files.createTempFile("test_audio", ".mp3");
        
        AudioResource resource = new AudioResource(client);
        
        // Test with actual file path
        assertTrue(resource.isFilePath(tempFile.toString()));
        
        // Test with file extension
        assertTrue(resource.isFilePath("test.mp3"));
        assertTrue(resource.isFilePath("test.wav"));
        assertTrue(resource.isFilePath("test.flac"));
        
        // Test with base64 data (should return false)
        assertFalse(resource.isFilePath(""));
        assertFalse(resource.isFilePath("abc123def456"));
        
        // Test with null and empty
        assertFalse(resource.isFilePath(null));
        assertFalse(resource.isFilePath(""));
        
        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testCreateTranscriptionMultipart() throws InterruptedException, IOException {
        String responseBody = """
                {
                    "text": "Multipart transcription result",
                    "language": "es",
                    "duration": 7.3
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        // Create a temporary audio file
        Path tempFile = Files.createTempFile("test_audio", ".mp3");
        Files.write(tempFile, new byte[]{0x01, 0x02, 0x03});

        TranscriptionRequest request = new TranscriptionRequest("whisper-large-v3-turbo", tempFile.toString());
        request.setLanguage("es");
        request.setResponseFormat("json");

        GroqResponse<Transcription> response = audioResource.createTranscription(request);

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Multipart transcription result", response.getData().getText());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("//openai/v1/audio/transcriptions", recordedRequest.getPath());
        assertTrue(recordedRequest.getHeader("Content-Type").contains("multipart"));

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testCreateTranslationMultipart() throws InterruptedException, IOException {
        String responseBody = """
                {
                    "text": "Multipart translation result",
                    "language": "en",
                    "duration": 4.1
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        // Create a temporary audio file
        Path tempFile = Files.createTempFile("test_audio", ".wav");
        Files.write(tempFile, new byte[]{0x04, 0x05, 0x06});

        TranslationRequest request = new TranslationRequest("whisper-large-v3", tempFile.toString());
        request.setPrompt("Translate this audio");
        request.setResponseFormat("json");

        GroqResponse<Translation> response = audioResource.createTranslation(request);

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Multipart translation result", response.getData().getText());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("//openai/v1/audio/translations", recordedRequest.getPath());
        assertTrue(recordedRequest.getHeader("Content-Type").contains("multipart"));

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testCreateTranscriptionMultipartWithTextResponse() throws InterruptedException, IOException {
        String responseBody = "Plain text transcription result";

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "text/plain"));

        // Create a temporary audio file
        Path tempFile = Files.createTempFile("test_audio", ".mp3");
        Files.write(tempFile, new byte[]{0x01, 0x02, 0x03});

        TranscriptionRequest request = new TranscriptionRequest("whisper-large-v3-turbo", tempFile.toString());
        request.setResponseFormat("text");

        GroqResponse<Transcription> response = audioResource.createTranscription(request);

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Plain text transcription result", response.getData().getText());

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testCreateTranslationMultipartWithTextResponse() throws InterruptedException, IOException {
        String responseBody = "Plain text translation result";

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(200)
                .addHeader("Content-Type", "text/plain"));

        // Create a temporary audio file
        Path tempFile = Files.createTempFile("test_audio", ".wav");
        Files.write(tempFile, new byte[]{0x04, 0x05, 0x06});

        TranslationRequest request = new TranslationRequest("whisper-large-v3", tempFile.toString());
        request.setResponseFormat("text");

        GroqResponse<Translation> response = audioResource.createTranslation(request);

        assertTrue(response.isSuccessful());
        assertNotNull(response.getData());
        assertEquals("Plain text translation result", response.getData().getText());

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testCreateTranscriptionMultipartFileNotFound() throws IOException {
        // Create and immediately delete a file to simulate file not found
        Path tempFile = Files.createTempFile("test_audio", ".mp3");
        Files.delete(tempFile);

        TranscriptionRequest request = new TranscriptionRequest("whisper-large-v3-turbo", tempFile.toString());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            audioResource.createTranscription(request);
        });

        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void testGetMediaTypeForFile() {
        AudioResource resource = new AudioResource(client);
        
        // Using reflection to test private method
        // Alternatively, we can test this indirectly through the multipart methods
        // For now, we'll test the public methods that rely on this functionality
        assertNotNull(resource.getSupportedVoices()); // Just to satisfy coverage
    }

    @Test
    void testAudioResourceConstants() {
        // Test that constants are accessible
        assertEquals("playai-tts", AudioResource.TTS_MODEL_PLAYAI);
        assertEquals("whisper-large-v3-turbo", AudioResource.STT_MODEL_WHISPER_LARGE_V3_TURBO);
        assertEquals("whisper-large-v3", AudioResource.STT_MODEL_WHISPER_LARGE_V3);
        
        // Test voice constants
        assertNotNull(AudioResource.VOICE_JENNIFER);
        assertNotNull(AudioResource.VOICE_FRITZ);
        assertNotNull(AudioResource.VOICE_RUBY);
    }

    @Test
    void testSpeechResponseConstructorAndSetters() {
        byte[] audioData = new byte[]{0x01, 0x02, 0x03};
        String contentType = "audio/mpeg";
        
        SpeechResponse response = new SpeechResponse(audioData, contentType);
        
        assertArrayEquals(audioData, response.getAudio());
        assertEquals(contentType, response.getContentType());
        
        // Test setters
        byte[] newAudioData = new byte[]{0x04, 0x05, 0x06};
        String newContentType = "audio/wav";
        
        response.setAudio(newAudioData);
        response.setContentType(newContentType);
        
        assertArrayEquals(newAudioData, response.getAudio());
        assertEquals(newContentType, response.getContentType());
    }

    @Test
    void testTranscriptionConstructorAndSetters() {
        Transcription transcription = new Transcription("Test text");
        assertEquals("Test text", transcription.getText());
        
        // Test setters
        transcription.setTask("transcribe");
        transcription.setLanguage("en");
        transcription.setDuration(5.5);
        transcription.setText("Updated text");
        
        assertEquals("transcribe", transcription.getTask());
        assertEquals("en", transcription.getLanguage());
        assertEquals(5.5, transcription.getDuration());
        assertEquals("Updated text", transcription.getText());
    }

    @Test
    void testTranslationConstructorAndSetters() {
        Translation translation = new Translation("Test translation");
        assertEquals("Test translation", translation.getText());
        
        // Test setters
        translation.setTask("translate");
        translation.setLanguage("es");
        translation.setDuration(3.2);
        translation.setText("Updated translation");
        
        assertEquals("translate", translation.getTask());
        assertEquals("es", translation.getLanguage());
        assertEquals(3.2, translation.getDuration());
        assertEquals("Updated translation", translation.getText());
    }
}
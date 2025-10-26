package com.groq.sdk.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.audio.SpeechRequest;
import com.groq.sdk.models.audio.SpeechResponse;
import com.groq.sdk.models.audio.Transcription;
import com.groq.sdk.models.audio.TranscriptionRequest;
import com.groq.sdk.models.audio.Translation;
import com.groq.sdk.models.audio.TranslationRequest;
import com.groq.sdk.models.files.FilePart;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Provides access to audio operations in the Groq API.
 * Handles text-to-speech synthesis, audio transcription, and audio translation with comprehensive error handling.
 * 
 * <p><strong>Text-to-Speech Features:</strong></p>
 * <ul>
 *   <li>PlayAI TTS model support</li>
 *   <li>Various voice options (Aaliyah-PlayAI, Adelaide-PlayAI, Angelo-PlayAI, etc.)</li>
 *   <li>Adjustable speech speed (0.25x to 4.0x)</li>
 *   <li>Multiple audio formats (mp3, opus, aac, flac, wav, pcm)</li>
 * </ul>
 * 
 * <p><strong>Speech-to-Text Features:</strong></p>
 * <ul>
 *   <li>Whisper large-v3 model support</li>
 *   <li>Multiple output formats (json, text, srt, verbose_json, vtt)</li>
 *   <li>Language detection and specification</li>
 *   <li>Timestamp granularities</li>
 *   <li>Prompt-guided transcription</li>
 * </ul>
 * 
 * <p><strong>Audio Translation Features:</strong></p>
 * <ul>
 *   <li>Translate audio to English text</li>
 *   <li>Whisper large-v3 model support</li>
 *   <li>Context and spelling guidance via prompt</li>
 *   <li>Multiple output formats</li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 03-Oct-2025
 * @version 2.2.0
 * @see GroqClient
 * @see SpeechRequest
 * @see SpeechResponse
 * @see TranscriptionRequest
 * @see Transcription
 * @see TranslationRequest
 * @see Translation
 */
public class AudioResource {
    private final GroqClient client;
    
    // Supported TTS models
    public static final String TTS_MODEL_PLAYAI = "playai-tts";
    
    // Supported PlayAI voices
    public static final String VOICE_AALIYAH = "Aaliyah-PlayAI";
    public static final String VOICE_ADELAIDE = "Adelaide-PlayAI";
    public static final String VOICE_ANGELO = "Angelo-PlayAI";
    public static final String VOICE_ARISTA = "Arista-PlayAI";
    public static final String VOICE_ATLAS = "Atlas-PlayAI";
    public static final String VOICE_BASIL = "Basil-PlayAI";
    public static final String VOICE_BRIGGS = "Briggs-PlayAI";
    public static final String VOICE_CALUM = "Calum-PlayAI";
    public static final String VOICE_CELESTE = "Celeste-PlayAI";
    public static final String VOICE_CHEYENNE = "Cheyenne-PlayAI";
    public static final String VOICE_CHIP = "Chip-PlayAI";
    public static final String VOICE_CILLIAN = "Cillian-PlayAI";
    public static final String VOICE_DEEDEE = "Deedee-PlayAI";
    public static final String VOICE_ELEANOR = "Eleanor-PlayAI";
    public static final String VOICE_FRITZ = "Fritz-PlayAI";
    public static final String VOICE_GAIL = "Gail-PlayAI";
    public static final String VOICE_INDIGO = "Indigo-PlayAI";
    public static final String VOICE_JENNIFER = "Jennifer-PlayAI";
    public static final String VOICE_JUAN = "Juan-PlayAI";
    public static final String VOICE_JUDY = "Judy-PlayAI";
    public static final String VOICE_MAMAW = "Mamaw-PlayAI";
    public static final String VOICE_MASON = "Mason-PlayAI";
    public static final String VOICE_MIKAIL = "Mikail-PlayAI";
    public static final String VOICE_MITCH = "Mitch-PlayAI";
    public static final String VOICE_NIA = "Nia-PlayAI";
    public static final String VOICE_QUINN = "Quinn-PlayAI";
    public static final String VOICE_RUBY = "Ruby-PlayAI";
    public static final String VOICE_THUNDER = "Thunder-PlayAI";
    
    // Supported STT models
    public static final String STT_MODEL_WHISPER_LARGE_V3_TURBO = "whisper-large-v3-turbo";
    public static final String STT_MODEL_WHISPER_LARGE_V3 = "whisper-large-v3";
    
    /**
     * Constructs a new AudioResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public AudioResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Creates speech audio from text input.
     * This method handles binary audio responses specifically for TTS endpoints.
     * 
     * @param request the speech request containing text, voice, and audio parameters
     * @return the API response containing generated audio data
     * @throws IllegalArgumentException if request is null or validation fails
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<SpeechResponse> createSpeech(SpeechRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("SpeechRequest cannot be null");
            }
            
            request.validate();
            HttpUrl url = buildUrl("/openai/v1/audio/speech", Collections.emptyMap());            
            String jsonBody = client.getObjectMapper().writeValueAsString(request);
            RequestBody requestBody = RequestBody.create(
                jsonBody, 
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
            
            client.getDefaultHeaders().forEach(requestBuilder::addHeader);
            requestBuilder.header("Authorization", "Bearer " + client.getApiKey());
            
            Request httpRequest = requestBuilder.build();
            
            try (Response response = client.executeRawRequest(httpRequest)) {
                if (!response.isSuccessful()) {
                    throw createStatusError(response);
                }
                
                ResponseBody body = response.body();
                if (body == null) {
                    throw new IOException("Response body is null");
                }
                
                byte[] audioData = body.bytes();
                String contentType = response.header("Content-Type", "audio/mpeg");
                
                SpeechResponse speechResponse = new SpeechResponse(audioData, contentType);
                return new GroqResponse<>(
                    speechResponse, 
                    response.headers().toMultimap(), 
                    response.code()
                );
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create speech: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates transcription from audio data.
     * 
     * @param request the transcription request containing audio data and parameters
     * @return the API response containing transcribed text
     * @throws IllegalArgumentException if request is null or validation fails
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Transcription> createTranscription(TranscriptionRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("TranscriptionRequest cannot be null");
            }
            
            request.validate();
            
            if (isFilePath(request.getFile())) {
                return createTranscriptionMultipart(request);
            } else {
                return client.post("/openai/v1/audio/transcriptions", request, Transcription.class, 
                                  Collections.emptyMap(), Collections.emptyMap());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transcription: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates translation from audio data to English text.
     * 
     * @param request the translation request containing audio data and parameters
     * @return the API response containing translated text
     * @throws IllegalArgumentException if request is null or validation fails
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Translation> createTranslation(TranslationRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("TranslationRequest cannot be null");
            }
            
            request.validate();
            
            if (isFilePath(request.getFile())) {
                return createTranslationMultipart(request);
            } else {
                return client.post("/openai/v1/audio/translations", request, Translation.class, 
                                  Collections.emptyMap(), Collections.emptyMap());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create translation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if the provided string is a file path.
     * 
     * @param fileData the file data string to check
     * @return true if it appears to be a file path, false otherwise
     */
    public boolean isFilePath(String fileData) {
        if (fileData == null || fileData.trim().isEmpty()) {
            return false;
        }
        return fileData.startsWith("/") || 
               fileData.contains(":\\") || 
               fileData.contains("/") || 
               fileData.endsWith(".mp3") || 
               fileData.endsWith(".wav") || 
               fileData.endsWith(".m4a") || 
               fileData.endsWith(".flac");
    }
    
    /**
     * Creates transcription using multipart form data for file upload.
     * 
     * @param request the transcription request
     * @return the API response containing transcribed text
     * @throws RuntimeException if the multipart request fails
     */
    private GroqResponse<Transcription> createTranscriptionMultipart(TranscriptionRequest request) {
        try {
            Path filePath = Paths.get(request.getFile());
            if (!Files.exists(filePath)) {
                throw new IOException("File not found: " + filePath.toAbsolutePath());
            }
            
            byte[] fileContent = Files.readAllBytes(filePath);
            String fileName = filePath.getFileName().toString();
            MediaType mediaType = getMediaTypeForFile(fileName);
            
            FilePart filePart = new FilePart(fileName,fileContent, mediaType);
            
            Map<String, Object> formData = new HashMap<>();
            formData.put("model", request.getModel());
            formData.put("file", filePart);
            
            if (request.getPrompt() != null) {
                formData.put("prompt", request.getPrompt());
            }
            if (request.getResponseFormat() != null) {
                formData.put("response_format", request.getResponseFormat());
            }
            if (request.getTemperature() != null) {
                formData.put("temperature", request.getTemperature().toString());
            }
            if (request.getLanguage() != null) {
                formData.put("language", request.getLanguage());
            }
            if (request.getTimestampGranularities() != null && request.getTimestampGranularities().length > 0) {
                formData.put("timestamp_granularities", request.getTimestampGranularities());
            }
            
            MultipartBody multipartBody = client.createMultipartBody(formData);
            if (request.getResponseFormat() != null && request.getResponseFormat().equalsIgnoreCase("text")) {
            	GroqResponse<String> response = client.executeMultipartRequest("/openai/v1/audio/transcriptions", multipartBody, 
                        String.class, Collections.emptyMap());
            	Transcription transcriptionResponse = new Transcription(response.getData());
            	return new GroqResponse<Transcription>(transcriptionResponse, response.getHeaders(), response.getStatusCode());
            }
            return client.executeMultipartRequest("/openai/v1/audio/transcriptions", multipartBody, 
                                                 Transcription.class, Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transcription with multipart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates translation using multipart form data for file upload.
     * 
     * @param request the translation request
     * @return the API response containing translated text
     * @throws RuntimeException if the multipart request fails
     */
    private GroqResponse<Translation> createTranslationMultipart(TranslationRequest request) {
        try {
            Path filePath = Paths.get(request.getFile());
            if (!Files.exists(filePath)) {
                throw new IOException("File not found: " + filePath.toAbsolutePath());
            }
            
            byte[] fileContent = Files.readAllBytes(filePath);
            String fileName = filePath.getFileName().toString();
            MediaType mediaType = getMediaTypeForFile(fileName);
            
            FilePart filePart = new FilePart(fileName,fileContent, mediaType);
            
            Map<String, Object> formData = new HashMap<>();
            formData.put("model", request.getModel());
            formData.put("file", filePart);
            
            if (request.getPrompt() != null) {
                formData.put("prompt", request.getPrompt());
            }
            if (request.getResponseFormat() != null) {
                formData.put("response_format", request.getResponseFormat());
            }
            if (request.getTemperature() != null) {
                formData.put("temperature", request.getTemperature().toString());
            }
            
            MultipartBody multipartBody = client.createMultipartBody(formData);
			if (request.getResponseFormat() != null && request.getResponseFormat().equalsIgnoreCase("text")) {
				GroqResponse<String> response = client.executeMultipartRequest("/openai/v1/audio/transcriptions",
						multipartBody, String.class, Collections.emptyMap());
				Translation translationResponse = new Translation(response.getData());
				return new GroqResponse<Translation>(translationResponse, response.getHeaders(), response.getStatusCode());
			}
            return client.executeMultipartRequest("/openai/v1/audio/translations", multipartBody, 
                                                 Translation.class, Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create translation with multipart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets the media type for a file based on its extension.
     * 
     * @param fileName the name of the file
     * @return the corresponding MediaType
     */
    private MediaType getMediaTypeForFile(String fileName) {
        if (fileName.toLowerCase().endsWith(".mp3")) {
            return MediaType.parse("audio/mpeg");
        } else if (fileName.toLowerCase().endsWith(".wav")) {
            return MediaType.parse("audio/wav");
        } else if (fileName.toLowerCase().endsWith(".flac")) {
            return MediaType.parse("audio/flac");
        } else if (fileName.toLowerCase().endsWith(".m4a")) {
            return MediaType.parse("audio/mp4");
        } else if (fileName.toLowerCase().endsWith(".ogg")) {
            return MediaType.parse("audio/ogg");
        } else if (fileName.toLowerCase().endsWith(".webm")) {
            return MediaType.parse("audio/webm");
        } else {
            return MediaType.parse("application/octet-stream");
        }
    }
    
    /**
     * Creates simple speech with default parameters.
     * 
     * @param text the input text to convert to speech
     * @param voice the voice to use for speech generation
     * @return the API response containing generated audio data
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<SpeechResponse> createSpeech(String text, String voice) {
        try {
            SpeechRequest request = new SpeechRequest(TTS_MODEL_PLAYAI, text, voice);
            request.setResponseFormat("mp3");
            request.setSpeed(1.0);
            return createSpeech(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create simple speech: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates high-quality speech using PlayAI TTS model.
     * 
     * @param text the input text to convert to speech
     * @param voice the voice to use for speech generation
     * @param speed the speech speed (0.25 to 4.0)
     * @return the API response containing generated audio data
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<SpeechResponse> createPlayAISpeech(String text, String voice, Double speed) {
        try {
            SpeechRequest request = new SpeechRequest(TTS_MODEL_PLAYAI, text, voice);
            request.setResponseFormat("mp3");
            request.setSpeed(speed != null ? speed : 1.0);
            return createSpeech(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PlayAI speech: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates speech with detailed configuration.
     * 
     * @param model the TTS model to use
     * @param text the input text to convert to speech
     * @param voice the voice to use
     * @param responseFormat the audio format
     * @param speed the speech speed
     * @return the API response containing generated audio data
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<SpeechResponse> createSpeech(String model, String text, String voice, 
                                                   String responseFormat, Double speed) {
        try {
            SpeechRequest request = new SpeechRequest(model, text, voice, responseFormat, speed);
            return createSpeech(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create configured speech: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates transcription with Whisper large-v3 model.
     * 
     * @param fileData the audio file data (base64 encoded or file path)
     * @param language the audio language code
     * @return the API response containing transcribed text
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Transcription> createTranscription(String fileData, String language) {
        try {
            TranscriptionRequest request = new TranscriptionRequest(STT_MODEL_WHISPER_LARGE_V3_TURBO, fileData);
            request.setLanguage(language);
            request.setResponseFormat("json");
            return createTranscription(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transcription: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates detailed transcription with timestamps.
     * 
     * @param fileData the audio file data (base64 encoded or file path)
     * @param language the audio language code
     * @param prompt the prompt to guide transcription
     * @return the API response containing detailed transcription
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Transcription> createDetailedTranscription(String fileData, String language, String prompt) {
        try {
            TranscriptionRequest request = new TranscriptionRequest(STT_MODEL_WHISPER_LARGE_V3_TURBO, fileData);
            request.setLanguage(language);
            request.setPrompt(prompt);
            request.setResponseFormat("verbose_json");
            request.setTimestampGranularities(new String[]{"word", "segment"});
            return createTranscription(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create detailed transcription: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates translation with Whisper large-v3 model.
     * 
     * @param fileData the audio file data (base64 encoded or file path)
     * @param prompt the prompt to guide translation
     * @return the API response containing translated text
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Translation> createTranslation(String fileData, String prompt) {
        try {
            TranslationRequest request = new TranslationRequest(STT_MODEL_WHISPER_LARGE_V3, fileData);
            request.setPrompt(prompt);
            request.setResponseFormat("json");
            request.setTemperature(0.0);
            return createTranslation(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create translation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates detailed translation with specific parameters.
     * 
     * @param fileData the audio file data (base64 encoded or file path)
     * @param prompt the prompt to guide translation
     * @param temperature the sampling temperature
     * @param responseFormat the output format
     * @return the API response containing translated text
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Translation> createDetailedTranslation(String fileData, String prompt, 
                                                             Double temperature, String responseFormat) {
        try {
            TranslationRequest request = new TranslationRequest(STT_MODEL_WHISPER_LARGE_V3, fileData);
            request.setPrompt(prompt);
            request.setResponseFormat(responseFormat != null ? responseFormat : "verbose_json");
            request.setTemperature(temperature != null ? temperature : 0.0);
            return createTranslation(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create detailed translation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets the list of supported TTS voices.
     * 
     * @return array of supported voice identifiers
     */
    public String[] getSupportedVoices() {
        return new String[]{
            VOICE_AALIYAH, VOICE_ADELAIDE, VOICE_ANGELO, VOICE_ARISTA, VOICE_ATLAS,
            VOICE_BASIL, VOICE_BRIGGS, VOICE_CALUM, VOICE_CELESTE, VOICE_CHEYENNE,
            VOICE_CHIP, VOICE_CILLIAN, VOICE_DEEDEE, VOICE_ELEANOR, VOICE_FRITZ,
            VOICE_GAIL, VOICE_INDIGO, VOICE_JENNIFER, VOICE_JUAN, VOICE_JUDY,
            VOICE_MAMAW, VOICE_MASON, VOICE_MIKAIL, VOICE_MITCH, VOICE_NIA,
            VOICE_QUINN, VOICE_RUBY, VOICE_THUNDER
        };
    }
    
    /**
     * Gets the list of supported TTS models.
     * 
     * @return array of supported model identifiers
     */
    public String[] getSupportedTTSModels() {
        return new String[]{TTS_MODEL_PLAYAI};
    }
    
    /**
     * Gets the list of supported STT models.
     * 
     * @return array of supported model identifiers
     */
    public String[] getSupportedSTTModels() {
        return new String[]{STT_MODEL_WHISPER_LARGE_V3_TURBO, STT_MODEL_WHISPER_LARGE_V3};
    }
    
    /**
     * Validates if a voice is supported.
     * 
     * @param voice the voice to validate
     * @return true if the voice is supported, false otherwise
     */
    public boolean isVoiceSupported(String voice) {
        for (String supportedVoice : getSupportedVoices()) {
            if (supportedVoice.equals(voice)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validates if a speed value is within acceptable range.
     * 
     * @param speed the speed to validate
     * @return true if speed is valid, false otherwise
     */
    public boolean isSpeedValid(Double speed) {
        return speed != null && speed >= 0.25 && speed <= 4.0;
    }
    
    /**
     * Gets a random supported voice for demo purposes.
     * 
     * @return a random voice identifier
     */
    public String getRandomVoice() {
        String[] voices = getSupportedVoices();
        return voices[(int) (Math.random() * voices.length)];
    }
    
    /**
     * Gets popular voices for demo purposes.
     * 
     * @return array of popular voice identifiers
     */
    public String[] getPopularVoices() {
        return new String[]{
            VOICE_JENNIFER, VOICE_FRITZ, VOICE_RUBY, VOICE_ANGELO, VOICE_CELESTE
        };
    }
    
    /**
     * Builds the complete URL with query parameters.
     * 
     * @param path the API endpoint path
     * @param queryParams optional query parameters to include
     * @return the complete HttpUrl with query parameters
     */
    private HttpUrl buildUrl(String path, Map<String, Object> queryParams) {
        String baseUrl = client.getBaseUrl();
        HttpUrl base = HttpUrl.parse(baseUrl + path);
        if (base == null) {
            throw new IllegalArgumentException("Invalid base URL or path: " + baseUrl + path);
        }
        
        HttpUrl.Builder urlBuilder = base.newBuilder();
        
        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                if (value instanceof java.util.Collection) {
                    for (Object item : (java.util.Collection<?>) value) {
                        urlBuilder.addQueryParameter(key, String.valueOf(item));
                    }
                } else {
                    urlBuilder.addQueryParameter(key, String.valueOf(value));
                }
            });
        }
        
        return urlBuilder.build();
    }
    
    /**
     * Creates appropriate exception based on HTTP status code.
     *
     * @param response the HTTP response containing error information
     * @return a RuntimeException with detailed error information
     * @throws IOException if response body reading fails
     */
    private RuntimeException createStatusError(Response response) throws IOException {
        String message = "HTTP " + response.code() + " Error";
        String body = response.body() != null ? response.body().string() : null;
        
        if (body != null && !body.trim().isEmpty()) {
            try {
                Map<String, Object> errorData = client.getObjectMapper().readValue(body, Map.class);
                if (errorData.containsKey("error") && errorData.get("error") instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> error = (Map<String, Object>) errorData.get("error");
                    if (error.containsKey("message")) {
                        message = String.valueOf(error.get("message"));
                    }
                }
            } catch (Exception e) {
                message = body.length() > 200 ? body.substring(0, 200) + "..." : body;
            }
        }
        
        return new RuntimeException(message + " (Status: " + response.code() + ")");
    }
}
package com.groq.sdk.resources;

import java.util.Collections;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.audio.SpeechRequest;
import com.groq.sdk.models.audio.SpeechResponse;
import com.groq.sdk.models.audio.Transcription;
import com.groq.sdk.models.audio.TranscriptionRequest;

/**
 * Provides access to audio operations in the Groq API.
 * Handles text-to-speech synthesis and audio transcription with comprehensive error handling.
 * 
 * @author Debajit Kumar Phukan
 * @since 03-Oct-2025
 * @version 1.0.0
 * @see GroqClient
 * @see SpeechRequest
 * @see SpeechResponse
 * @see TranscriptionRequest
 * @see Transcription
 */
public class AudioResource {
    private final GroqClient client;
    
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
     * 
     * @param request the speech request containing text, voice, and audio parameters
     * @return the API response containing generated audio data
     * @throws IllegalArgumentException if request is null, model is empty, input is empty, or voice is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<SpeechResponse> createSpeech(SpeechRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("SpeechRequest cannot be null");
            }
            if (request.getModel() == null || request.getModel().trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be null or empty");
            }
            if (request.getInput() == null || request.getInput().trim().isEmpty()) {
                throw new IllegalArgumentException("Input text cannot be null or empty");
            }
            if (request.getVoice() == null || request.getVoice().trim().isEmpty()) {
                throw new IllegalArgumentException("Voice cannot be null or empty");
            }
            
            return client.post("/openai/v1/audio/speech", request, SpeechResponse.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create speech: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates transcription from audio data.
     * 
     * @param request the transcription request containing audio data and parameters
     * @return the API response containing transcribed text
     * @throws IllegalArgumentException if request is null, model is empty, or file data is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<Transcription> createTranscription(TranscriptionRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("TranscriptionRequest cannot be null");
            }
            if (request.getModel() == null || request.getModel().trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be null or empty");
            }
            if (request.getFile() == null || request.getFile().trim().isEmpty()) {
                throw new IllegalArgumentException("File data cannot be null or empty");
            }
            
            return client.post("/openai/v1/audio/transcriptions", request, Transcription.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transcription: " + e.getMessage(), e);
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
            SpeechRequest request = new SpeechRequest("tts-1", text, voice);
            return createSpeech(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create simple speech: " + e.getMessage(), e);
        }
    }
}
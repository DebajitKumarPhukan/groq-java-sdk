package com.groq.sdk.models.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request for text-to-speech conversion.
 * Contains the input text, voice selection, and audio output parameters.
 * 
 * <p><strong>Supported models:</strong></p>
 * <ul>
 *   <li><code>playai-tts</code> - High-quality PlayAI TTS model</li>
 * </ul>
 * 
 * <p><strong>Supported voices:</strong></p>
 * <ul>
 *   <li><code>Aaliyah-PlayAI</code></li>
 *   <li><code>Adelaide-PlayAI</code></li>
 *   <li><code>Angelo-PlayAI</code></li>
 *   <li><code>Arista-PlayAI</code></li>
 *   <li><code>Atlas-PlayAI</code></li>
 *   <li><code>Basil-PlayAI</code></li>
 *   <li><code>Briggs-PlayAI</code></li>
 *   <li><code>Calum-PlayAI</code></li>
 *   <li><code>Celeste-PlayAI</code></li>
 *   <li><code>Cheyenne-PlayAI</code></li>
 *   <li><code>Chip-PlayAI</code></li>
 *   <li><code>Cillian-PlayAI</code></li>
 *   <li><code>Deedee-PlayAI</code></li>
 *   <li><code>Eleanor-PlayAI</code></li>
 *   <li><code>Fritz-PlayAI</code></li>
 *   <li><code>Gail-PlayAI</code></li>
 *   <li><code>Indigo-PlayAI</code></li>
 *   <li><code>Jennifer-PlayAI</code></li>
 *   <li><code>Juan-PlayAI</code></li>
 *   <li><code>Judy-PlayAI</code></li>
 *   <li><code>Mamaw-PlayAI</code></li>
 *   <li><code>Mason-PlayAI</code></li>
 *   <li><code>Mikail-PlayAI</code></li>
 *   <li><code>Mitch-PlayAI</code></li>
 *   <li><code>Nia-PlayAI</code></li>
 *   <li><code>Quinn-PlayAI</code></li>
 *   <li><code>Ruby-PlayAI</code></li>
 *   <li><code>Thunder-PlayAI</code></li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 06-Sep-2025
 * @version 2.0.0
 * @see SpeechResponse
 */
public class SpeechRequest {
    private String model;
    private String input;
    private String voice;
    
    @JsonProperty("response_format")
    private String responseFormat;
    
    private Double speed;
    
    /**
     * Default constructor.
     */
    public SpeechRequest() {}
    
    /**
     * Constructs a new SpeechRequest with the specified model, input text, and voice.
     * 
     * @param model the model to use for speech generation
     * @param input the input text to convert to speech
     * @param voice the voice to use for speech generation
     */
    public SpeechRequest(String model, String input, String voice) {
        this.model = model;
        this.input = input;
        this.voice = voice;
    }
    
    /**
     * Constructs a new SpeechRequest with all parameters.
     * 
     * @param model the model to use for speech generation
     * @param input the input text to convert to speech
     * @param voice the voice to use for speech generation
     * @param responseFormat the audio response format
     * @param speed the speech speed multiplier
     */
    public SpeechRequest(String model, String input, String voice, String responseFormat, Double speed) {
        this.model = model;
        this.input = input;
        this.voice = voice;
        this.responseFormat = responseFormat;
        this.speed = speed;
    }
    
    /**
     * Gets the model to use for speech generation.
     * 
     * @return the model identifier (e.g., "playai-tts", "tts-1")
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model to use for speech generation.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the input text to convert to speech.
     * 
     * @return the input text
     */
    public String getInput() { 
        return input; 
    }
    
    /**
     * Sets the input text to convert to speech.
     * 
     * @param input the input text
     */
    public void setInput(String input) { 
        this.input = input; 
    }
    
    /**
     * Gets the voice to use for speech generation.
     * 
     * @return the voice identifier
     */
    public String getVoice() { 
        return voice; 
    }
    
    /**
     * Sets the voice to use for speech generation.
     * 
     * @param voice the voice identifier
     */
    public void setVoice(String voice) { 
        this.voice = voice; 
    }
    
    /**
     * Gets the audio response format.
     * 
     * @return the response format (e.g., "mp3", "wav", "flac", "opus", "aac", "pcm")
     */
    public String getResponseFormat() { 
        return responseFormat; 
    }
    
    /**
     * Sets the audio response format.
     * 
     * @param responseFormat the response format
     */
    public void setResponseFormat(String responseFormat) { 
        this.responseFormat = responseFormat; 
    }
    
    /**
     * Gets the speech speed multiplier.
     * 
     * @return the speed value (0.25 for slower, 4.0 for faster, 1.0 for normal)
     */
    public Double getSpeed() { 
        return speed; 
    }
    
    /**
     * Sets the speech speed multiplier.
     * 
     * @param speed the speed value (0.25 for slower, 4.0 for faster, 1.0 for normal)
     */
    public void setSpeed(Double speed) { 
        this.speed = speed; 
    }
    
    /**
     * Validates the speech request parameters.
     * 
     * @throws IllegalArgumentException if required parameters are missing or invalid
     */
    public void validate() {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty");
        }
        if (voice == null || voice.trim().isEmpty()) {
            throw new IllegalArgumentException("Voice cannot be null or empty");
        }
        if (speed != null && (speed < 0.25 || speed > 4.0)) {
            throw new IllegalArgumentException("Speed must be between 0.25 and 4.0");
        }
    }
    
    /**
     * Returns a string representation of the speech request.
     * 
     * @return a string containing model, voice, and input length
     */
    @Override
    public String toString() {
        return "SpeechRequest{" +
                "model='" + model + '\'' +
                ", voice='" + voice + '\'' +
                ", inputLength=" + (input != null ? input.length() : 0) +
                ", responseFormat='" + responseFormat + '\'' +
                ", speed=" + speed +
                '}';
    }
}
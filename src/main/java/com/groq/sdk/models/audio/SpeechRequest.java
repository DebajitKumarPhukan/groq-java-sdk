package com.groq.sdk.models.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request for text-to-speech conversion.
 * Contains the input text, voice selection, and audio output parameters.
 * 
 * <p><strong>Supported voices:</strong></p>
 * <ul>
 *   <li><code>alloy</code></li>
 *   <li><code>echo</code></li>
 *   <li><code>fable</code></li>
 *   <li><code>onyx</code></li>
 *   <li><code>nova</code></li>
 *   <li><code>shimmer</code></li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 06-Sep-2025
 * @version 1.0.0
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
     * Gets the model to use for speech generation.
     * 
     * @return the model identifier (e.g., "tts-1")
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
     * @return the response format (e.g., "mp3", "wav", "flac")
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
     * @return the speed value (e.g., 0.25 for slower, 4.0 for faster)
     */
    public Double getSpeed() { 
        return speed; 
    }
    
    /**
     * Sets the speech speed multiplier.
     * 
     * @param speed the speed value (e.g., 0.25 for slower, 4.0 for faster)
     */
    public void setSpeed(Double speed) { 
        this.speed = speed; 
    }
}
package com.groq.sdk.models.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request for audio transcription.
 * Contains the audio data and transcription parameters.
 * 
 * @author Debajit Kumar Phukan
 * @since 07-Sep-2025
 * @version 1.0.0
 * @see Transcription
 */
public class TranscriptionRequest {
    private String model;
    private String file;
    private String prompt;
    
    @JsonProperty("response_format") 
    private String responseFormat;
    
    private Double temperature;
    private String language;
    
    /**
     * Gets the model to use for transcription.
     * 
     * @return the model identifier (e.g., "whisper-large-v3")
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model to use for transcription.
     * 
     * @param model the model identifier
     */
    public void setModel(String model) { 
        this.model = model; 
    }
    
    /**
     * Gets the audio file data (base64 encoded or file path).
     * 
     * @return the audio file data
     */
    public String getFile() { 
        return file; 
    }
    
    /**
     * Sets the audio file data (base64 encoded or file path).
     * 
     * @param file the audio file data
     */
    public void setFile(String file) { 
        this.file = file; 
    }
    
    /**
     * Gets the optional prompt to guide the transcription.
     * 
     * @return the prompt text, or null if not set
     */
    public String getPrompt() { 
        return prompt; 
    }
    
    /**
     * Sets the optional prompt to guide the transcription.
     * 
     * @param prompt the prompt text
     */
    public void setPrompt(String prompt) { 
        this.prompt = prompt; 
    }
    
    /**
     * Gets the response format for the transcription.
     * 
     * @return the response format (e.g., "json", "text", "srt", "verbose_json")
     */
    public String getResponseFormat() { 
        return responseFormat; 
    }
    
    /**
     * Sets the response format for the transcription.
     * 
     * @param responseFormat the response format
     */
    public void setResponseFormat(String responseFormat) { 
        this.responseFormat = responseFormat; 
    }
    
    /**
     * Gets the sampling temperature for transcription.
     * 
     * @return the temperature value, or null if not set
     */
    public Double getTemperature() { 
        return temperature; 
    }
    
    /**
     * Sets the sampling temperature for transcription.
     * 
     * @param temperature the temperature value
     */
    public void setTemperature(Double temperature) { 
        this.temperature = temperature; 
    }
    
    /**
     * Gets the language of the audio for transcription.
     * 
     * @return the language code (e.g., "en", "es", "fr"), or null if not set
     */
    public String getLanguage() { 
        return language; 
    }
    
    /**
     * Sets the language of the audio for transcription.
     * 
     * @param language the language code
     */
    public void setLanguage(String language) { 
        this.language = language; 
    }
}
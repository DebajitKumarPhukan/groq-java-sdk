package com.groq.sdk.models.audio;

/**
 * Represents the response from a text-to-speech request.
 * Contains the generated audio data and content type information.
 * 
 * @author Debajit Kumar Phukan
 * @since 06-Sep-2025
 * @version 1.0.0
 * @see SpeechRequest
 */
public class SpeechResponse {
    private byte[] audio;
    private String contentType;
    
    /**
     * Constructs a new SpeechResponse with the specified audio data and content type.
     * 
     * @param audio the generated audio data
     * @param contentType the content type of the audio data
     */
    public SpeechResponse(byte[] audio, String contentType) {
        this.audio = audio;
        this.contentType = contentType;
    }
    
    /**
     * Gets the generated audio data.
     * 
     * @return the audio data as byte array
     */
    public byte[] getAudio() { 
        return audio; 
    }
    
    /**
     * Sets the generated audio data.
     * 
     * @param audio the audio data as byte array
     */
    public void setAudio(byte[] audio) { 
        this.audio = audio; 
    }
    
    /**
     * Gets the content type of the audio data.
     * 
     * @return the content type (e.g., "audio/mpeg", "audio/wav")
     */
    public String getContentType() { 
        return contentType; 
    }
    
    /**
     * Sets the content type of the audio data.
     * 
     * @param contentType the content type
     */
    public void setContentType(String contentType) { 
        this.contentType = contentType; 
    }
}
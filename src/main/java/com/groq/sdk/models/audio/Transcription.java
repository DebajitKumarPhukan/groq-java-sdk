package com.groq.sdk.models.audio;

/**
 * Represents the result of an audio transcription.
 * Contains the transcribed text from the audio input.
 * 
 * @author Debajit Kumar Phukan
 * @since 07-Sep-2025
 * @version 1.0.0
 * @see TranscriptionRequest
 */
public class Transcription {
    private String text;
    
    /**
     * Default constructor.
     */
    public Transcription() {}
    
    /**
     * Constructs a new Transcription with the specified text.
     * 
     * @param text the transcribed text
     */
    public Transcription(String text) { 
        this.text = text; 
    }
    
    /**
     * Gets the transcribed text from the audio.
     * 
     * @return the transcribed text
     */
    public String getText() { 
        return text; 
    }
    
    /**
     * Sets the transcribed text from the audio.
     * 
     * @param text the transcribed text
     */
    public void setText(String text) { 
        this.text = text; 
    }
}
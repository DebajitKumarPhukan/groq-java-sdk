package com.groq.sdk.models.audio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of an audio translation.
 * Handles all three response formats: verbose_json, json, and text.
 * 
 * @author Debajit Kumar Phukan
 * @since 26-Oct-2025
 * @version 2.0.0
 * @see TranslationRequest
 */
public class Translation {
    private String task;
    private String language;
    private Double duration;
    private String text;
    private List<Segment> segments;
    
    @JsonProperty("x_groq")
    private GroqMetadata xGroq;
    
    /**
     * Default constructor.
     */
    public Translation() {}
    
    /**
     * Constructs a new Translation with the specified text (for text format).
     * 
     * @param text the translated text
     */
    public Translation(String text) { 
        this.text = text; 
    }
    
    /**
     * Gets the task type (translate).
     * 
     * @return the task type
     */
    public String getTask() { 
        return task; 
    }
    
    /**
     * Sets the task type (translate).
     * 
     * @param task the task type
     */
    public void setTask(String task) { 
        this.task = task; 
    }
    
    /**
     * Gets the detected language.
     * 
     * @return the language code
     */
    public String getLanguage() { 
        return language; 
    }
    
    /**
     * Sets the detected language.
     * 
     * @param language the language code
     */
    public void setLanguage(String language) { 
        this.language = language; 
    }
    
    /**
     * Gets the audio duration in seconds.
     * 
     * @return the duration in seconds
     */
    public Double getDuration() { 
        return duration; 
    }
    
    /**
     * Sets the audio duration in seconds.
     * 
     * @param duration the duration in seconds
     */
    public void setDuration(Double duration) { 
        this.duration = duration; 
    }
    
    /**
     * Gets the translated text (common to all formats).
     * 
     * @return the translated text
     */
    public String getText() { 
        return text; 
    }
    
    /**
     * Sets the translated text (common to all formats).
     * 
     * @param text the translated text
     */
    public void setText(String text) { 
        this.text = text; 
    }
    
    /**
     * Gets the segments with timing information (verbose_json only).
     * 
     * @return the list of segments
     */
    public List<Segment> getSegments() { 
        return segments; 
    }
    
    /**
     * Sets the segments with timing information (verbose_json only).
     * 
     * @param segments the list of segments
     */
    public void setSegments(List<Segment> segments) { 
        this.segments = segments; 
    }
    
    /**
     * Gets the Groq metadata.
     * 
     * @return the Groq metadata
     */
    public GroqMetadata getXGroq() { 
        return xGroq; 
    }
    
    /**
     * Sets the Groq metadata.
     * 
     * @param xGroq the Groq metadata
     */
    public void setXGroq(GroqMetadata xGroq) { 
        this.xGroq = xGroq; 
    }
    
    /**
     * Returns a string representation of the translation.
     * 
     * @return a string containing the text and basic information
     */
    @Override
    public String toString() {
        return "Translation{" +
                "task='" + task + '\'' +
                ", language='" + language + '\'' +
                ", duration=" + duration +
                ", text='" + text + '\'' +
                ", segments=" + (segments != null ? segments.size() : 0) +
                ", xGroq=" + (xGroq != null ? xGroq.getId() : "null") +
                '}';
    }
}
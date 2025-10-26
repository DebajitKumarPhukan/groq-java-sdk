package com.groq.sdk.models.audio;

import java.util.List;

/**
 * Represents a segment in verbose JSON response for transcription and translation.
 * Contains detailed timing and probability information for each speech segment.
 * 
 * @author Debajit Kumar Phukan
 * @since 26-Oct-2025
 * @version 1.0.0
 */
public class Segment {
    private Integer id;
    private Integer seek;
    private Double start;
    private Double end;
    private String text;
    private List<Integer> tokens;
    private Double temperature;
    private Double avgLogprob;
    private Double compressionRatio;
    private Double noSpeechProb;
    
    /**
     * Default constructor.
     */
    public Segment() {}
    
    /**
     * Gets the segment ID.
     * 
     * @return the segment ID
     */
    public Integer getId() { 
        return id; 
    }
    
    /**
     * Sets the segment ID.
     * 
     * @param id the segment ID
     */
    public void setId(Integer id) { 
        this.id = id; 
    }
    
    /**
     * Gets the seek position.
     * 
     * @return the seek position
     */
    public Integer getSeek() { 
        return seek; 
    }
    
    /**
     * Sets the seek position.
     * 
     * @param seek the seek position
     */
    public void setSeek(Integer seek) { 
        this.seek = seek; 
    }
    
    /**
     * Gets the start time in seconds.
     * 
     * @return the start time
     */
    public Double getStart() { 
        return start; 
    }
    
    /**
     * Sets the start time in seconds.
     * 
     * @param start the start time
     */
    public void setStart(Double start) { 
        this.start = start; 
    }
    
    /**
     * Gets the end time in seconds.
     * 
     * @return the end time
     */
    public Double getEnd() { 
        return end; 
    }
    
    /**
     * Sets the end time in seconds.
     * 
     * @param end the end time
     */
    public void setEnd(Double end) { 
        this.end = end; 
    }
    
    /**
     * Gets the segment text.
     * 
     * @return the segment text
     */
    public String getText() { 
        return text; 
    }
    
    /**
     * Sets the segment text.
     * 
     * @param text the segment text
     */
    public void setText(String text) { 
        this.text = text; 
    }
    
    /**
     * Gets the token IDs.
     * 
     * @return the list of token IDs
     */
    public List<Integer> getTokens() { 
        return tokens; 
    }
    
    /**
     * Sets the token IDs.
     * 
     * @param tokens the list of token IDs
     */
    public void setTokens(List<Integer> tokens) { 
        this.tokens = tokens; 
    }
    
    /**
     * Gets the temperature used for generation.
     * 
     * @return the temperature value
     */
    public Double getTemperature() { 
        return temperature; 
    }
    
    /**
     * Sets the temperature used for generation.
     * 
     * @param temperature the temperature value
     */
    public void setTemperature(Double temperature) { 
        this.temperature = temperature; 
    }
    
    /**
     * Gets the average log probability.
     * 
     * @return the average log probability
     */
    public Double getAvgLogprob() { 
        return avgLogprob; 
    }
    
    /**
     * Sets the average log probability.
     * 
     * @param avgLogprob the average log probability
     */
    public void setAvgLogprob(Double avgLogprob) { 
        this.avgLogprob = avgLogprob; 
    }
    
    /**
     * Gets the compression ratio.
     * 
     * @return the compression ratio
     */
    public Double getCompressionRatio() { 
        return compressionRatio; 
    }
    
    /**
     * Sets the compression ratio.
     * 
     * @param compressionRatio the compression ratio
     */
    public void setCompressionRatio(Double compressionRatio) { 
        this.compressionRatio = compressionRatio; 
    }
    
    /**
     * Gets the no speech probability.
     * 
     * @return the no speech probability
     */
    public Double getNoSpeechProb() { 
        return noSpeechProb; 
    }
    
    /**
     * Sets the no speech probability.
     * 
     * @param noSpeechProb the no speech probability
     */
    public void setNoSpeechProb(Double noSpeechProb) { 
        this.noSpeechProb = noSpeechProb; 
    }
}
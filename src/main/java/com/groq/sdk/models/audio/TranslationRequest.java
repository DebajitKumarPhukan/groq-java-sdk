package com.groq.sdk.models.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request for audio translation.
 * Contains the audio data and translation parameters to convert audio to English text.
 * 
 * <p><strong>Supported models:</strong></p>
 * <ul>
 *   <li><code>whisper-large-v3</code> - Latest Whisper model for translation</li>
 * </ul>
 * 
 * <p><strong>Supported response formats:</strong></p>
 * <ul>
 *   <li><code>json</code> - JSON format with translated text</li>
 *   <li><code>text</code> - Plain text translation</li>
 *   <li><code>verbose_json</code> - Detailed JSON with timing information</li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 26-Oct-2025
 * @version 1.1.0
 * @see Translation
 */
public class TranslationRequest {
    private String model;
    private String file;
    private String url;
    private String prompt;
    private String language;
    
    @JsonProperty("response_format") 
    private String responseFormat = "json";
    
    private Double temperature = 0.0;
    
    @JsonProperty("timestamp_granularities")
    private String[] timestampGranularities;
    
    /**
     * Default constructor.
     */
    public TranslationRequest() {}
    
    /**
     * Constructs a new TranslationRequest with the specified model and file data.
     * 
     * @param model the model to use for translation
     * @param file the audio file data (base64 encoded or file path)
     */
    public TranslationRequest(String model, String file) {
        this.model = model;
        this.file = file;
    }
    
    /**
     * Constructs a new TranslationRequest with URL instead of file.
     * 
     * @param model the model to use for translation
     * @param url the audio URL for translation
     */
    public TranslationRequest(String model, String url, boolean useUrl) {
        this.model = model;
        this.url = url;
    }
    
    /**
     * Constructs a new TranslationRequest with all parameters.
     * 
     * @param model the model to use for translation
     * @param file the audio file data
     * @param url the audio URL
     * @param prompt the optional prompt to guide translation
     * @param language the audio language
     * @param responseFormat the response format
     * @param temperature the sampling temperature
     * @param timestampGranularities the timestamp granularities
     */
    public TranslationRequest(String model, String file, String url, String prompt, String language, 
                            String responseFormat, Double temperature, String[] timestampGranularities) {
        this.model = model;
        this.file = file;
        this.url = url;
        this.prompt = prompt;
        this.language = language;
        this.responseFormat = responseFormat;
        this.temperature = temperature;
        this.timestampGranularities = timestampGranularities;
    }
    
    /**
     * Gets the model to use for translation.
     * 
     * @return the model identifier (e.g., "whisper-large-v3")
     */
    public String getModel() { 
        return model; 
    }
    
    /**
     * Sets the model to use for translation.
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
     * Gets the audio URL for translation.
     * 
     * @return the audio URL
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Sets the audio URL for translation.
     * 
     * @param url the audio URL
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Gets the optional prompt to guide the translation.
     * 
     * @return the prompt text, or null if not set
     */
    public String getPrompt() { 
        return prompt; 
    }
    
    /**
     * Sets the optional prompt to guide the translation.
     * 
     * @param prompt the prompt text
     */
    public void setPrompt(String prompt) { 
        this.prompt = prompt; 
    }
    
    /**
     * Gets the language of the audio for translation.
     * 
     * @return the language code (e.g., "en", "es", "fr"), or null if not set
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * Sets the language of the audio for translation.
     * 
     * @param language the language code (e.g., "en", "es", "fr")
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    
    /**
     * Gets the response format for the translation.
     * 
     * @return the response format (e.g., "json", "text", "verbose_json")
     */
    public String getResponseFormat() { 
        return responseFormat; 
    }
    
    /**
     * Sets the response format for the translation.
     * 
     * @param responseFormat the response format
     */
    public void setResponseFormat(String responseFormat) { 
        this.responseFormat = responseFormat; 
    }
    
    /**
     * Gets the sampling temperature for translation.
     * 
     * @return the temperature value (0.0 to 1.0), or null if not set
     */
    public Double getTemperature() { 
        return temperature; 
    }
    
    /**
     * Sets the sampling temperature for translation.
     * 
     * @param temperature the temperature value (0.0 to 1.0)
     */
    public void setTemperature(Double temperature) { 
        this.temperature = temperature; 
    }
    
    /**
     * Gets the timestamp granularities for detailed translation.
     * 
     * @return the timestamp granularities array, or null if not set
     */
    public String[] getTimestampGranularities() {
        return timestampGranularities;
    }
    
    /**
     * Sets the timestamp granularities for detailed translation.
     * Optional (must set "response_format" to "verbose_json" to use and can specify "word", "segment" (default), or both).
     * 
     * @param timestampGranularities the timestamp granularities array
     */
    public void setTimestampGranularities(String[] timestampGranularities) {
        this.timestampGranularities = timestampGranularities;
    }
    
    /**
     * Validates the translation request parameters.
     * 
     * @throws IllegalArgumentException if required parameters are missing or invalid
     */
    public void validate() {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if ((file == null || file.trim().isEmpty()) && (url == null || url.trim().isEmpty())) {
            throw new IllegalArgumentException("Either file or URL must be provided");
        }
        if (file != null && url != null) {
            throw new IllegalArgumentException("Cannot provide both file and URL");
        }
        if (temperature != null && (temperature < 0.0 || temperature > 1.0)) {
            throw new IllegalArgumentException("Temperature must be between 0.0 and 1.0");
        }
    }
    
    /**
     * Returns a string representation of the translation request.
     * 
     * @return a string containing model and response format
     */
    @Override
    public String toString() {
        return "TranslationRequest{" +
                "model='" + model + '\'' +
                ", file=" + (file != null ? "provided" : "null") +
                ", url=" + (url != null ? "provided" : "null") +
                ", language='" + language + '\'' +
                ", responseFormat='" + responseFormat + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}
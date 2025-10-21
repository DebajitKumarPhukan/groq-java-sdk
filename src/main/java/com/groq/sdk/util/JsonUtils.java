package com.groq.sdk.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Provides JSON serialization and deserialization utilities for the Groq SDK.
 * Configures Jackson ObjectMapper with appropriate settings for API compatibility and error handling.
 * 
 * <p>This utility class provides:</p>
 * <ul>
 *   <li>JSON serialization of Java objects</li>
 *   <li>JSON deserialization to Java objects</li>
 *   <li>Pretty-printing for debugging</li>
 *   <li>JSON validation</li>
 * </ul>
 * 
 * @author Debajit Kumar Phukan
 * @since 21-Oct-2025
 * @version 1.0.0
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = createObjectMapper();
    
    /**
     * Creates and configures the ObjectMapper instance with Groq API compatible settings.
     * 
     * @return a configured ObjectMapper instance
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    
    /**
     * Gets the configured ObjectMapper instance.
     * 
     * @return the shared ObjectMapper instance
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    /**
     * Serializes an object to JSON string with proper error handling.
     * 
     * @param object the object to serialize
     * @return the JSON string representation, or null if object is null
     * @throws RuntimeException if serialization fails
     */
    public static String toJson(Object object) {
        try {
            if (object == null) {
                return null;
            }
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deserializes JSON string to object of specified class with proper error handling.
     * 
     * @param <T> the type of object to deserialize to
     * @param json the JSON string to deserialize
     * @param clazz the target class
     * @return the deserialized object, or null if json is null or empty
     * @throws RuntimeException if deserialization fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return null;
            }
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName() + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Pretty prints JSON string for debugging purposes.
     * 
     * @param object the object to pretty print
     * @return the pretty-printed JSON string, or null if object is null
     * @throws RuntimeException if pretty printing fails
     */
    public static String toPrettyJson(Object object) {
        try {
            if (object == null) {
                return null;
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to pretty print object: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if a string is valid JSON.
     * 
     * @param json the string to validate
     * @return true if the string is valid JSON, false otherwise
     */
    public static boolean isValidJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return false;
            }
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
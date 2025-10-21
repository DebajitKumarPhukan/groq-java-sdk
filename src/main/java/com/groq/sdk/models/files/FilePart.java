package com.groq.sdk.models.files;

import okhttp3.MediaType;

/**
 * Represents a file part in multipart form data requests.
 * Used for file uploads with filename, content, and media type information.
 * 
 * @author Debajit Kumar Phukan
 * @since 05-Oct-2025
 * @version 1.0.0
 */
public class FilePart {
    public final String filename;
    public final byte[] content;
    public final MediaType mediaType;
    
    /**
     * Constructs a new FilePart with the specified filename, content, and media type.
     * 
     * @param filename the filename
     * @param content the file content as byte array
     * @param mediaType the media type, or null for default
     */
    public FilePart(String filename, byte[] content, MediaType mediaType) {
        this.filename = filename;
        this.content = content;
        this.mediaType = mediaType;
    }
    
    /**
     * Constructs a new FilePart with the specified filename and content.
     * Uses default media type (application/octet-stream).
     * 
     * @param filename the filename
     * @param content the file content as byte array
     */
    public FilePart(String filename, byte[] content) {
        this(filename, content, null);
    }
    
    /**
     * Gets the filename.
     * 
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    
    /**
     * Gets the file content.
     * 
     * @return the file content as byte array
     */
    public byte[] getContent() {
        return content;
    }
    
    /**
     * Gets the media type of the file.
     * 
     * @return the media type, or null if not specified
     */
    public MediaType getMediaType() {
        return mediaType;
    }
    
    /**
     * Returns a string representation of the file part.
     * 
     * @return a string containing filename and content size
     */
    @Override
    public String toString() {
        return "FilePart{filename='" + filename + "', contentSize=" + (content != null ? content.length : 0) + "}";
    }
}
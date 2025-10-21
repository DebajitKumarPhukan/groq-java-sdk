package com.groq.sdk.models.files;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a file object in the Groq API.
 * Contains file metadata including size, purpose, and status information.
 * 
 * @author Debajit Kumar Phukan
 * @since 15-Sep-2025
 * @version 1.0.0
 * @see FileList
 * @see FileUploadRequest
 * @see FileDeleteResponse
 */
public class FileObject {
    private String id;
    private String object;
    private Long bytes;
    private Long createdAt;
    private String filename;
    private String purpose;
    private String status;
    private String statusDetails;
    
    /**
     * Default constructor.
     */
    public FileObject() {}
    
    /**
     * Constructs a new FileObject with the specified properties.
     * 
     * @param id the file ID
     * @param object the object type
     * @param bytes the file size in bytes
     * @param createdAt the creation timestamp
     * @param filename the file name
     * @param purpose the file purpose
     */
    public FileObject(String id, String object, Long bytes, Long createdAt, 
                     String filename, String purpose) {
        this.id = id;
        this.object = object;
        this.bytes = bytes;
        this.createdAt = createdAt;
        this.filename = filename;
        this.purpose = purpose;
    }
    
    /**
     * Gets the unique identifier for the file.
     * 
     * @return the file ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique identifier for the file.
     * 
     * @param id the file ID
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * Gets the object type (usually "file").
     * 
     * @return the object type
     */
    public String getObject() { 
        return object; 
    }
    
    /**
     * Sets the object type.
     * 
     * @param object the object type
     */
    public void setObject(String object) { 
        this.object = object; 
    }
    
    /**
     * Gets the file size in bytes.
     * 
     * @return the file size in bytes
     */
    public Long getBytes() { 
        return bytes; 
    }
    
    /**
     * Sets the file size in bytes.
     * 
     * @param bytes the file size in bytes
     */
    public void setBytes(Long bytes) { 
        this.bytes = bytes; 
    }
    
    /**
     * Gets the timestamp when the file was created.
     * 
     * @return the creation timestamp in seconds since epoch
     */
    @JsonProperty("created_at")
    public Long getCreatedAt() { 
        return createdAt; 
    }
    
    /**
     * Sets the timestamp when the file was created.
     * 
     * @param createdAt the creation timestamp in seconds since epoch
     */
    public void setCreatedAt(Long createdAt) { 
        this.createdAt = createdAt; 
    }
    
    /**
     * Gets the original filename.
     * 
     * @return the filename
     */
    public String getFilename() { 
        return filename; 
    }
    
    /**
     * Sets the original filename.
     * 
     * @param filename the filename
     */
    public void setFilename(String filename) { 
        this.filename = filename; 
    }
    
    /**
     * Gets the purpose of the file.
     * 
     * @return the file purpose (e.g., "fine-tune", "batch", "assistants")
     */
    public String getPurpose() { 
        return purpose; 
    }
    
    /**
     * Sets the purpose of the file.
     * 
     * @param purpose the file purpose
     */
    public void setPurpose(String purpose) { 
        this.purpose = purpose; 
    }
    
    /**
     * Gets the current status of the file.
     * 
     * @return the file status (e.g., "uploaded", "processed", "error")
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * Sets the current status of the file.
     * 
     * @param status the file status
     */
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    /**
     * Gets the detailed status information.
     * 
     * @return the status details, or null if no details
     */
    @JsonProperty("status_details")
    public String getStatusDetails() { 
        return statusDetails; 
    }
    
    /**
     * Sets the detailed status information.
     * 
     * @param statusDetails the status details
     */
    public void setStatusDetails(String statusDetails) { 
        this.statusDetails = statusDetails; 
    }
}
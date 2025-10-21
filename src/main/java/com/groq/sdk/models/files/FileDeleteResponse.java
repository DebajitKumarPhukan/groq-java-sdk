package com.groq.sdk.models.files;

/**
 * Represents the response from a file deletion operation.
 * Confirms whether the file was successfully deleted.
 * 
 * @author Debajit Kumar Phukan
 * @since 12-Sep-2025
 * @version 1.0.0
 * @see FileObject
 */
public class FileDeleteResponse {
    private String id;
    private String object;
    private Boolean deleted;
    
    /**
     * Default constructor.
     */
    public FileDeleteResponse() {}
    
    /**
     * Constructs a new FileDeleteResponse with the specified properties.
     * 
     * @param id the file ID
     * @param object the object type
     * @param deleted whether the file was deleted
     */
    public FileDeleteResponse(String id, String object, Boolean deleted) {
        this.id = id;
        this.object = object;
        this.deleted = deleted;
    }
    
    /**
     * Gets the unique identifier of the deleted file.
     * 
     * @return the file ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique identifier of the deleted file.
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
     * Gets whether the file was successfully deleted.
     * 
     * @return true if the file was deleted, false otherwise
     */
    public Boolean getDeleted() { 
        return deleted; 
    }
    
    /**
     * Sets whether the file was successfully deleted.
     * 
     * @param deleted true if the file was deleted, false otherwise
     */
    public void setDeleted(Boolean deleted) { 
        this.deleted = deleted; 
    }
}
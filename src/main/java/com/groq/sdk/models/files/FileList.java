package com.groq.sdk.models.files;

import java.util.List;

/**
 * Represents a list of files with pagination support.
 * Contains file objects and list metadata for file management.
 * 
 * @author Debajit Kumar Phukan
 * @since 21-Oct-2025
 * @version 1.0.0
 * @see FileObject
 */
public class FileList {
    private String object;
    private List<FileObject> data;
    
    /**
     * Default constructor.
     */
    public FileList() {}
    
    /**
     * Constructs a new FileList with the specified object type and data.
     * 
     * @param object the object type
     * @param data the list of file objects
     */
    public FileList(String object, List<FileObject> data) {
        this.object = object;
        this.data = data;
    }
    
    /**
     * Gets the object type (usually "list").
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
     * Gets the list of file objects.
     * 
     * @return the list of files
     * @see FileObject
     */
    public List<FileObject> getData() { 
        return data; 
    }
    
    /**
     * Sets the list of file objects.
     * 
     * @param data the list of files
     * @see FileObject
     */
    public void setData(List<FileObject> data) { 
        this.data = data; 
    }
}
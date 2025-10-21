package com.groq.sdk.models.files;

/**
 * Represents a request to upload a file to the Groq API.
 * Contains the file content, purpose, and filename for upload operations.
 * 
 * @author Debajit Kumar Phukan
 * @since 14-Sep-2025
 * @version 1.0.0
 * @see FileObject
 */
public class FileUploadRequest {
    private String file;
    private String purpose;
    private String filename;
    
    /**
     * Default constructor.
     */
    public FileUploadRequest() {}
    
    /**
     * Constructs a new FileUploadRequest with the specified file and purpose.
     * 
     * @param file the file content (base64 encoded or file path)
     * @param purpose the file purpose
     */
    public FileUploadRequest(String file, String purpose) {
        this.file = file;
        this.purpose = purpose;
    }
    
    /**
     * Constructs a new FileUploadRequest with the specified file, purpose, and filename.
     * 
     * @param file the file content (base64 encoded or file path)
     * @param purpose the file purpose
     * @param filename the filename
     */
    public FileUploadRequest(String file, String purpose, String filename) {
        this.file = file;
        this.purpose = purpose;
        this.filename = filename;
    }
    
    /**
     * Gets the file content (base64 encoded or file path).
     * 
     * @return the file content
     */
    public String getFile() { 
        return file; 
    }
    
    /**
     * Sets the file content (base64 encoded or file path).
     * 
     * @param file the file content
     */
    public void setFile(String file) { 
        this.file = file; 
    }
    
    /**
     * Gets the purpose of the file upload.
     * 
     * @return the file purpose (e.g., "fine-tune", "batch", "assistants")
     */
    public String getPurpose() { 
        return purpose; 
    }
    
    /**
     * Sets the purpose of the file upload.
     * 
     * @param purpose the file purpose
     */
    public void setPurpose(String purpose) { 
        this.purpose = purpose; 
    }
    
    /**
     * Gets the filename for the uploaded file.
     * 
     * @return the filename, or null if not specified
     */
    public String getFilename() { 
        return filename; 
    }
    
    /**
     * Sets the filename for the uploaded file.
     * 
     * @param filename the filename
     */
    public void setFilename(String filename) { 
        this.filename = filename; 
    }
}
package com.groq.sdk.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.files.FileDeleteResponse;
import com.groq.sdk.models.files.FileList;
import com.groq.sdk.models.files.FileObject;
import com.groq.sdk.models.files.FilePart;
import com.groq.sdk.models.files.FileUploadRequest;

import okhttp3.MediaType;
import okhttp3.MultipartBody;

/**
 * Provides access to file operations in the Groq API.
 * Handles file upload, retrieval, listing, and deletion with comprehensive error handling and multipart support.
 * 
 * @author Debajit Kumar Phukan
 * @since 04-Oct-2025
 * @version 1.0.0
 * @see GroqClient
 * @see FileObject
 * @see FileList
 * @see FileUploadRequest
 * @see FileDeleteResponse
 */
public class FilesResource {
    private final GroqClient client;
    
    /**
     * Constructs a new FilesResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public FilesResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Lists all available files.
     * 
     * @return the API response containing the list of files
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<FileList> list() {
        try {
            return client.get("/openai/v1/files", FileList.class, 
                             Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list files: " + e.getMessage(), e);
        }
    }
    
    /**
     * Uploads a file using multipart form data.
     * 
     * @param request the file upload request containing file content and purpose
     * @return the API response containing the uploaded file details
     * @throws IllegalArgumentException if request is null, purpose is empty, or file content is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<FileObject> upload(FileUploadRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("FileUploadRequest cannot be null");
            }
            if (request.getPurpose() == null || request.getPurpose().trim().isEmpty()) {
                throw new IllegalArgumentException("Purpose cannot be null or empty");
            }
            if (request.getFile() == null || request.getFile().trim().isEmpty()) {
                throw new IllegalArgumentException("File content cannot be null or empty");
            }
            
            MultipartBody multipartBody = createFileUploadMultipart(request);
            return client.executeMultipartRequest("/openai/v1/files", multipartBody, 
                                                 FileObject.class, Collections.emptyMap());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a specific file by ID.
     * 
     * @param fileId the unique identifier of the file to retrieve
     * @return the API response containing the file details
     * @throws IllegalArgumentException if file ID is null or empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<FileObject> retrieve(String fileId) {
        try {
            if (fileId == null || fileId.trim().isEmpty()) {
                throw new IllegalArgumentException("File ID cannot be null or empty");
            }
            
            return client.get("/openai/v1/files/" + fileId, FileObject.class, 
                             Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deletes a specific file by ID.
     * 
     * @param fileId the unique identifier of the file to delete
     * @return the API response confirming the deletion
     * @throws IllegalArgumentException if file ID is null or empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<FileDeleteResponse> delete(String fileId) {
        try {
            if (fileId == null || fileId.trim().isEmpty()) {
                throw new IllegalArgumentException("File ID cannot be null or empty");
            }
            
            return client.delete("/openai/v1/files/" + fileId, FileDeleteResponse.class, 
                                Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves the content of a specific file by ID.
     * 
     * @param fileId the unique identifier of the file whose content to retrieve
     * @return the API response containing the file content as string
     * @throws IllegalArgumentException if file ID is null or empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<String> retrieveContent(String fileId) {
        try {
            if (fileId == null || fileId.trim().isEmpty()) {
                throw new IllegalArgumentException("File ID cannot be null or empty");
            }
            
            return client.get("/openai/v1/files/" + fileId + "/content", String.class, 
                             Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file content: " + e.getMessage(), e);
        }
    }
    
    /**
     * Uploads string content as a file.
     * 
     * @param content the string content to upload
     * @param filename the name for the uploaded file
     * @param purpose the purpose of the file upload
     * @return the API response containing the uploaded file details
     * @throws IllegalArgumentException if content is empty, filename is empty, or purpose is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<FileObject> uploadStringContent(String content, String filename, String purpose) {
        try {
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty");
            }
            if (filename == null || filename.trim().isEmpty()) {
                throw new IllegalArgumentException("Filename cannot be null or empty");
            }
            if (purpose == null || purpose.trim().isEmpty()) {
                throw new IllegalArgumentException("Purpose cannot be null or empty");
            }
            
            FileUploadRequest request = new FileUploadRequest();
            request.setFile(content);
            request.setFilename(filename);
            request.setPurpose(purpose);
            
            return upload(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload string content: " + e.getMessage(), e);
        }
    }
    
    /**
     * Uploads JSON content as a file.
     * 
     * @param jsonContent the JSON content to upload
     * @param filename the base name for the JSON file (".json" extension will be added if missing)
     * @param purpose the purpose of the file upload
     * @return the API response containing the uploaded file details
     * @throws IllegalArgumentException if JSON content is empty
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<FileObject> uploadJson(String jsonContent, String filename, String purpose) {
        try {
            if (jsonContent == null || jsonContent.trim().isEmpty()) {
                throw new IllegalArgumentException("JSON content cannot be null or empty");
            }
            
            if (!filename.toLowerCase().endsWith(".json")) {
                filename = filename + ".json";
            }
            
            return uploadStringContent(jsonContent, filename, purpose);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload JSON content: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates multipart form data for file upload.
     * 
     * @param request the file upload request
     * @return the constructed MultipartBody for the file upload
     * @throws IOException if file reading fails or file content is empty
     */
    private MultipartBody createFileUploadMultipart(FileUploadRequest request) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        
        builder.addFormDataPart("purpose", request.getPurpose());
        
        byte[] fileContent;
        String filename = request.getFilename();
        
        if (request.getFile().startsWith("/") || request.getFile().contains(":\\")) {
            Path filePath = Paths.get(request.getFile());
            if (!Files.exists(filePath)) {
                throw new IOException("File does not exist: " + request.getFile());
            }
            fileContent = Files.readAllBytes(filePath);
            if (filename == null) {
                filename = filePath.getFileName().toString();
            }
        } else {
            fileContent = request.getFile().getBytes();
            if (filename == null) {
                filename = "uploaded_file";
            }
        }
        
        if (fileContent.length == 0) {
            throw new IOException("File content is empty");
        }
        
        MediaType mediaType = guessMediaType(filename);
        
        FilePart filePart = new FilePart(filename, fileContent, mediaType);
        Map<String, Object> formData = new HashMap<>();
        formData.put("purpose", request.getPurpose());
        formData.put("file", filePart);
        
        return client.createMultipartBody(formData);
    }
    
    /**
     * Guesses the media type based on file extension.
     * 
     * @param filename the filename to guess media type for
     * @return the guessed MediaType, or application/octet-stream as default
     */
    private MediaType guessMediaType(String filename) {
        if (filename.endsWith(".json")) return MediaType.parse("application/json");
        if (filename.endsWith(".txt")) return MediaType.parse("text/plain");
        if (filename.endsWith(".csv")) return MediaType.parse("text/csv");
        if (filename.endsWith(".pdf")) return MediaType.parse("application/pdf");
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return MediaType.parse("image/jpeg");
        if (filename.endsWith(".png")) return MediaType.parse("image/png");
        return MediaType.parse("application/octet-stream");
    }
}
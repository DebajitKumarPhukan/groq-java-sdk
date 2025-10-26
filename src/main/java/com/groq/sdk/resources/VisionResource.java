package com.groq.sdk.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.chat.ChatCompletion;
import com.groq.sdk.models.vision.VisionContentPart;
import com.groq.sdk.models.vision.VisionMessage;
import com.groq.sdk.models.vision.VisionRequest;

/**
 * Provides access to vision operations in the Groq API.
 * Handles image understanding and multimodal processing with comprehensive error handling.
 * 
 * <p><strong>Common Vision Models:</strong></p>
 * <ul>
 *   <li><code>meta-llama/llama-4-maverick-17b-128e-instruct</code> - High-performance vision model</li>
 *   <li><code>meta-llama/llama-4-scout-17b-16e-instruct</code> - Efficient vision model</li>
 * </ul>
 * 
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * VisionResource vision = client.vision();
 * 
 * // Using remote URL
 * VisionRequest request = vision.createVisionRequestWithUrl(
 *     "meta-llama/llama-4-maverick-17b-128e-instruct", 
 *     "https://example.com/image.jpg", 
 *     "Describe this image"
 * );
 * 
 * // Using local file
 * VisionRequest request = vision.createVisionRequestWithLocalImage(
 *     "meta-llama/llama-4-maverick-17b-128e-instruct", 
 *     "/path/to/image.jpg", 
 *     "Describe this image"
 * );
 * 
 * // Using image bytes
 * byte[] imageBytes = Files.readAllBytes(Paths.get("image.jpg"));
 * VisionRequest request = vision.createVisionRequestWithImageBytes(
 *     "meta-llama/llama-4-maverick-17b-128e-instruct", 
 *     imageBytes, 
 *     "image/jpeg",
 *     "Describe this image"
 * );
 * 
 * GroqResponse<ChatCompletion> response = vision.createCompletion(request);
 * }</pre>
 * 
 * @author Debajit Kumar Phukan
 * @since 25-Oct-2025
 * @version 2.3.0
 * @see GroqClient
 * @see VisionRequest
 * @see ChatCompletion
 */
public class VisionResource {
    private final GroqClient client;
    
    /**
     * Constructs a new VisionResource with the specified GroqClient.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    public VisionResource(GroqClient client) {
        this.client = client;
    }
    
    /**
     * Creates a vision completion with the specified request.
     * 
     * @param request the vision request containing model, messages, and parameters
     * @return the API response containing vision processing results
     * @throws IllegalArgumentException if request is null or validation fails
     * @throws RuntimeException if the API call fails
     */
    public GroqResponse<ChatCompletion> createCompletion(VisionRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("VisionRequest cannot be null");
            }
            if (request.getModel() == null || request.getModel().trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be null or empty");
            }
            if (request.getMessages() == null || request.getMessages().isEmpty()) {
                throw new IllegalArgumentException("Messages cannot be null or empty");
            }
            
            return client.post("/openai/v1/chat/completions", request, ChatCompletion.class, 
                              Collections.emptyMap(), Collections.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create vision completion: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a vision request with a remote image URL.
     * 
     * @param model the vision model to use
     * @param imageUrl the URL of the image to analyze
     * @param prompt the text prompt describing what to analyze
     * @return a configured VisionRequest for vision processing
     * @throws RuntimeException if request creation fails
     */
    public VisionRequest createVisionRequestWithUrl(String model, String imageUrl, String prompt) {
        try {
            VisionMessage message = createVisionMessageWithUrl(prompt, imageUrl);
            return new VisionRequest(model, Arrays.asList(message));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create vision request with URL: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a vision request with a local image file.
     * 
     * @param model the vision model to use
     * @param imagePath the path to the local image file
     * @param prompt the text prompt describing what to analyze
     * @return a configured VisionRequest for vision processing
     * @throws RuntimeException if request creation fails or image processing fails
     */
    public VisionRequest createVisionRequestWithLocalImage(String model, String imagePath, String prompt) {
        try {
        	Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);
            String mimeType = getMimeType(imagePath);
            
            return createVisionRequestWithImageBytes(model, imageBytes, mimeType, prompt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create vision request with local image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a vision request with image bytes.
     * 
     * @param model the vision model to use
     * @param imageBytes the image data as byte array
     * @param mimeType the MIME type of the image (e.g., "image/jpeg", "image/png")
     * @param prompt the text prompt describing what to analyze
     * @return a configured VisionRequest for vision processing
     * @throws RuntimeException if request creation fails or image processing fails
     */
    public VisionRequest createVisionRequestWithImageBytes(String model, byte[] imageBytes, String mimeType, String prompt) {
        try {
            if (imageBytes == null || imageBytes.length == 0) {
                throw new IllegalArgumentException("Image bytes cannot be null or empty");
            }
            if (mimeType == null || mimeType.trim().isEmpty()) {
                throw new IllegalArgumentException("MIME type cannot be null or empty");
            }
            
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String dataUrl = "data:" + mimeType + ";base64," + base64Image;
            
            VisionMessage message = createVisionMessageWithUrl(prompt, dataUrl);
            return new VisionRequest(model, Arrays.asList(message));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create vision request with image bytes: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a vision request with base64 encoded image string.
     * 
     * @param model the vision model to use
     * @param base64Image the base64 encoded image string
     * @param mimeType the MIME type of the image (e.g., "image/jpeg", "image/png")
     * @param prompt the text prompt describing what to analyze
     * @return a configured VisionRequest for vision processing
     * @throws RuntimeException if request creation fails
     */
    public VisionRequest createVisionRequestWithBase64Image(String model, String base64Image, String mimeType, String prompt) {
        try {
            if (base64Image == null || base64Image.trim().isEmpty()) {
                throw new IllegalArgumentException("Base64 image cannot be null or empty");
            }
            if (mimeType == null || mimeType.trim().isEmpty()) {
                throw new IllegalArgumentException("MIME type cannot be null or empty");
            }
            
            String dataUrl = "data:" + mimeType + ";base64," + base64Image;
            VisionMessage message = createVisionMessageWithUrl(prompt, dataUrl);
            return new VisionRequest(model, Arrays.asList(message));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create vision request with base64 image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a vision message with remote image URL.
     * 
     * @param text the text content
     * @param imageUrl the remote image URL
     * @return a VisionMessage with multimodal content
     */
    public VisionMessage createVisionMessageWithUrl(String text, String imageUrl) {
        VisionContentPart textPart = VisionContentPart.createTextPart(text);
        VisionContentPart imagePart = VisionContentPart.createImagePart(imageUrl);
        return new VisionMessage("user", Arrays.asList(textPart, imagePart));
    }
    
    /**
     * Encodes a local image file to base64 string.
     * 
     * @param imagePath the path to the image file
     * @return base64 encoded image string
     * @throws IOException if file reading fails
     * @throws IllegalArgumentException if image path is invalid
     */
    public String encodeImageToBase64(String imagePath) throws IOException {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Image path cannot be null or empty");
        }
        
        Path path = Paths.get(imagePath);
        if (!Files.exists(path) || !Files.isReadable(path)) {
            throw new IllegalArgumentException("Image file does not exist or is not readable: " + imagePath);
        }
        
        byte[] imageBytes = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    
    /**
     * Encodes image bytes to base64 string.
     * 
     * @param imageBytes the image data as byte array
     * @return base64 encoded image string
     * @throws IllegalArgumentException if image bytes are null or empty
     */
    public String encodeImageToBase64(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("Image bytes cannot be null or empty");
        }
        
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    
    /**
     * Determines the MIME type based on file extension.
     * 
     * @param imagePath the path to the image file
     * @return the MIME type string
     */
    private String getMimeType(String imagePath) {
        String fileName = imagePath.toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        } else {
            return "image/jpeg";
        }
    }
    
    /**
     * Gets the list of common vision models.
     * 
     * @return array of common vision model identifiers
     */
    public String[] getCommonVisionModels() {
        return new String[]{
            "meta-llama/llama-4-maverick-17b-128e-instruct", 
            "meta-llama/llama-4-scout-17b-16e-instruct"
        };
    }
}
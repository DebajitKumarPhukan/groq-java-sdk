# Groq Java SDK

A modern, type-safe Java SDK for interacting with the Groq API, providing seamless access to large language models with ultra-low latency inference.

## Overview

The Groq Java SDK is a comprehensive client library that enables Java developers to easily integrate with Groq's high-performance inference engine. It provides type-safe interfaces for all Groq API endpoints including chat completions, embeddings, audio processing, batch operations, and file management.

### Key Features

- 🚀 **High Performance**: Optimized for Groq's lightning-fast inference
- 🛡️ **Type-Safe**: Full type safety with comprehensive Java models
- 🔧 **Flexible Configuration**: Customizable timeouts, retries, and headers
- 🎯 **Comprehensive Coverage**: Support for all Groq API endpoints
- 🛠️ **Production Ready**: Built-in error handling, logging, and retry mechanisms
- 📦 **Zero Dependencies**: Minimal external dependencies

## Quick Start

### Installation

#### Maven Dependency

```xml
<dependency>
    <groupId>com.groq</groupId>
    <artifactId>groq-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Basic Usage

```java
import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.chat.ChatCompletionRequest;
import com.groq.sdk.models.chat.ChatMessage;
import java.util.List;

// Initialize client
GroqClient client = GroqClient.builder()
    .apiKey("your-api-key-here")
    .timeout(java.time.Duration.ofSeconds(30))
    .maxRetries(3)
    .build();

// Create chat completion
ChatMessage message = new ChatMessage("user", "Explain quantum computing in simple terms");
ChatCompletionRequest request = new ChatCompletionRequest("openai/gpt-oss-20b", List.of(message));
request.setMaxTokens(100);
request.setTemperature(0.7);

var response = client.chat().createCompletion(request);

if (response.isSuccessful()) {
    String content = response.getData().getChoices().get(0).getMessage().getContent();
    System.out.println("Response: " + content);
}
```
### Advanced Examples
#### Chat Completions with Multiple Models
```java
// Try multiple models with fallback
String[] models = {"openai/gpt-oss-20b", "llama-3.1-8b-instant", "qwen/qwen3-32b"};

for (String model : models) {
    try {
        ChatCompletionRequest request = new ChatCompletionRequest(model, messages);
        var response = client.chat().createCompletion(request);
        
        if (response.isSuccessful()) {
            // Process successful response
            break;
        }
    } catch (Exception e) {
        // Fallback to next model
        continue;
    }
}
```
#### Tool Calls and Function Calling
```java
import com.groq.sdk.models.chat.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Create function definitions
FunctionDefinition weatherFunction = new FunctionDefinition(
    "get_weather",
    "Get the current weather for a location",
    Map.of(
        "type", "object",
        "properties", Map.of(
            "location", Map.of("type", "string", "description", "The city and state"),
            "unit", Map.of("type", "string", "enum", Arrays.asList("celsius", "fahrenheit"))
        ),
        "required", Arrays.asList("location")
    )
);

// Create tools
ChatTool weatherTool = new ChatTool("function", weatherFunction);
ChatTool calculatorTool = new ChatTool("function", createCalculatorFunction());

// Create request with tools
ChatMessage message = new ChatMessage("user", "What's the weather in Tokyo and calculate 15 * 8?");
ChatCompletionRequest request = new ChatCompletionRequest("llama-3.3-70b-versatile", Arrays.asList(message));
request.setTools(Arrays.asList(weatherTool, calculatorTool));
request.setToolChoice("auto"); // Let model choose which tools to use

GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);

if (response.isSuccessful()) {
    ChatMessage assistantMessage = response.getData().getChoices().get(0).getMessage();
    
    if (assistantMessage.getToolCalls() != null) {
        // Handle tool calls
        for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
            String toolName = toolCall.getFunction().getName();
            String arguments = toolCall.getFunction().getArguments();
            
            // Execute the tool and get result
            String toolResult = executeTool(toolName, arguments);
            
            // Continue conversation with tool result
            ChatMessage toolMessage = ChatMessage.createToolMessage(toolCall.getId(), toolResult);
            // ... continue conversation
        }
    } else {
        // Direct response
        String content = assistantMessage.getContent();
        System.out.println("Response: " + content);
    }
}
```
#### Forced Tool Usage
```java
// Force the model to use a specific tool
ChatNamedToolChoice forcedChoice = new ChatNamedToolChoice("calculate");
request.setToolChoice(forcedChoice); // Must use the calculator tool
```
#### Embeddings Generation [Preview]
```java
import com.groq.sdk.models.embeddings.EmbeddingRequest;
import java.util.Arrays;
import java.util.List;

List<String> inputs = Arrays.asList(
    "The weather is nice today",
    "It's sunny outside",
    "I enjoy programming in Java"
);

EmbeddingRequest request = new EmbeddingRequest("text-embedding-ada-002", inputs);
var response = client.embeddings().create(request);

if (response.isSuccessful()) {
    System.out.println("Generated " + response.getData().getData().size() + " embeddings");
}
```
#### Audio Processing
```java
import com.groq.sdk.models.audio.SpeechRequest;
import com.groq.sdk.models.audio.TranscriptionRequest;

// Text-to-Speech
SpeechRequest speechRequest = new SpeechRequest("tts-1", "Hello world!", "alloy");
var speechResponse = client.audio().createSpeech(speechRequest);

// Audio Transcription
TranscriptionRequest transcriptionRequest = new TranscriptionRequest();
transcriptionRequest.setModel("whisper-large-v3");
transcriptionRequest.setFile("base64_audio_data");
var transcription = client.audio().createTranscription(transcriptionRequest);
```
### Project Structure
```java
groq-java-sdk/
├── src/main/java/com/groq/sdk/
│   ├── client/
│   │   └── GroqClient.java          # Main client class and entry point
│   ├── core/
│   │   └── BaseClient.java          # Base HTTP client with retry logic
│   ├── models/                      # Data models organized by feature
│   │   ├── chat/                    # Chat completion models
│   │   │   ├── ChatCompletion.java
│   │   │   ├── ChatCompletionRequest.java
│   │   │   ├── ChatMessage.java
│   │   │   ├── ChatChoice.java
│   │   │   ├── ChatTool.java        # Tool definitions
│   │   │   ├── ChatToolCall.java    # Tool call execution
│   │   │   ├── ChatNamedToolChoice.java # Forced tool usage
│   │   │   ├── FunctionDefinition.java # Function schemas
│   │   │   └── Usage.java
│   │   ├── embeddings/              # Embedding models
│   │   │   ├── EmbeddingRequest.java
│   │   │   ├── EmbeddingResponse.java
│   │   │   └── EmbeddingData.java
│   │   ├── audio/                   # Audio processing models
│   │   │   ├── SpeechRequest.java
│   │   │   ├── SpeechResponse.java
│   │   │   ├── TranscriptionRequest.java
│   │   │   └── Transcription.java
│   │   ├── batches/                 # Batch processing models
│   │   │   ├── Batch.java
│   │   │   ├── BatchList.java
│   │   │   ├── BatchCreateRequest.java
│   │   │   └── BatchRequestCounts.java
│   │   ├── files/                   # File management models
│   │   │   ├── FileObject.java
│   │   │   ├── FileList.java
│   │   │   ├── FileUploadRequest.java
│   │   │   ├── FileDeleteResponse.java
│   │   │   └── FilePart.java
│   │   ├── models/                  # Model management
│   │   │   ├── Model.java
│   │   │   └── ModelList.java
│   │   └── GroqResponse.java        # Generic API response wrapper
│   ├── resources/                   # API resource classes
│   │   ├── ChatResource.java        # Chat completion operations
│   │   ├── EmbeddingsResource.java  # Embedding operations
│   │   ├── AudioResource.java       # Audio operations
│   │   ├── BatchesResource.java     # Batch operations
│   │   ├── FilesResource.java       # File operations
│   │   └── ModelsResource.java      # Model operations
│   ├── exceptions/
│   │   └── GroqException.java       # Custom exception types
│   └── util/
│       └── JsonUtils.java           # JSON serialization utilities
├── examples/
│   └── Example.java                    # Comprehensive usage examples
└── pom.xml                          # Maven build configuration
```
### API Resources
#### Available Resources
* client.chat() - Chat completions and conversations with tool calling support

* client.embeddings() - Text embedding generation

* client.audio() - Speech synthesis and transcription

* client.batches() - Batch processing operations

* client.files() - File upload and management

* client.models() - Model information and listing

#### Tool Calling Features
* Automatic Tool Selection: Model chooses which tools to use based on context

* Parallel Tool Calls: Multiple tools can be called simultaneously

* Function Definitions: JSON Schema based function definitions

#### Configuration Options
```java
GroqClient client = GroqClient.builder()
    .apiKey("your-api-key")          // Required: Your Groq API key
    .baseUrl("https://api.groq.com") // Optional: Custom base URL
    .timeout(Duration.ofSeconds(30)) // Optional: Request timeout
    .maxRetries(3)                   // Optional: Maximum retry attempts
    .defaultHeader("X-Custom", "value") // Optional: Custom headers
    .build();
```
    
#### Error Handling
The SDK provides comprehensive error handling:

```java
try {
    var response = client.chat().createCompletion(request);
    if (response.isSuccessful()) {
        // Process successful response
    } else {
        System.err.println("API Error: " + response.getStatusCode());
    }
} catch (GroqException e) {
    System.err.println("Groq API Error: " + e.getMessage());
    System.err.println("Status Code: " + e.getStatusCode());
} catch (Exception e) {
    System.err.println("Unexpected error: " + e.getMessage());
}
```

#### Contributing
We welcome contributions from the community! Here's how you can help:

1. Fork the repository
```bash
git clone https://github.com/your-username/groq-java-sdk.git
cd groq-java-sdk
```
2. Build the project
```bash
mvn clean compile
```
3. Run tests
```bash
mvn test
```
### Contribution Guidelines
#####🐛 Reporting Bugs
* Open an issue with the "[ISSUE]" at the beginning of the subject line

* Use the GitHub issue tracker

* Include detailed reproduction steps

* Provide code examples and error logs

* Specify your Java version and environment

#####💡 Feature Requests
* Open an issue with the "[ENHANCEMENT]" at the beginning of the subject line

* Describe the use case and expected behavior

* Consider if it aligns with the SDK's scope

#####🔧 Code Contributions
* Follow the code style

* Use 4-space indentation
 
* Follow Java naming conventions
 
* Include Javadoc for public methods

* Write tests

* Add unit tests for new features

* Ensure all tests pass

* Maintain test coverage

* Update documentation

* Update README.md for new features

* Add Javadoc comments

* Include usage examples

* Submit Pull Request

* Create a descriptive PR title

* Link related issues

* Provide clear implementation details

#####📝 Code Review Process
* All PRs require review from maintainers

* Address review comments promptly

* Ensure CI checks pass

* Squash commits before merging

####Development Dependencies
* Java 21 or higher

* Maven 3.6+

* Groq API key (for integration tests)

####Running the Demo
```bash
export GROQ_API_KEY="your-api-key"
mvn compile exec:java -Dexec.mainClass="com.groq.sdk.examples.Example"
```
####License
This project is licensed under the Apache 2.0 License - see the LICENSE file for details.

####Support
📚 API Documentation

🐛 Report Issues

💬 Community Discussions

####Author
Project led and maintained by Debajit Kumar Phukan
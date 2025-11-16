package com.groq.sdk.examples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.audio.SpeechRequest;
import com.groq.sdk.models.audio.SpeechResponse;
import com.groq.sdk.models.audio.Transcription;
import com.groq.sdk.models.audio.TranscriptionRequest;
import com.groq.sdk.models.audio.Translation;
import com.groq.sdk.models.audio.TranslationRequest;
import com.groq.sdk.models.batches.Batch;
import com.groq.sdk.models.batches.BatchCreateRequest;
import com.groq.sdk.models.batches.BatchList;
import com.groq.sdk.models.chat.ChatChoice;
import com.groq.sdk.models.chat.ChatCompletion;
import com.groq.sdk.models.chat.ChatCompletionRequest;
import com.groq.sdk.models.chat.ChatMessage;
import com.groq.sdk.models.chat.ChatNamedToolChoice;
import com.groq.sdk.models.chat.ChatTool;
import com.groq.sdk.models.chat.ChatToolCall;
import com.groq.sdk.models.chat.FunctionDefinition;
import com.groq.sdk.models.embeddings.EmbeddingRequest;
import com.groq.sdk.models.embeddings.EmbeddingResponse;
import com.groq.sdk.models.files.FileList;
import com.groq.sdk.models.files.FileUploadRequest;
import com.groq.sdk.models.mcp.MCPCallOutput;
import com.groq.sdk.models.mcp.MCPListToolsOutput;
import com.groq.sdk.models.mcp.MCPTool;
import com.groq.sdk.models.mcp.MCPToolDefinition;
import com.groq.sdk.models.models.ModelList;
import com.groq.sdk.models.responses.MessageContent;
import com.groq.sdk.models.responses.MessageInput;
import com.groq.sdk.models.responses.MessageOutput;
import com.groq.sdk.models.responses.ReasoningConfig;
import com.groq.sdk.models.responses.ReasoningContent;
import com.groq.sdk.models.responses.ReasoningOutput;
import com.groq.sdk.models.responses.Response;
import com.groq.sdk.models.responses.ResponseOutput;
import com.groq.sdk.models.responses.ResponseRequest;
import com.groq.sdk.models.vision.VisionRequest;

/**
 * Main class demonstrating the usage of Groq Java SDK.
 * Provides examples for all available functionalities including chat completions,
 * embeddings, model listing, tool calls, and error handling with fallbacks.
 * 
 * <p><strong>Note:</strong> This class is intended for demonstration purposes
 * and should be excluded from production JAR builds.</p>
 * 
 * @author Debajit Kumar Phukan
 * @version 1.2.0
 * @see GroqClient
 */
public class Example {
    private static final String DEFAULT_MODEL = "openai/gpt-oss-20b";
    private static final String EMBEDDING_MODEL = "text-embedding-ada-002";
    private static final String TOOL_MODEL = "llama-3.3-70b-versatile";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // Audio output directory
    private static final String AUDIO_OUTPUT_DIR = "src/main/resources/output";
    
    // Mock weather data for demonstration
    private static final Map<String, WeatherData> WEATHER_DATA = Map.of(
        "san francisco", new WeatherData(18.0, "celsius", "partly cloudy", 65, 15.2),
        "new york", new WeatherData(22.0, "celsius", "sunny", 55, 10.5),
        "london", new WeatherData(12.0, "celsius", "rainy", 85, 8.3),
        "tokyo", new WeatherData(25.0, "celsius", "clear", 60, 12.7),
        "sydney", new WeatherData(28.0, "celsius", "sunny", 45, 20.1)
    );
          
    public static void main(String[] args) {
        try {
            System.out.println("=== Groq Java SDK Demo ===");            
            
            // Create audio output directory
            createAudioOutputDirectory();
            
            GroqClient client = initializeClient();
            
            // Demonstrate all functionalities
            demonstrateChatCompletions(client);
            demonstrateReasoning(client);
            demonstrateReasoningWithTools(client);
            demonstrateVisionOperations(client);
            demonstrateToolCalls(client);
            demonstrateEmbeddings(client);
            demonstrateModelListing(client);
            demonstrateAudioOperations(client);
            demonstrateBatchOperations(client);
            demonstrateFileOperations(client);
            demonstrateErrorHandling(client);
            demonstrateResponseAPI(client);
            demonstrateMCPOperations(client);
            demonstrateSimpleMCP(client);
            demonstrateMCPErrorHandling(client);
            
            // Interactive demo
            runInteractiveDemo(client);            
        } catch (Exception e) {
            System.err.println("Fatal error in demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates the audio output directory if it doesn't exist.
     */
    private static void createAudioOutputDirectory() {
        try {
            Path outputPath = Paths.get(AUDIO_OUTPUT_DIR);
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
                System.out.println("* Created audio output directory: " + outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("x Failed to create audio output directory: " + e.getMessage());
        }
    }
    
    /**
     * Saves audio data to a file in the output directory.
     * 
     * @param audioData the audio data bytes
     * @param filename the filename to save as
     * @param format the audio format (mp3, wav, etc.)
     * @return the full path to the saved file, or null if failed
     */
    private static String saveAudioToFile(byte[] audioData, String filename, String format) {
        try {
            // Ensure filename has proper extension
            if (!filename.toLowerCase().endsWith("." + format.toLowerCase())) {
                filename = filename + "." + format;
            }
            
            Path filePath = Paths.get(AUDIO_OUTPUT_DIR, filename);
            
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(audioData);
            }
            
            System.out.println("* Audio saved to: " + filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
            
        } catch (IOException e) {
            System.err.println("x Failed to save audio file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Generates a timestamped filename for audio files.
     * 
     * @param prefix the filename prefix
     * @param format the audio format
     * @return a timestamped filename
     */
    private static String generateTimestampedFilename(String prefix, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
        String timestamp = LocalDateTime.now().format(formatter);
        return prefix + "_" + timestamp + "." + format;
    }
    
    /**
     * Initializes the GroqClient with proper error handling and fallbacks.
     * 
     * @return a configured GroqClient instance
     * @throws RuntimeException if client initialization fails
     */
    private static GroqClient initializeClient() {
        try {
            String apiKey = System.getenv("GROQ_API_KEY");
            if (apiKey == null || apiKey.trim().isEmpty()) {
                System.err.println("Warning: GROQ_API_KEY environment variable not set.");
                System.err.println("Please set GROQ_API_KEY to use the Groq API.");
                System.err.println("Some demo features may not work without a valid API key.");
            }
            
            return GroqClient.builder()
                    .apiKey(apiKey)
                    .timeout(java.time.Duration.ofSeconds(30))
                    .maxRetries(3)
                    .build();
                    
        } catch (Exception e) {
            System.err.println("Failed to initialize GroqClient: " + e.getMessage());
            throw new RuntimeException("Client initialization failed", e);
        }
    }
    
    /**
     * Demonstrates chat completion functionality with multiple models and fallbacks.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateChatCompletions(GroqClient client) {
        System.out.println("\n=== Chat Completions Demo ===");
        
        String[] modelsToTry = {DEFAULT_MODEL, "llama-3.1-8b-instant", "qwen/qwen3-32b"};
        
        for (String model : modelsToTry) {
            try {
                System.out.println("Trying model: " + model);
                
                ChatMessage message = new ChatMessage("user", "Explain quantum computing in one sentence");
                ChatCompletionRequest request = new ChatCompletionRequest(model, Arrays.asList(message));
                request.setMaxTokens(100);
                request.setTemperature(0.7);
                
                GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
                
                if (response.isSuccessful()) {
                    String content = response.getData().getChoices().get(0).getMessage().getContent();
                    System.out.println("* " + model + ": " + content);
                    break; // Success, no need to try other models
                } else {
                    System.err.println("x " + model + " failed with status: " + response.getStatusCode());
                }
                
            } catch (Exception e) {
                System.err.println("x " + model + " error: " + e.getMessage());
                // Continue to next model
            }
        }
    }
    
    /**
     * Demonstrates tool call functionality with function definitions and tool choices.
     * Shows both automatic tool selection and forced tool usage.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateToolCalls(GroqClient client) {
        System.out.println("\n=== Tool Calls Demo ===");
        
        try {
            // Create function definitions for various tools
            FunctionDefinition weatherFunction = createWeatherFunction();
            FunctionDefinition calculatorFunction = createCalculatorFunction();
            FunctionDefinition timeFunction = createTimeFunction();
            
            // Create tools from function definitions
            ChatTool weatherTool = new ChatTool("function", weatherFunction);
            ChatTool calculatorTool = new ChatTool("function", calculatorFunction);
            ChatTool timeTool = new ChatTool("function", timeFunction);
            
            List<ChatTool> tools = Arrays.asList(weatherTool, calculatorTool, timeTool);
            
            // Demo 1: Auto tool selection
            System.out.println("1. Auto tool selection:");
            demonstrateAutoToolSelection(client, tools);
            
            // Demo 2: Forced tool usage
            System.out.println("\n2. Forced tool usage:");
            demonstrateForcedToolUsage(client, tools);
            
            // Demo 3: Tool call with response (using real execution)
            System.out.println("\n3. Tool call with REAL execution:");
            demonstrateToolCallWithRealExecution(client, tools);
            
        } catch (Exception e) {
            System.err.println("x Tool calls demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates automatic tool selection where the model chooses which tool to use.
     */
    private static void demonstrateAutoToolSelection(GroqClient client, List<ChatTool> tools) {
        try {
            ChatMessage message = new ChatMessage("user", "What's the weather in San Francisco and what time is it there?");
            ChatCompletionRequest request = new ChatCompletionRequest(TOOL_MODEL, Arrays.asList(message));
            request.setTools(tools);
            request.setToolChoice("auto");
            request.setMaxTokens(500);
            
            GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
            
            if (response.isSuccessful()) {
                ChatCompletion completion = response.getData();
                ChatMessage assistantMessage = completion.getChoices().get(0).getMessage();
                
                if (assistantMessage.getToolCalls() != null && !assistantMessage.getToolCalls().isEmpty()) {
                    System.out.println("* Model decided to use tools:");
                    for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
                        System.out.println("  - Tool: " + toolCall.getFunction().getName());
                        System.out.println("  - Arguments: " + toolCall.getFunction().getArguments());
                    }
                } else {
                    System.out.println("* Model chose to respond directly: " + 
                                     (assistantMessage.getContent() != null ? assistantMessage.getContent() : "[No content]"));
                }
            } else {
                System.err.println("x Auto tool selection failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Auto tool selection error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates forced tool usage where a specific tool must be used.
     */
    private static void demonstrateForcedToolUsage(GroqClient client, List<ChatTool> tools) {
        try {
            ChatMessage message = new ChatMessage("user", "Calculate 15 * 8");
            ChatCompletionRequest request = new ChatCompletionRequest(TOOL_MODEL, Arrays.asList(message));
            request.setTools(tools);
            
            // Force the model to use the calculator tool
            ChatNamedToolChoice forcedChoice = new ChatNamedToolChoice("calculate");
            request.setToolChoice(forcedChoice);
            request.setMaxTokens(300);
            
            GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
            
            if (response.isSuccessful()) {
                ChatMessage assistantMessage = response.getData().getChoices().get(0).getMessage();
                
                if (assistantMessage.getToolCalls() != null && !assistantMessage.getToolCalls().isEmpty()) {
                    System.out.println("* Model used forced tool:");
                    for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
                        System.out.println("  - Tool: " + toolCall.getFunction().getName());
                        System.out.println("  - Arguments: " + toolCall.getFunction().getArguments());
                    }
                } else {
                    System.out.println("* Model responded directly: " + 
                                     (assistantMessage.getContent() != null ? assistantMessage.getContent() : "[No content]"));
                }
            } else {
                System.err.println("x Forced tool usage failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Forced tool usage error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates a complete tool call flow with REAL tool execution.
     */
    private static void demonstrateToolCallWithRealExecution(GroqClient client, List<ChatTool> tools) {
        try {
            // First call: Model decides to use a tool
            ChatMessage userMessage = new ChatMessage("user", "What's the temperature in Tokyo?");
            ChatCompletionRequest firstRequest = new ChatCompletionRequest(TOOL_MODEL, Arrays.asList(userMessage));
            firstRequest.setTools(tools);
            firstRequest.setToolChoice("auto");
            firstRequest.setMaxTokens(500);
            
            GroqResponse<ChatCompletion> firstResponse = client.chat().createCompletion(firstRequest);
            
            if (firstResponse.isSuccessful()) {
                ChatMessage assistantMessage = firstResponse.getData().getChoices().get(0).getMessage();
                
                if (assistantMessage.getToolCalls() != null && !assistantMessage.getToolCalls().isEmpty()) {
                    System.out.println("* Model requested tool call:");
                    
                    // Simulate tool execution and create tool response
                    for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
                        String toolName = toolCall.getFunction().getName();
                        System.out.println("  - Tool called: " + toolName);
                        
                        // Use REAL tool execution
                        String toolResponse = realToolExecution(toolName, toolCall.getFunction().getArguments());
                        
                        // Create tool message with response
                        ChatMessage toolMessage = ChatMessage.createToolMessage(
                            toolCall.getId(), 
                            toolResponse
                        );
                        
                        // Second call: Send tool response back to model
                        List<ChatMessage> conversation = Arrays.asList(
                            userMessage,
                            assistantMessage,
                            toolMessage
                        );
                        
                        ChatCompletionRequest secondRequest = new ChatCompletionRequest(TOOL_MODEL, conversation);
                        secondRequest.setMaxTokens(300);
                        
                        GroqResponse<ChatCompletion> secondResponse = client.chat().createCompletion(secondRequest);
                        
                        if (secondResponse.isSuccessful()) {
                            String finalResponse = secondResponse.getData().getChoices().get(0).getMessage().getContent();
                            System.out.println("* Final response: " + finalResponse);
                        } else {
                            System.err.println("x Second request failed with status: " + secondResponse.getStatusCode());
                        }
                    }
                } else {
                    System.out.println("* Model responded directly without tools: " + 
                                     (assistantMessage.getContent() != null ? assistantMessage.getContent() : "[No content]"));
                }
            } else {
                System.err.println("x First request failed with status: " + firstResponse.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Tool call with real execution error: " + e.getMessage());
        }
    }
    
    /**
     * Creates a weather function definition.
     */
    private static FunctionDefinition createWeatherFunction() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        parameters.put("properties", Map.of(
            "location", Map.of(
                "type", "string",
                "description", "The city and state, e.g. San Francisco, CA"
            ),
            "unit", Map.of(
                "type", "string",
                "enum", Arrays.asList("celsius", "fahrenheit"),
                "description", "The temperature unit to use"
            )
        ));
        parameters.put("required", Arrays.asList("location"));
        
        return new FunctionDefinition(
            "get_weather",
            "Get the current weather for a location",
            parameters
        );
    }
    
    /**
     * Creates a calculator function definition.
     */
    private static FunctionDefinition createCalculatorFunction() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        parameters.put("properties", Map.of(
            "expression", Map.of(
                "type", "string",
                "description", "The mathematical expression to calculate, e.g. 2 + 2 * 3"
            )
        ));
        parameters.put("required", Arrays.asList("expression"));
        
        return new FunctionDefinition(
            "calculate",
            "Evaluate a mathematical expression",
            parameters
        );
    }
    
    /**
     * Creates a time function definition.
     */
    private static FunctionDefinition createTimeFunction() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        parameters.put("properties", Map.of(
            "location", Map.of(
                "type", "string",
                "description", "The city and timezone, e.g. New York, America/New_York"
            )
        ));
        parameters.put("required", Arrays.asList("location"));
        
        return new FunctionDefinition(
            "get_current_time",
            "Get the current time for a location",
            parameters
        );
    }
    
    /**
     * Simulates tool execution for demonstration purposes.
     */
    private static String simulateToolExecution(String toolName, String arguments) {
        switch (toolName) {
            case "get_weather":
                return "{\"temperature\": 22, \"unit\": \"celsius\", \"conditions\": \"sunny\", \"humidity\": 65}";
            case "calculate":
                return "{\"result\": 42, \"expression\": \"7 * 6\"}";
            case "get_current_time":
                return "{\"time\": \"2024-01-15T14:30:00+09:00\", \"timezone\": \"Asia/Tokyo\"}";
            default:
                return "{\"error\": \"Tool not found\"}";
        }
    }
    
    /**
     * REAL tool execution that actually invokes Java methods.
     * 
     * @param toolName the name of the tool to execute
     * @param arguments JSON string containing the tool arguments
     * @return JSON string with the execution result
     */
    private static String realToolExecution(String toolName, String arguments) {
        try {
            System.out.println("    Executing " + toolName + " with arguments: " + arguments);
            
            switch (toolName) {
                case "get_weather":
                    return getWeather(arguments);
                case "calculate":
                    return calculate(arguments);
                case "get_current_time":
                    return getCurrentTime(arguments);
                default:
                    return "{\"error\": \"Unknown tool: " + toolName + "\"}";
            }
        } catch (Exception e) {
            System.err.println("Error executing tool " + toolName + ": " + e.getMessage());
            return "{\"error\": \"Tool execution failed: " + e.getMessage() + "\"}";
        }
    }
    
    /**
     * REAL weather implementation that returns actual weather data.
     */
    private static String getWeather(String arguments) throws Exception {
    	System.out.println("Real getWeather() executed.");
        Map<String, Object> params = objectMapper.readValue(arguments, new TypeReference<Map<String, Object>>() {});
        String location = ((String) params.get("location")).toLowerCase();
        String unit = params.containsKey("unit") ? (String) params.get("unit") : "celsius";
        
        WeatherData weather = WEATHER_DATA.get(location);
        if (weather == null) {
            // Generate random weather for unknown locations
            double temp = 15 + Math.random() * 20; // 15-35°C
            String[] conditions = {"sunny", "partly cloudy", "cloudy", "rainy", "stormy"};
            String condition = conditions[(int) (Math.random() * conditions.length)];
            int humidity = 40 + (int) (Math.random() * 40); // 40-80%
            double windSpeed = 5 + Math.random() * 15; // 5-20 km/h
            
            weather = new WeatherData(temp, "celsius", condition, humidity, windSpeed);
        }
        
        // Convert temperature if needed
        double temperature = weather.temperature;
        if ("fahrenheit".equals(unit) && "celsius".equals(weather.unit)) {
            temperature = (weather.temperature * 9/5) + 32;
        } else if ("celsius".equals(unit) && "fahrenheit".equals(weather.unit)) {
            temperature = (weather.temperature - 32) * 5/9;
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("temperature", Math.round(temperature * 10.0) / 10.0);
        result.put("unit", unit);
        result.put("conditions", weather.conditions);
        result.put("humidity", weather.humidity);
        result.put("wind_speed", Math.round(weather.windSpeed * 10.0) / 10.0);
        result.put("location", location);
        
        return objectMapper.writeValueAsString(result);
    }
    
    /**
     * REAL calculator implementation that evaluates mathematical expressions using pure Java.
     */
    private static String calculate(String arguments) throws Exception {
    	System.out.println("Real calculate() executed.");
        Map<String, Object> params = objectMapper.readValue(arguments, new TypeReference<Map<String, Object>>() {});
        String expression = (String) params.get("expression");
        
        try {
            if (!isValidExpression(expression)) {
                throw new SecurityException("Invalid characters in expression: " + expression);
            }
            
            double result = evaluateExpression(expression);
            
            Map<String, Object> response = new HashMap<>();
            response.put("result", result);
            response.put("expression", expression);
            response.put("evaluated_by", "Java Expression Parser");
            
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid expression: " + expression);
            response.put("message", e.getMessage());
            return objectMapper.writeValueAsString(response);
        }
    }
    
    /**
     * Validates if the expression contains only allowed characters.
     */
    private static boolean isValidExpression(String expression) {
        // Remove spaces for validation
        String cleanExpr = expression.replaceAll("\\s+", "");
        
        // Check if all characters are valid
        for (char c : cleanExpr.toCharArray()) {
            if (!(Character.isDigit(c) || c == '.' || c == '+' || c == '-' || c == '*' || c == '/' || 
                  c == '(' || c == ')' || c == ' ')) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Evaluates a mathematical expression using pure Java (no ScriptEngine).
     * Supports +, -, *, /, and parentheses with proper operator precedence.
     */
    private static double evaluateExpression(String expression) {
        // Remove all spaces
        String expr = expression.replaceAll("\\s+", "");
        
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            
            if (Character.isDigit(c) || c == '.') {
                // Parse the entire number
                StringBuilder numBuilder = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    numBuilder.append(expr.charAt(i));
                    i++;
                }
                i--;
                
                try {
                    numbers.push(Double.parseDouble(numBuilder.toString()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number: " + numBuilder.toString());
                }
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop();
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + c);
            }
        }
        
        // Apply remaining operations
        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }
        
        if (numbers.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        
        return numbers.pop();
    }
    
    /**
     * Checks if a character is a valid operator.
     */
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    /**
     * Checks operator precedence.
     */
    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }
    
    /**
     * Applies a mathematical operation to two numbers.
     */
    private static double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    
    /**
     * REAL time implementation that returns current time for locations.
     */
    private static String getCurrentTime(String arguments) throws Exception {
    	System.out.println("Real getCurrentTime() executed.");
        Map<String, Object> params = objectMapper.readValue(arguments,  new TypeReference<Map<String, Object>>() {});
        String location = ((String) params.get("location")).toLowerCase();
        
        // Map locations to timezones
        Map<String, String> timezoneMap = Map.of(
            "new york", "America/New_York",
            "san francisco", "America/Los_Angeles", 
            "london", "Europe/London",
            "tokyo", "Asia/Tokyo",
            "sydney", "Australia/Sydney",
            "paris", "Europe/Paris",
            "berlin", "Europe/Berlin",
            "mumbai", "Asia/Kolkata",
            "beijing", "Asia/Shanghai",
            "moscow", "Europe/Moscow"
        );
        
        String timezone = timezoneMap.getOrDefault(location, "UTC");
        
        try {
            LocalDateTime now = LocalDateTime.now(ZoneId.of(timezone));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            Map<String, Object> response = new HashMap<>();
            response.put("time", now.format(formatter));
            response.put("timezone", timezone);
            response.put("location", location);
            response.put("iso_format", now.toString());
            
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            // Fallback to system time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            Map<String, Object> response = new HashMap<>();
            response.put("time", now.format(formatter));
            response.put("timezone", ZoneId.systemDefault().toString());
            response.put("location", "system_default");
            response.put("iso_format", now.toString());
            response.put("note", "Requested location not found, using system time");
            
            return objectMapper.writeValueAsString(response);
        }
    }
    
    /**
     * Data class for weather information.
     */
    private static class WeatherData {
        final double temperature;
        final String unit;
        final String conditions;
        final int humidity;
        final double windSpeed;
        
        WeatherData(double temperature, String unit, String conditions, int humidity, double windSpeed) {
            this.temperature = temperature;
            this.unit = unit;
            this.conditions = conditions;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
        }
    }
    
    /**
     * Shows fallback models when API call fails.
     */
    private static void showFallbackModels() {
        System.out.println("Using fallback model list:");
        String[] fallbackModels = {
            "openai/gpt-oss-20b",
            "llama-3.1-8b-instant", 
            "qwen/qwen3-32b",
            "llama-3.3-70b-versatile",
            "text-embedding-ada-002"
        };
        
        for (String model : fallbackModels) {
            System.out.println("  - " + model);
        }
    }
    
    /**
     * Demonstrates embeddings functionality with error handling.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateEmbeddings(GroqClient client) {
        System.out.println("\n=== Embeddings Demo ===");
        
        try {
            List<String> inputs = Arrays.asList(
                "The weather is nice today",
                "It's sunny outside",
                "I enjoy programming in Java"
            );
            
            EmbeddingRequest request = new EmbeddingRequest(EMBEDDING_MODEL, inputs);
            
            GroqResponse<EmbeddingResponse> response = client.embeddings().create(request);
            
            if (response.isSuccessful()) {
                EmbeddingResponse embeddingResponse = response.getData();
                System.out.println("* Generated " + embeddingResponse.getData().size() + " embeddings");
                System.out.println("* Token usage: " + embeddingResponse.getUsage().getTotalTokens());
                
                // Show sample of first embedding
                List<Double> firstEmbedding = embeddingResponse.getData().get(0).getEmbedding();
                System.out.println("* First embedding sample: " + 
                                 firstEmbedding.subList(0, Math.min(3, firstEmbedding.size())));
            } else {
                System.err.println("x Embeddings failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Embeddings error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates model listing with comprehensive error handling.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateModelListing(GroqClient client) {
        System.out.println("\n=== Model Listing Demo ===");
        
        try {
            GroqResponse<ModelList> response = client.models().list();
            
            if (response.isSuccessful()) {
                ModelList modelList = response.getData();
                System.out.println("* Available models (" + modelList.getData().size() + "):");
                
                modelList.getData().stream()
                    .limit(5) // Show first 5 models
                    .forEach(model -> 
                        System.out.println("  - " + model.getId() + 
                                         " (created: " + model.getCreated() + ")")
                    );
                
                if (modelList.getData().size() > 5) {
                    System.out.println("  ... and " + (modelList.getData().size() - 5) + " more");
                }
            } else {
                System.err.println("x Model listing failed with status: " + response.getStatusCode());
                showFallbackModels();
            }
            
        } catch (Exception e) {
            System.err.println("x Model listing error: " + e.getMessage());
            showFallbackModels();
        }
    }
       
    /**
     * Demonstrates comprehensive error handling scenarios.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateErrorHandling(GroqClient client) {
        System.out.println("\n=== Error Handling Demo ===");
        
        // Test with invalid model
        try {
            ChatMessage message = new ChatMessage("user", "Hello");
            ChatCompletionRequest request = new ChatCompletionRequest("invalid-model", Arrays.asList(message));
            
            GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
            
            if (!response.isSuccessful()) {
                System.out.println("* Properly handled invalid model error: HTTP " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.out.println("* Properly caught exception for invalid model: " + e.getClass().getSimpleName());
        }
        
        // Test with empty API key scenario
        try {
            GroqClient invalidClient = GroqClient.builder()
                    .apiKey("")
                    .build();
                    
            GroqResponse<ModelList> response = invalidClient.models().list();
            System.out.println("x Unexpected success with empty API key");
            
        } catch (Exception e) {
            System.out.println("* Properly handled empty API key: " + e.getMessage());
        }
        
        // Test with invalid tool configuration
        try {
            ChatMessage message = new ChatMessage("user", "Hello");
            ChatCompletionRequest request = new ChatCompletionRequest(TOOL_MODEL, Arrays.asList(message));
            
            // Invalid tool without function definition
            ChatTool invalidTool = new ChatTool();
            invalidTool.setType("function");
            // Missing function definition - this should cause an error
            request.setTools(Arrays.asList(invalidTool));
            
            GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
            System.out.println("x Unexpected success with invalid tool configuration");
            
        } catch (Exception e) {
            System.out.println("* Properly handled invalid tool configuration: " + e.getClass().getSimpleName());
        }
    }
    
    /**
     * Runs an interactive demo allowing user input.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void runInteractiveDemo(GroqClient client) {
        System.out.println("\n=== Interactive Demo ===");
        System.out.println("Enter your messages (type 'quit' to exit):");
        System.out.println("Special commands:");
        System.out.println("  'tools on' - Enable tool calls");
        System.out.println("  'tools off' - Disable tool calls");
        System.out.println("  'reasoning on' - Enable reasoning");
        System.out.println("  'reasoning off' - Disable reasoning"); // NEW
        System.out.println("  'real tools' - Use real tool execution (default: simulated)");
        System.out.println("  'sim tools' - Use simulated tool execution");
        
        Scanner scanner = new Scanner(System.in);
        boolean toolsEnabled = false;
        boolean reasoningEnabled = false; // NEW
        boolean useRealTools = false;
        List<ChatTool> interactiveTools = null;
        
        // Initialize tools for interactive demo
        FunctionDefinition weatherFunction = createWeatherFunction();
        FunctionDefinition calculatorFunction = createCalculatorFunction();
        interactiveTools = Arrays.asList(
            new ChatTool("function", weatherFunction),
            new ChatTool("function", calculatorFunction)
        );
        
        while (true) {
            System.out.print("\nYou: ");
            String userInput = scanner.nextLine().trim();
            
            if (userInput.equalsIgnoreCase("quit")) {
                break;
            }
            
            if (userInput.equalsIgnoreCase("tools on")) {
                toolsEnabled = true;
                System.out.println("AI: Tool calls enabled. I can now help with weather and calculations.");
                continue;
            }
            
            if (userInput.equalsIgnoreCase("tools off")) {
                toolsEnabled = false;
                System.out.println("AI: Tool calls disabled.");
                continue;
            }
            
            if (userInput.equalsIgnoreCase("reasoning on")) { // NEW
                reasoningEnabled = true;
                System.out.println("AI: Reasoning enabled. I will show my thought process.");
                continue;
            }
            
            if (userInput.equalsIgnoreCase("reasoning off")) { // NEW
                reasoningEnabled = false;
                System.out.println("AI: Reasoning disabled.");
                continue;
            }
            
            if (userInput.equalsIgnoreCase("real tools")) {
                useRealTools = true;
                System.out.println("AI: Real tool execution enabled.");
                continue;
            }
            
            if (userInput.equalsIgnoreCase("sim tools")) {
                useRealTools = false;
                System.out.println("AI: Simulated tool execution enabled.");
                continue;
            }
            
            if (userInput.isEmpty()) {
                continue;
            }
            
            try {
                ChatMessage message = new ChatMessage("user", userInput);
                ChatCompletionRequest request = new ChatCompletionRequest(
                    toolsEnabled ? TOOL_MODEL : DEFAULT_MODEL, 
                    Arrays.asList(message)
                );
                request.setMaxTokens(300);
                request.setTemperature(0.7);
                
                if (toolsEnabled) {
                    request.setTools(interactiveTools);
                    request.setToolChoice("auto");
                }
                
                if (reasoningEnabled) { // NEW
                    request.setIncludeReasoning(true);
//                    request.setReasoningFormat("parsed");
                }
                
                System.out.print("AI: ");
                GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
                
                if (response.isSuccessful()) {
                    ChatMessage assistantMessage = response.getData().getChoices().get(0).getMessage();
                    
                    // NEW: Display reasoning if available
                    if (reasoningEnabled && assistantMessage.getReasoning() != null && 
                        !assistantMessage.getReasoning().isEmpty()) {
                        System.out.println("[Reasoning: " + assistantMessage.getReasoning() + "]");
                    }
                    
                    if (toolsEnabled && assistantMessage.getToolCalls() != null && 
                        !assistantMessage.getToolCalls().isEmpty()) {
                        
                        System.out.println("[I need to use a tool to answer your question]");
                        for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
                            String toolName = toolCall.getFunction().getName();
                            String arguments = toolCall.getFunction().getArguments();
                            
                            System.out.println("  Using " + toolName + " with arguments: " + arguments);
                            
                            // Use real or simulated tool execution based on user preference
                            String toolResult = useRealTools ? 
                                realToolExecution(toolName, arguments) : 
                                simulateToolExecution(toolName, arguments);
                            
                            System.out.println("  Tool result: " + toolResult);
                            
                            // In a real application, you would make another API call with the tool response
                            System.out.println("  [In a real app, I would continue the conversation with this tool result]");
                        }
                    } else {
                        System.out.println(assistantMessage.getContent() != null ? assistantMessage.getContent() : "[No content]");
                    }
                } else {
                    System.out.println("[Sorry, I encountered an error. Please try again.]");
                }
                
            } catch (Exception e) {
                System.out.println("[Sorry, I'm having trouble responding right now. Error: " + e.getMessage() + "]");
            }
        }
        
        scanner.close();
        System.out.println("Demo completed. Thank you!");
    }
    
    /**
     * Demonstrates the latest audio operations including text-to-speech and speech-to-text.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateAudioOperations(GroqClient client) {
        System.out.println("\n=== Audio Operations Demo (Latest Features) ===");
        
        // Demo 1: Standard TTS with different PlayAI voices
        System.out.println("1. Standard Text-to-Speech:");
        demonstrateStandardTTS(client);
        
        // Demo 2: PlayAI TTS with enhanced features
        System.out.println("\n2. PlayAI TTS (High Quality):");
        demonstratePlayAITTS(client);
        
        // Demo 3: TTS with different formats and speeds
        System.out.println("\n3. Advanced TTS Configuration:");
        demonstrateAdvancedTTS(client);
        
        // Demo 4: Speech-to-Text Transcription
        System.out.println("\n4. Speech-to-Text Transcription:");
        demonstrateTranscription(client);
        
        // Demo 5: Audio Translation
        System.out.println("\n5. Audio Translation:");
        demonstrateTranslation(client);
    }

    /**
     * Demonstrates standard text-to-speech with different PlayAI voices.
     */
    private static void demonstrateStandardTTS(GroqClient client) {
        try {
            // Use popular voices for demonstration
            String[] voicesToTry = client.audio().getPopularVoices();
            String sampleText = "Hello ! This is a demonstration of Groq's text to speech capabilities.";
            
            for (String voice : voicesToTry) {
                try {
                    SpeechRequest request = new SpeechRequest();
                    request.setModel("playai-tts");
                    request.setInput(sampleText);
                    request.setVoice(voice);
                    request.setResponseFormat("mp3");
                    request.setSpeed(1.0);
                    
                    System.out.println("  Trying voice: " + voice);
                    GroqResponse<SpeechResponse> response = client.audio().createSpeech(request);
                    
                    if (response.isSuccessful()) {
                        SpeechResponse speechResponse = response.getData();
                        System.out.println("  * " + voice + ": Generated " + 
                                         speechResponse.getAudio().length + " bytes of audio");
                        
                        // Save the audio file
                        String filename = generateTimestampedFilename("voice_" + voice.replace("-", "_"), "mp3");
                        String savedPath = saveAudioToFile(speechResponse.getAudio(), filename, "mp3");
                        
                        if (savedPath != null) {
                            System.out.println("  * Saved as: " + filename);
                        }
                    } else {
                        System.err.println("  x " + voice + " failed with status: " + response.getStatusCode());
                    }
                    
                } catch (Exception e) {
                    System.err.println("  x " + voice + " error: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("x Standard TTS demo error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates PlayAI TTS with high-quality features.
     */
    private static void demonstratePlayAITTS(GroqClient client) {
        try {
            String sampleText = "Hello ! This is a demonstration of of Groq's text to AI speech capabilities.";
            
            SpeechRequest request = new SpeechRequest();
            request.setModel("playai-tts");
            request.setInput(sampleText);
            request.setVoice("Fritz-PlayAI");
            request.setResponseFormat("mp3");
            request.setSpeed(1.2); // Slightly faster than normal
            
            GroqResponse<SpeechResponse> response = client.audio().createSpeech(request);
            
            if (response.isSuccessful()) {
                SpeechResponse speechResponse = response.getData();
                System.out.println("* PlayAI TTS successful!");
                System.out.println("* Generated " + speechResponse.getAudio().length + " bytes of high-quality audio");
                System.out.println("* Voice: Fritz-PlayAI");
                System.out.println("* Speed: 1.2x");
                
                // Save the audio file
                String filename = generateTimestampedFilename("playai_high_quality", "mp3");
                String savedPath = saveAudioToFile(speechResponse.getAudio(), filename, "mp3");
                
                if (savedPath != null) {
                    System.out.println("* Saved as: " + filename);
                }
            } else {
                System.err.println("x PlayAI TTS failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x PlayAI TTS error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates advanced TTS configurations.
     */
    private static void demonstrateAdvancedTTS(GroqClient client) {
        try {
            String sampleText = "This demonstrates Groq's different audio formats and speech speeds.";
            String testVoice = "Jennifer-PlayAI";
            
            // Test different speeds
            Double[] speeds = {0.5, 1.0, 1.5, 2.0};
            for (Double speed : speeds) {
                try {
                    SpeechRequest request = new SpeechRequest();
                    request.setModel("playai-tts");
                    request.setInput(sampleText);
                    request.setVoice(testVoice);
                    request.setResponseFormat("mp3");
                    request.setSpeed(speed);
                    
                    GroqResponse<SpeechResponse> response = client.audio().createSpeech(request);
                    
                    if (response.isSuccessful()) {
                        SpeechResponse speechResponse = response.getData();
                        System.out.println("* Speed " + speed + "x: Success - " + 
                                         speechResponse.getAudio().length + " bytes");
                        
                        // Save the audio file
                        String filename = generateTimestampedFilename(
                            "speed_" + speed + "x_" + testVoice.replace("-", "_"), "mp3");
                        String savedPath = saveAudioToFile(speechResponse.getAudio(), filename, "mp3");
                        
                        if (savedPath != null) {
                            System.out.println("  * Saved as: " + filename);
                        }
                    } else {
                        System.err.println("x Speed " + speed + "x failed");
                    }
                    
                } catch (Exception e) {
                    System.err.println("x Speed " + speed + "x error: " + e.getMessage());
                }
            }
            
            // Test different formats
            String[] formats = {"mp3", "wav", "flac"};
            for (String format : formats) {
                try {
                    SpeechRequest request = new SpeechRequest();
                    request.setModel("playai-tts");
                    request.setInput("Format test: " + format);
                    request.setVoice(testVoice);
                    request.setResponseFormat(format);
                    request.setSpeed(1.0);
                    
                    GroqResponse<SpeechResponse> response = client.audio().createSpeech(request);
                    
                    if (response.isSuccessful()) {
                        SpeechResponse speechResponse = response.getData();
                        System.out.println("* Format " + format + ": Success - " + 
                                         speechResponse.getAudio().length + " bytes");
                        
                        // Save the audio file
                        String filename = generateTimestampedFilename(
                            "format_" + format + "_" + testVoice.replace("-", "_"), format);
                        String savedPath = saveAudioToFile(speechResponse.getAudio(), filename, format);
                        
                        if (savedPath != null) {
                            System.out.println("  * Saved as: " + filename);
                        }
                    } else {
                        System.err.println("x Format " + format + " failed");
                    }
                    
                } catch (Exception e) {
                    System.err.println("x Format " + format + " error: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("x Advanced TTS demo error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates speech-to-text transcription with actual audio files.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateTranscription(GroqClient client) {
        try {
            System.out.println("* Speech-to-Text Transcription Demo:");
            System.out.println("  Note: Transcription converts speech to text in the original language");
            
            String[] audioFiles = {
                "src/main/resources/audio/input_1.mp3",
                "src/main/resources/audio/input_2.wav"
            };
            
            for (String audioFile : audioFiles) {
                try {
                    System.out.println("\n  Processing: " + audioFile);
                    
                    Path filePath = Paths.get(audioFile);
                    
                    if (!Files.exists(filePath)) {
                        System.err.println("  x Audio file not found: " + filePath.toAbsolutePath());
                        System.out.println("  * Please ensure audio files exist at: " + audioFile);
                        continue;
                    }
                    
                    System.out.println("  * Found audio file: " + filePath.toAbsolutePath());
                    System.out.println("  * File size: " + Files.size(filePath) + " bytes");
                    
                    // Create transcription request with file path
                    TranscriptionRequest request = new TranscriptionRequest();
                    request.setModel("whisper-large-v3-turbo");
                    request.setFile(filePath.toAbsolutePath().toString());
                    request.setLanguage("en");
                    request.setResponseFormat("verbose_json");
                    request.setTemperature(0.0);
                    request.setPrompt("This is a demonstration of speech recognition.");
                    
                    System.out.println("  * Sending transcription request...");
                    System.out.println("  * Model: " + request.getModel());
                    System.out.println("  * Language: " + request.getLanguage());
                    System.out.println("  * Format: " + request.getResponseFormat());
                    
                    GroqResponse<Transcription> response = client.audio().createTranscription(request);
                    
                    if (response.isSuccessful()) {
                        Transcription transcription = response.getData();
                        String transcribedText = transcription.getText();
                        
                        System.out.println("  * Transcription successful!");
                        System.out.println("  * Extracted text: " + transcribedText);
                        System.out.println("  * Response status: " + response.getStatusCode());
                        
                        // Save transcription to file
                        String transcriptFilename = generateTimestampedFilename(
                            "transcript_" + filePath.getFileName().toString().replace(".", "_"), "txt");
                        Path transcriptPath = Paths.get(AUDIO_OUTPUT_DIR, transcriptFilename);
                        
                        Files.write(transcriptPath, transcribedText.getBytes());
                        System.out.println("  * Transcript saved to: " + transcriptPath.toAbsolutePath());
                        
                    } else {
                        System.err.println("  x Transcription failed with status: " + response.getStatusCode());
                    }
                    
                } catch (Exception e) {
                    System.err.println("  x Error processing " + audioFile + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }            
            // Demonstrate different transcription configurations
            System.out.println("\n* Advanced Transcription Features:");
            demonstrateAdvancedTranscription(client);
        } catch (Exception e) {
            System.err.println("x Transcription demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates audio translation to Spanish text.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateTranslation(GroqClient client) {
        try {
            System.out.println("* Audio Translation Demo:");
            System.out.println("  Note: This demonstrates translating Spanish audio to English text");
            
            String[] audioFiles = {
                "src/main/resources/audio/spanish_input_1.mp3",
                "src/main/resources/audio/spanish_input_2.wav"
            };
            
            for (String audioFile : audioFiles) {
                try {
                    System.out.println("\n  Processing: " + audioFile);
                    
                    Path filePath = Paths.get(audioFile);
                    
                    if (!Files.exists(filePath)) {
                        System.err.println("  x Audio file not found: " + filePath.toAbsolutePath());
                        continue;
                    }
                    
                    System.out.println("  * Found audio file: " + filePath.toAbsolutePath());
                    System.out.println("  * File size: " + Files.size(filePath) + " bytes");
                    
                    // Create translation request with file path
                    TranslationRequest request = new TranslationRequest();
                    request.setModel("whisper-large-v3");
                    request.setFile(filePath.toAbsolutePath().toString());
                    request.setPrompt("Translate this audio content from Spanish to English. Provide accurate English translation.");
                    request.setResponseFormat("verbose_json");
                    request.setTemperature(0.1); // Lower temperature for more consistent translations
                    request.setLanguage("es");
                    
                    System.out.println("  * Sending translation request...");
                    System.out.println("  * Model: " + request.getModel());
                    System.out.println("  * Target Language: English");
                    System.out.println("  * Format: " + request.getResponseFormat());
                    System.out.println("  * Prompt: " + request.getPrompt());
                    
                    GroqResponse<Translation> response = client.audio().createTranslation(request);
                    
                    if (response.isSuccessful()) {
                        Translation translation = response.getData();
                        String translatedText = translation.getText();
                        
                        System.out.println("  * Translation to English successful!");
                        System.out.println("  * Translated text (English): " + translatedText);
                        System.out.println("  * Response status: " + response.getStatusCode());
                        
                        // Save translation to file
                        String translationFilename = generateTimestampedFilename(
                            "translation_es_en_" + filePath.getFileName().toString().replace(".", "_"), "txt");
                        Path translationPath = Paths.get(AUDIO_OUTPUT_DIR, translationFilename);
                        
                        Files.write(translationPath, translatedText.getBytes());
                        System.out.println("  * Spanish translation saved to: " + translationPath.toAbsolutePath());
                        
                    } else {
                        System.err.println("  x Translation failed with status: " + response.getStatusCode());
                    }
                    
                } catch (Exception e) {
                    System.err.println("  x Error processing " + audioFile + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }            
        } catch (Exception e) {
            System.err.println("x Spanish translation demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates advanced transcription features with different configurations.
     */
    private static void demonstrateAdvancedTranscription(GroqClient client) {
        try {
            String testAudioFile = "src/main/resources/audio/input_1.mp3";
            Path filePath = Paths.get(testAudioFile);
            
            if (!Files.exists(filePath)) {
                System.out.println("  x Test audio file not found for advanced features");
                return;
            }
            
            // Demo 1: Simple text output
            System.out.println("  1. Simple Text Transcription:");
            TranscriptionRequest simpleRequest = new TranscriptionRequest();
            simpleRequest.setModel("whisper-large-v3-turbo");
            simpleRequest.setFile(filePath.toAbsolutePath().toString());
            simpleRequest.setResponseFormat("text");
            
            GroqResponse<Transcription> simpleResponse = client.audio().createTranscription(simpleRequest);
            if (simpleResponse.isSuccessful()) {
                System.out.println("    * Text format: " + 
                    (simpleResponse.getData().getText() != null ? 
                     simpleResponse.getData().getText().substring(0, Math.min(100, simpleResponse.getData().getText().length())) + "..." : 
                     "No text"));
            }
            
            // Demo 2: JSON with timestamps
            System.out.println("  2. JSON with Timestamps:");
            TranscriptionRequest jsonRequest = new TranscriptionRequest();
            jsonRequest.setModel("whisper-large-v3-turbo");
            jsonRequest.setFile(filePath.toAbsolutePath().toString());
            jsonRequest.setResponseFormat("verbose_json");
            
            GroqResponse<Transcription> jsonResponse = client.audio().createTranscription(jsonRequest);
            if (jsonResponse.isSuccessful()) {
                System.out.println("    * JSON format successful");
                System.out.println("    * Transcribed text length: " + 
                    (jsonResponse.getData().getText() != null ? jsonResponse.getData().getText().length() : 0));
            }
            
            // Demo 3: Different language specification
            System.out.println("  3. Language-Specific Transcription:");
            TranscriptionRequest langRequest = new TranscriptionRequest();
            langRequest.setModel("whisper-large-v3-turbo");
            langRequest.setFile(filePath.toAbsolutePath().toString());
            langRequest.setLanguage("en");
            langRequest.setResponseFormat("json");
            
            GroqResponse<Transcription> langResponse = client.audio().createTranscription(langRequest);
            if (langResponse.isSuccessful()) {
                System.out.println("    * Language-specific transcription successful");
            }
            
            // Demo 4: With temperature for variability
            System.out.println("  4. Transcription with Temperature:");
            TranscriptionRequest tempRequest = new TranscriptionRequest();
            tempRequest.setModel("whisper-large-v3-turbo");
            tempRequest.setFile(filePath.toAbsolutePath().toString());
            tempRequest.setTemperature(0.5);
            tempRequest.setResponseFormat("text");
            
            GroqResponse<Transcription> tempResponse = client.audio().createTranscription(tempRequest);
            if (tempResponse.isSuccessful()) {
                System.out.println("    * Temperature-based transcription successful");
            }
            
            System.out.println("  * Advanced transcription features demonstrated");
            
        } catch (Exception e) {
            System.err.println("  x Advanced transcription error: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates batch processing operations.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateBatchOperations(GroqClient client) {
        System.out.println("\n=== Batch Operations Demo ===");
        
        // Demo 1: List batches
        System.out.println("1. List Batches:");
        demonstrateListBatches(client);
        
        // Demo 2: Create batch (conceptual - requires input file)
        System.out.println("\n2. Batch Creation (Conceptual):");
        demonstrateBatchCreation(client);
        
        // Demo 3: Retrieve batch
        System.out.println("\n3. Batch Retrieval:");
        demonstrateBatchRetrieval(client);
    }

    /**
     * Demonstrates listing available batches.
     */
    private static void demonstrateListBatches(GroqClient client) {
        try {
            GroqResponse<BatchList> response = client.batches().list();
            
            if (response.isSuccessful()) {
                BatchList batchList = response.getData();
                System.out.println("* Retrieved " + (batchList.getData() != null ? batchList.getData().size() : 0) + " batches");
                
                if (batchList.getData() != null && !batchList.getData().isEmpty()) {
                    batchList.getData().stream()
                        .limit(3) // Show first 3 batches
                        .forEach(batch -> {
                            System.out.println("  - Batch: " + batch.getId() + 
                                             " (status: " + batch.getStatus() + ")");
                            if (batch.getRequestCounts() != null) {
                                System.out.println("    Total: " + batch.getRequestCounts().getTotal() +
                                                 ", Completed: " + batch.getRequestCounts().getCompleted() +
                                                 ", Failed: " + batch.getRequestCounts().getFailed());
                            }
                        });
                } else {
                    System.out.println("* No batches found (this is normal for new accounts)");
                }
            } else {
                System.err.println("x Batch listing failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Batch listing error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates batch creation (conceptual example).
     */
    private static void demonstrateBatchCreation(GroqClient client) {
        try {
            // Note: Batch creation requires an input file ID which we don't have in this demo
            // This is a conceptual example showing the request structure
            BatchCreateRequest request = new BatchCreateRequest();
            request.setInputFileId("file-abc123"); // This would be a real file ID
            request.setEndpoint("/v1/chat/completions");
            request.setCompletionWindow("24h");
            
            System.out.println("* Batch creation request structure demonstrated");
            System.out.println("* Endpoint: " + request.getEndpoint());
            System.out.println("* Completion window: " + request.getCompletionWindow());
            System.out.println("* In real usage, provide actual input file ID");
            
            // Uncomment the following lines when you have actual file IDs:
            /*
            GroqResponse<Batch> response = client.batches().create(request);
            if (response.isSuccessful()) {
                Batch batch = response.getData();
                System.out.println("* Batch created: " + batch.getId() + " (status: " + batch.getStatus() + ")");
            }
            */
            
        } catch (Exception e) {
            System.err.println("x Batch creation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates batch retrieval.
     */
    private static void demonstrateBatchRetrieval(GroqClient client) {
        try {
            // First, list batches to get an ID to retrieve
            GroqResponse<BatchList> listResponse = client.batches().list();
            
            if (listResponse.isSuccessful() && 
                listResponse.getData().getData() != null && 
                !listResponse.getData().getData().isEmpty()) {
                
                String batchId = listResponse.getData().getData().get(0).getId();
                GroqResponse<Batch> response = client.batches().retrieve(batchId);
                
                if (response.isSuccessful()) {
                    Batch batch = response.getData();
                    System.out.println("* Retrieved batch: " + batch.getId());
                    System.out.println("* Status: " + batch.getStatus());
                    System.out.println("* Created: " + batch.getCreatedAt());
                    
                    if (batch.getRequestCounts() != null) {
                        System.out.println("* Requests: " + batch.getRequestCounts().getTotal() + " total, " +
                                         batch.getRequestCounts().getCompleted() + " completed, " +
                                         batch.getRequestCounts().getFailed() + " failed");
                    }
                } else {
                    System.err.println("x Batch retrieval failed with status: " + response.getStatusCode());
                }
            } else {
                System.out.println("* No batches available to retrieve (this is normal for new accounts)");
            }
            
        } catch (Exception e) {
            System.err.println("x Batch retrieval error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates file operations including upload, list, and retrieval.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateFileOperations(GroqClient client) {
        System.out.println("\n=== File Operations Demo ===");
        
        // Demo 1: List files
        System.out.println("1. List Files:");
        demonstrateListFiles(client);
        
        // Demo 2: File upload (conceptual)
        System.out.println("\n2. File Upload (Conceptual):");
        demonstrateFileUpload(client);
        
        // Demo 3: File operations with mock data
        System.out.println("\n3. File Operations with JSON:");
        demonstrateFileOperationsWithJson(client);
    }

    /**
     * Demonstrates listing available files.
     */
    private static void demonstrateListFiles(GroqClient client) {
        try {
            GroqResponse<FileList> response = client.files().list();
            
            if (response.isSuccessful()) {
                FileList fileList = response.getData();
                System.out.println("* Retrieved " + (fileList.getData() != null ? fileList.getData().size() : 0) + " files");
                
                if (fileList.getData() != null && !fileList.getData().isEmpty()) {
                    fileList.getData().stream()
                        .limit(3) // Show first 3 files
                        .forEach(file -> {
                            System.out.println("  - File: " + file.getFilename() + 
                                             " (ID: " + file.getId() + ")");
                            System.out.println("    Purpose: " + file.getPurpose() +
                                             ", Size: " + file.getBytes() + " bytes");
                            System.out.println("    Created: " + file.getCreatedAt() +
                                             ", Status: " + file.getStatus());
                        });
                } else {
                    System.out.println("* No files found (this is normal for new accounts)");
                }
            } else {
                System.err.println("x File listing failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x File listing error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates file upload (conceptual example).
     */
    private static void demonstrateFileUpload(GroqClient client) {
        try {
            // Note: This is a conceptual example showing the request structure
            // In a real scenario, you would provide actual file content
            FileUploadRequest request = new FileUploadRequest();
            request.setFile("[Base64 encoded file content or file path would go here]");
            request.setPurpose("fine-tune");
            request.setFilename("training_data.jsonl");
            
            System.out.println("* File upload request structure demonstrated");
            System.out.println("* Purpose: " + request.getPurpose());
            System.out.println("* Filename: " + request.getFilename());
            System.out.println("* In real usage, provide actual file content");
            
            // Uncomment the following lines when you have actual file content:
            /*
            GroqResponse<FileObject> response = client.files().upload(request);
            if (response.isSuccessful()) {
                FileObject file = response.getData();
                System.out.println("* File uploaded: " + file.getFilename() + " (ID: " + file.getId() + ")");
            }
            */
            
        } catch (Exception e) {
            System.err.println("x File upload error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates file operations with JSON content.
     */
    private static void demonstrateFileOperationsWithJson(GroqClient client) {
        try {
            // Create sample JSON content
            Map<String, Object> sampleData = new HashMap<>();
            sampleData.put("demo", "Groq Java SDK File Operations");
            sampleData.put("timestamp", System.currentTimeMillis());
            sampleData.put("features", Arrays.asList("chat", "embeddings", "audio", "files", "batches"));
            
            String jsonContent = objectMapper.writeValueAsString(sampleData);
            String filename = "groq_sdk_demo_" + System.currentTimeMillis();
            String purpose = "assistants";
            
            System.out.println("* Created sample JSON content for upload");
            System.out.println("* JSON size: " + jsonContent.length() + " characters");
            
            // Note: Uncomment to actually upload (commented to avoid creating real files in demo)
            /*
            GroqResponse<FileObject> uploadResponse = client.files().uploadJson(jsonContent, filename, purpose);
            if (uploadResponse.isSuccessful()) {
                FileObject uploadedFile = uploadResponse.getData();
                System.out.println("* JSON file uploaded: " + uploadedFile.getFilename());
                System.out.println("* File ID: " + uploadedFile.getId());
                
                // Demonstrate file retrieval
                GroqResponse<FileObject> retrieveResponse = client.files().retrieve(uploadedFile.getId());
                if (retrieveResponse.isSuccessful()) {
                    System.out.println("* File retrieved: " + retrieveResponse.getData().getFilename());
                }
                
                // Demonstrate file content retrieval
                GroqResponse<String> contentResponse = client.files().retrieveContent(uploadedFile.getId());
                if (contentResponse.isSuccessful()) {
                    System.out.println("* File content retrieved: " + contentResponse.getData().length() + " characters");
                }
                
                // Demonstrate file deletion (commented to avoid actual deletion in demo)
                // GroqResponse<FileDeleteResponse> deleteResponse = client.files().delete(uploadedFile.getId());
                // if (deleteResponse.isSuccessful() && deleteResponse.getData().getDeleted()) {
                //     System.out.println("* File deleted successfully");
                // }
            }
            */
            
        } catch (Exception e) {
            System.err.println("x File operations with JSON error: " + e.getMessage());
        }
    }
 // Add this method to demonstrate reasoning functionality
    private static void demonstrateReasoning(GroqClient client) {
        System.out.println("\n=== Reasoning Demo ===");
        
        try {
            ChatMessage message = new ChatMessage("user", "Solve this math problem step by step: What is 15% of 80?");
            ChatCompletionRequest request = new ChatCompletionRequest("openai/gpt-oss-120b", Arrays.asList(message));
            
            // Enable reasoning
            request.setIncludeReasoning(true);
//            request.setReasoningFormat("parsed");
            request.setMaxTokens(500);
            
            GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
            
            if (response.isSuccessful()) {
                ChatCompletion completion = response.getData();
                ChatChoice choice = completion.getChoices().get(0);
                ChatMessage assistantMessage = choice.getMessage();
                
                System.out.println("* Reasoning response:");
                
                // Display reasoning if available
                if (assistantMessage.getReasoning() != null && !assistantMessage.getReasoning().isEmpty()) {
                    System.out.println("Reasoning: " + assistantMessage.getReasoning());
                }
                
                // Display content
                if (assistantMessage.getContent() != null && !assistantMessage.getContent().isEmpty()) {
                    System.out.println("Content: " + assistantMessage.getContent());
                }
                
                System.out.println("* Finish reason: " + choice.getFinishReason());
                
            } else {
                System.err.println("x Reasoning demo failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Reasoning demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add this method to demonstrate reasoning with tools
    private static void demonstrateReasoningWithTools(GroqClient client) {
        System.out.println("\n=== Reasoning with Tools Demo ===");
        
        try {
            // Create tools
            FunctionDefinition calculatorFunction = createCalculatorFunction();
            ChatTool calculatorTool = new ChatTool("function", calculatorFunction);
            List<ChatTool> tools = Arrays.asList(calculatorTool);
            
            ChatMessage message = new ChatMessage("user", 
                "I need to calculate the area of a circle with radius 5. " +
                "Please show your reasoning and use the calculator tool for the calculation.");
            
            ChatCompletionRequest request = new ChatCompletionRequest("openai/gpt-oss-120b", Arrays.asList(message));
            request.setTools(tools);
            request.setToolChoice("auto");
            request.setIncludeReasoning(true);
//            request.setReasoningFormat("parsed");
            request.setMaxTokens(600);
            
            GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
            
            if (response.isSuccessful()) {
                ChatMessage assistantMessage = response.getData().getChoices().get(0).getMessage();
                
                System.out.println("* Reasoning with tools:");
                
                // Display reasoning
                if (assistantMessage.getReasoning() != null && !assistantMessage.getReasoning().isEmpty()) {
                    System.out.println("Reasoning: " + assistantMessage.getReasoning());
                }
                
                // Check for tool calls
                if (assistantMessage.getToolCalls() != null && !assistantMessage.getToolCalls().isEmpty()) {
                    System.out.println("Tool calls requested:");
                    for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
                        System.out.println("  - " + toolCall.getFunction().getName() + 
                                         ": " + toolCall.getFunction().getArguments());
                        
                        // Execute tool and continue conversation
                        String toolResult = realToolExecution(toolCall.getFunction().getName(), 
                                                             toolCall.getFunction().getArguments());
                        System.out.println("  - Tool result: " + toolResult);
                        
                        // Create tool response message
                        ChatMessage toolMessage = ChatMessage.createToolMessage(toolCall.getId(), toolResult);
                        
                        // Continue conversation with tool result
                        List<ChatMessage> conversation = Arrays.asList(message, assistantMessage, toolMessage);
                        ChatCompletionRequest followupRequest = new ChatCompletionRequest(
                            "openai/gpt-oss-120b", conversation);
                        followupRequest.setIncludeReasoning(true);
                        // Enable below when "includeReasoning" is not set.
                        // followupRequest.setReasoningFormat("parsed");
                        
                        GroqResponse<ChatCompletion> followupResponse = client.chat().createCompletion(followupRequest);
                        
                        if (followupResponse.isSuccessful()) {
                            ChatMessage finalMessage = followupResponse.getData().getChoices().get(0).getMessage();
                            System.out.println("* Final response with reasoning:");
                            if (finalMessage.getReasoning() != null) {
                                System.out.println("Reasoning: " + finalMessage.getReasoning());
                            }
                            System.out.println("Answer: " + finalMessage.getContent());
                        }
                    }
                } else if (assistantMessage.getContent() != null) {
                    System.out.println("Content: " + assistantMessage.getContent());
                }
                
            } else {
                System.err.println("x Reasoning with tools failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Reasoning with tools error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates vision operations including image analysis with URL, local images, and image bytes.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateVisionOperations(GroqClient client) {
        System.out.println("\n=== Vision Operations Demo ===");
        
        try {
            String[] visionModels = client.vision().getCommonVisionModels();
            System.out.println("* Available vision models: " + Arrays.toString(visionModels));
            
            String visionModel = visionModels[0];
            System.out.println("* Using vision model: " + visionModel);
            
            // Demo 1: Using remote URL
            System.out.println("\n1. Testing with Remote Image URL:");
            demonstrateRemoteImageAnalysis(client, visionModel);
            
            // Demo 2: Using local image
            System.out.println("\n2. Testing with Local Image:");
            demonstrateLocalImageAnalysis(client, visionModel);
            
            // Demo 3: Using image bytes
            System.out.println("\n3. Testing with Image Bytes:");
            demonstrateImageBytesAnalysis(client, visionModel);
            
        } catch (Exception e) {
            System.err.println("x Vision operations demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates vision analysis with image bytes.
     */
    private static void demonstrateImageBytesAnalysis(GroqClient client, String model) {
        try {
            Path imagePath = Paths.get("src/main/resources/images/input_2.jpg");
            
            if (!Files.exists(imagePath)) {
                System.out.println("x Local image file not found: " + imagePath.toAbsolutePath());
                System.out.println("* Please ensure the image file exists at: src/main/resources/images/input_1.jpg");
                return;
            }
            
            System.out.println("* Using image from: " + imagePath.toAbsolutePath());
            
            // Read image as bytes
            byte[] imageBytes = Files.readAllBytes(imagePath);
            System.out.println("* Read image bytes: " + imageBytes.length + " bytes");
            
            String prompt = "What's in this image? Describe it in detail.";
            String mimeType = "image/jpeg";
            
            VisionRequest request = client.vision().createVisionRequestWithImageBytes(
                model, imageBytes, mimeType, prompt
            );
            request.setMaxTokens(500);
            request.setTemperature(0.1);
            request.setTopP(1.0);
            request.setStream(false);
            
            GroqResponse<ChatCompletion> response = client.vision().createCompletion(request);
            
            if (response.isSuccessful()) {
                ChatCompletion completion = response.getData();
                if (completion.getChoices() != null && !completion.getChoices().isEmpty()) {
                    ChatChoice choice = completion.getChoices().get(0);
                    if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                        String analysis = choice.getMessage().getContent();
                        System.out.println("* Image Bytes Analysis: " + analysis);
                        System.out.println("* Token usage: " + completion.getUsage().getTotalTokens() + " tokens");
                    } else {
                        System.out.println("* No analysis content received");
                    }
                } else {
                    System.out.println("* No choices in response");
                }
            } else {
                System.err.println("x Image bytes analysis failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Image bytes analysis error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates vision analysis with a remote image URL.
     */
    private static void demonstrateRemoteImageAnalysis(GroqClient client, String model) {
        try {
            String testImageUrl = "https://wallpaperaccess.com/full/5324597.jpg";
            String prompt = "Who is in the image ?.";
            
            System.out.println("* Testing with remote URL: " + testImageUrl);
            
            VisionRequest request = client.vision().createVisionRequestWithUrl(model, testImageUrl, prompt);
            request.setMaxTokens(500);
            request.setTemperature(0.1);
            request.setTopP(1.0);
            request.setStream(false);
            
            GroqResponse<ChatCompletion> response = client.vision().createCompletion(request);
            
            if (response.isSuccessful()) {
                ChatCompletion completion = response.getData();
                if (completion.getChoices() != null && !completion.getChoices().isEmpty()) {
                    ChatChoice choice = completion.getChoices().get(0);
                    if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                        String analysis = choice.getMessage().getContent();
                        System.out.println("* Remote Image Analysis: " + analysis);
                        System.out.println("* Token usage: " + completion.getUsage().getTotalTokens() + " tokens");
                    } else {
                        System.out.println("* No analysis content received");
                    }
                } else {
                    System.out.println("* No choices in response");
                }
            } else {
                System.err.println("x Remote image analysis failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Remote image analysis error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates vision analysis with a local image file.
     */
    private static void demonstrateLocalImageAnalysis(GroqClient client, String model) {
        try {
            Path imagePath = Paths.get("src/main/resources/images/input_1.jpg");
            
            if (!Files.exists(imagePath)) {
                System.out.println("x Local image file not found: " + imagePath.toAbsolutePath());
                System.out.println("* Please ensure the image file exists at: src/main/resources/images/input_1.jpg");
                return;
            }
            
            System.out.println("* Using local image: " + imagePath.toAbsolutePath());
            
            String prompt = "What is the text in the image ?.";
            
            VisionRequest request = client.vision().createVisionRequestWithLocalImage(
                model, imagePath.toString(), prompt
            );
            request.setMaxTokens(500);
            request.setTemperature(0.1);
            request.setTopP(1.0);
            request.setStream(false);
            
            GroqResponse<ChatCompletion> response = client.vision().createCompletion(request);
            
            if (response.isSuccessful()) {
                ChatCompletion completion = response.getData();
                if (completion.getChoices() != null && !completion.getChoices().isEmpty()) {
                    ChatChoice choice = completion.getChoices().get(0);
                    if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                        String analysis = choice.getMessage().getContent();
                        System.out.println("* Local Image Analysis: " + analysis);
                        System.out.println("* Token usage: " + completion.getUsage().getTotalTokens() + " tokens");
                    } else {
                        System.out.println("* No analysis content received");
                    }
                } else {
                    System.out.println("* No choices in response");
                }
            } else {
                System.err.println("x Local image analysis failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Local image analysis error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates Response API operations with reasoning.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateResponseAPI(GroqClient client) {
        System.out.println("\n=== Response API Demo ===");
        
        try {
            // Demo 1: Reasoning with low effort
            System.out.println("1. Reasoning with Low Effort:");
            demonstrateReasoningResponse(client);
            
            // Demo 2: Code Interpreter
            System.out.println("\n2. Code Interpreter:");
            demonstrateCodeInterpreterResponse(client);
            
            // Demo 3: Browser Search
            System.out.println("\n3. Browser Search:");
            demonstrateBrowserSearchResponse(client);
            
            // Demo 4: Complex input with messages
            System.out.println("\n4. Complex Message Input:");
            demonstrateComplexResponse(client);
            
        } catch (Exception e) {
            System.err.println("x Response API demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates Response API with reasoning.
     */
    private static void demonstrateReasoningResponse(GroqClient client) {
        try {
            com.groq.sdk.models.responses.ResponseRequest request = 
                new com.groq.sdk.models.responses.ResponseRequest("openai/gpt-oss-20b", 
                "How are AI models trained? Be brief.");
            request.setReasoning(new com.groq.sdk.models.responses.ReasoningConfig("low"));
            request.setTemperature(1.0);
            request.setTopP(1.0);
            
            System.out.println("* Sending reasoning request...");
            
            com.groq.sdk.models.GroqResponse<com.groq.sdk.models.responses.Response> response = 
                client.responses().create(request);
            
            if (response.isSuccessful()) {
                com.groq.sdk.models.responses.Response resp = response.getData();
                System.out.println("* Response ID: " + resp.getId());
                System.out.println("* Status: " + resp.getStatus());
                System.out.println("* Model: " + resp.getModel());
                
                // Process outputs
                for (com.groq.sdk.models.responses.ResponseOutput output : resp.getOutput()) {
                    if (output instanceof com.groq.sdk.models.responses.ReasoningOutput) {
                        com.groq.sdk.models.responses.ReasoningOutput reasoning = 
                            (com.groq.sdk.models.responses.ReasoningOutput) output;
                        System.out.println("* Reasoning:");
                        for (com.groq.sdk.models.responses.ReasoningContent content : reasoning.getContent()) {
                            System.out.println("  - " + content.getText());
                        }
                    } else if (output instanceof com.groq.sdk.models.responses.MessageOutput) {
                        com.groq.sdk.models.responses.MessageOutput message = 
                            (com.groq.sdk.models.responses.MessageOutput) output;
                        System.out.println("* Final Response:");
                        for (com.groq.sdk.models.responses.MessageContent content : message.getContent()) {
                            System.out.println("  " + content.getText());
                        }
                    }
                }
                
                // Show usage
                if (resp.getUsage() != null) {
                    System.out.println("* Usage: " + resp.getUsage().getTotalTokens() + " tokens total");
                    System.out.println("* Input: " + resp.getUsage().getInputTokens() + " tokens");
                    System.out.println("* Output: " + resp.getUsage().getOutputTokens() + " tokens");
                }
            } else {
                System.err.println("x Reasoning response failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Reasoning response error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates Response API with complex message input.
     */
    private static void demonstrateComplexResponse(GroqClient client) {
        try {
            // Create multiple message inputs
            List<com.groq.sdk.models.responses.MessageInput> messages = Arrays.asList(
                new com.groq.sdk.models.responses.MessageInput("system", "You are a helpful assistant."),
                new com.groq.sdk.models.responses.MessageInput("user", "Explain quantum computing in simple terms.")
            );
            
            com.groq.sdk.models.responses.ResponseRequest request = 
                new com.groq.sdk.models.responses.ResponseRequest("openai/gpt-oss-20b", messages);
            request.setReasoning(new com.groq.sdk.models.responses.ReasoningConfig("medium"));
            request.setMaxOutputTokens(500);
            
            System.out.println("* Sending complex message request...");
            
            com.groq.sdk.models.GroqResponse<com.groq.sdk.models.responses.Response> response = 
                client.responses().create(request);
            
            if (response.isSuccessful()) {
                com.groq.sdk.models.responses.Response resp = response.getData();
                System.out.println("* Complex response received");
                
                for (com.groq.sdk.models.responses.ResponseOutput output : resp.getOutput()) {
                    if (output instanceof com.groq.sdk.models.responses.ReasoningOutput) {
                        System.out.println("* Reasoning process completed");
                    } else if (output instanceof com.groq.sdk.models.responses.MessageOutput) {
                        com.groq.sdk.models.responses.MessageOutput message = 
                            (com.groq.sdk.models.responses.MessageOutput) output;
                        for (com.groq.sdk.models.responses.MessageContent content : message.getContent()) {
                            System.out.println("* Explanation: " + content.getText());
                        }
                    }
                }
            } else {
                System.err.println("x Complex response failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Complex response error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates Response API with code interpreter.
     */
    private static void demonstrateCodeInterpreterResponse(GroqClient client) {
        try {
            ResponseRequest request = new ResponseRequest("openai/gpt-oss-20b", 
                "What is 1312 X 3333? Output only the final answer.");
            request.setToolChoice("required");
            
            // Create code interpreter tool
            com.groq.sdk.models.chat.ChatTool codeInterpreter = new com.groq.sdk.models.chat.ChatTool();
            codeInterpreter.setType("code_interpreter");
            
            request.setChatTools(Arrays.asList(codeInterpreter));
            
            System.out.println("* Sending code interpreter request...");
            
            GroqResponse<Response> response = client.responses().create(request);
            
            if (response.isSuccessful()) {
                Response resp = response.getData();
                System.out.println("* Code interpreter response received");
                
                for (ResponseOutput output : resp.getOutput()) {
                    if (output instanceof MessageOutput) {
                        MessageOutput message = (MessageOutput) output;
                        for (MessageContent content : message.getContent()) {
                            System.out.println("* Result: " + content.getText());
                        }
                    }
                }
            } else {
                System.err.println("x Code interpreter failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Code interpreter error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates Response API with browser search.
     */
    private static void demonstrateBrowserSearchResponse(GroqClient client) {
        try {
            ResponseRequest request = new ResponseRequest("openai/gpt-oss-20b", 
                "Analyze the current weather in San Francisco and provide a detailed forecast.");
            request.setToolChoice("required");
            
            // Create browser search tool
            com.groq.sdk.models.chat.ChatTool browserSearch = new com.groq.sdk.models.chat.ChatTool();
            browserSearch.setType("browser_search");
            
            request.setChatTools(Arrays.asList(browserSearch));
            
            System.out.println("* Sending browser search request...");
            
            GroqResponse<Response> response = client.responses().create(request);
            
            if (response.isSuccessful()) {
                Response resp = response.getData();
                System.out.println("* Browser search response received");
                
                for (ResponseOutput output : resp.getOutput()) {
                    if (output instanceof MessageOutput) {
                        MessageOutput message = (MessageOutput) output;
                        for (MessageContent content : message.getContent()) {
                            System.out.println("* Weather information: " + content.getText());
                        }
                    }
                }
            } else {
                System.err.println("x Browser search failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Browser search error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates MCP operations using the unified Response API.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateMCPOperations(GroqClient client) {
        System.out.println("\n=== MCP Operations Demo (Using Response API) ===");
        
        try {
            // Demo 1: HuggingFace MCP Tool
            System.out.println("1. HuggingFace MCP Tool:");
            demonstrateHuggingFaceMCP(client);
            
            // Demo 2: Multiple MCP Tools
            System.out.println("\n2. Multiple MCP Tools:");
            demonstrateMultipleMCPTools(client);
            
            // Demo 3: Custom MCP Request
            System.out.println("\n3. Custom MCP Request:");
            demonstrateCustomMCPRequest(client);
            
        } catch (Exception e) {
            System.err.println("x MCP operations demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates MCP with HuggingFace tool.
     */
    private static void demonstrateHuggingFaceMCP(GroqClient client) {
        try {
            // Create HuggingFace MCP tool definition with validation
            MCPToolDefinition huggingFaceTool = createValidatedMCPTool(
                "Huggingface",
                 null,
                "https://huggingface.co/mcp", 
                "never"
            );
            
            System.out.println("* Created validated MCP tool: " + huggingFaceTool);
            System.out.println("* Sending MCP request with Huggingface tool...");
            
            GroqResponse<Response> response = client.mcp().createResponse(
                "openai/gpt-oss-120b", 
                "What models are trending on Huggingface ?",
                huggingFaceTool
            );
            
            processMCPResponse(response, "Huggingface");
            
        } catch (IllegalArgumentException e) {
            System.err.println("x MCP tool validation error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("x Firecrawl MCP error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates MCP with multiple tools.
     */
    private static void demonstrateMultipleMCPTools(GroqClient client) {
        try {
            // Create multiple MCP tool definitions with validation
            List<MCPToolDefinition> mcpTools = Arrays.asList(
                createValidatedMCPTool(
                    "firecrawl",
                    "Web scraping and content extraction",
                    "https://mcp.firecrawl.dev/<APIKEY>/v2/mcp",
                    "never"
                ),
                createValidatedMCPTool(
                    "calculator", 
                    "Mathematical calculations and computations",
                    "https://mcp.calculator.dev/v1/mcp",
                    "never"
                )
            );
            
            System.out.println("* Created " + mcpTools.size() + " validated MCP tools");
            System.out.println("* Sending MCP request with multiple tools...");
            
            GroqResponse<Response> response = client.mcp().createResponse(
                "openai/gpt-oss-120b",
                "What's the current weather in Tokyo and calculate the temperature in Fahrenheit if it's 25°C?",
                mcpTools
            );
            
            processMCPResponse(response, "Multiple Tools");
            
        } catch (IllegalArgumentException e) {
            System.err.println("x MCP tool validation error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("x Multiple MCP tools error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates custom MCP request with advanced configuration.
     */
    private static void demonstrateCustomMCPRequest(GroqClient client) {
        try {
            // Create custom message inputs
            List<MessageInput> messages = Arrays.asList(
                new MessageInput("system", "You are a helpful assistant with access to web search tools."),
                new MessageInput("user", "Find the latest news about artificial intelligence and summarize the key points.")
            );
            
            // Create MCP tool
            MCPToolDefinition newsTool = new MCPToolDefinition(
                "news-search",
                "Search for latest news and current events",
                "https://mcp.news.dev/v1/mcp",
                "never"
            );
            
            // Create custom request with reasoning
            ResponseRequest request = new ResponseRequest("openai/gpt-oss-120b", messages);
            request.setMCPTools(Arrays.asList(newsTool));
            request.setReasoning(new ReasoningConfig("medium"));
            request.setMaxOutputTokens(800);
            request.setTemperature(0.7);
            request.setToolChoice("auto");
            
            System.out.println("* Sending custom MCP request with reasoning...");
            
            GroqResponse<Response> response = client.mcp().createResponse(request);
            
            processMCPResponse(response, "Custom Request");
            
        } catch (Exception e) {
            System.err.println("x Custom MCP request error: " + e.getMessage());
        }
    }

    /**
     * Processes and displays MCP response outputs.
     * 
     * @param response the MCP response to process
     * @param context context description for logging
     */
    private static void processMCPResponse(GroqResponse<Response> response, String context) {
        if (response.isSuccessful()) {
            Response resp = response.getData();
            System.out.println("* " + context + " - Response ID: " + resp.getId());
            System.out.println("* Status: " + resp.getStatus());
            System.out.println("* Model: " + resp.getModel());
            System.out.println("* Number of outputs: " + resp.getOutput().size());
            
            // Process different output types
            for (ResponseOutput output : resp.getOutput()) {
                processMCPOutput(output);
            }
            
            // Show usage statistics
            if (resp.getUsage() != null) {
                System.out.println("* Usage Statistics:");
                System.out.println("  - Total tokens: " + resp.getUsage().getTotalTokens());
                System.out.println("  - Input tokens: " + resp.getUsage().getInputTokens());
                System.out.println("  - Output tokens: " + resp.getUsage().getOutputTokens());
                
                if (resp.getUsage().getInputTokensDetails() != null) {
                    System.out.println("  - Reasoning tokens: " + 
                        resp.getUsage().getInputTokensDetails().getReasoningTokens());
                }
            }
            
        } else {
            System.err.println("x " + context + " failed with status: " + response.getStatusCode());
        }
    }

    /**
     * Processes individual MCP output types.
     * 
     * @param output the output to process
     */
    private static void processMCPOutput(ResponseOutput output) {
        if (output instanceof MCPListToolsOutput) {
            MCPListToolsOutput toolsOutput = (MCPListToolsOutput) output;
            System.out.println("* MCP Tools Available from " + toolsOutput.getServerLabel() + ":");
            for (MCPTool tool : toolsOutput.getTools()) {
                System.out.println("  - " + tool.getName() + ": " + 
                    (tool.getDescription() != null ? 
                     tool.getDescription().substring(0, Math.min(80, tool.getDescription().length())) + "..." : 
                     "No description"));
            }
            
        } else if (output instanceof ReasoningOutput) {
            ReasoningOutput reasoningOutput = (ReasoningOutput) output;
            System.out.println("* Model Reasoning:");
            for (ReasoningContent content : reasoningOutput.getContent()) {
                System.out.println("  " + content.getText());
            }
            
        } else if (output instanceof MCPCallOutput) {
            MCPCallOutput callOutput = (MCPCallOutput) output;
            System.out.println("* MCP Tool Call:");
            System.out.println("  - Server: " + callOutput.getServerLabel());
            System.out.println("  - Tool: " + callOutput.getName());
            System.out.println("  - Arguments: " + 
                (callOutput.getArguments() != null ? 
                 callOutput.getArguments().substring(0, Math.min(100, callOutput.getArguments().length())) + "..." : 
                 "No arguments"));
            System.out.println("  - Output: " + 
                (callOutput.getOutput() != null ? 
                 callOutput.getOutput().substring(0, Math.min(150, callOutput.getOutput().length())) + "..." : 
                 "No output"));
            
        } else if (output instanceof MessageOutput) {
            MessageOutput messageOutput = (MessageOutput) output;
            System.out.println("* Final Response (" + messageOutput.getRole() + "):");
            for (MessageContent content : messageOutput.getContent()) {
                if (content.getText() != null && !content.getText().trim().isEmpty()) {
                    // Show truncated response for long outputs
                    String text = content.getText();
                    if (text.length() > 200) {
                        System.out.println("  " + text.substring(0, 200) + "...");
                        System.out.println("  ... (truncated, full response: " + text.length() + " characters)");
                    } else {
                        System.out.println("  " + text);
                    }
                }
            }
            
        } else {
            System.out.println("* Unknown output type: " + output.getType());
        }
    }

    /**
     * Demonstrates simple MCP usage for quick testing.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateSimpleMCP(GroqClient client) {
        System.out.println("\n=== Simple MCP Demo ===");
        
        try {
            // Simple MCP request without external tools
            GroqResponse<Response> response = client.responses().create(
                "openai/gpt-oss-120b", 
                "Explain the concept of machine learning in three bullet points."
            );
            
            if (response.isSuccessful()) {
                Response resp = response.getData();
                System.out.println("* Simple response received");
                
                for (ResponseOutput output : resp.getOutput()) {
                    if (output instanceof MessageOutput) {
                        MessageOutput message = (MessageOutput) output;
                        for (MessageContent content : message.getContent()) {
                            if (content.getText() != null) {
                                System.out.println("* Response: " + content.getText());
                            }
                        }
                    }
                }
            } else {
                System.err.println("x Simple MCP failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("x Simple MCP error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates MCP error handling and edge cases.
     * 
     * @param client the GroqClient instance to use for API calls
     */
    private static void demonstrateMCPErrorHandling(GroqClient client) {
        System.out.println("\n=== MCP Error Handling Demo ===");
        
        try {
            // Test with invalid MCP server URL
            MCPToolDefinition invalidTool = new MCPToolDefinition(
                "invalid-tool",
                "This tool should fail",
                "https://invalid-mcp-server.example.com/v1/mcp",
                "never"
            );
            
            System.out.println("* Testing MCP with invalid server URL...");
            
            GroqResponse<Response> response = client.mcp().createResponse(
                "openai/gpt-oss-120b",
                "This request should demonstrate error handling",
                invalidTool
            );
            
            if (response.isSuccessful()) {
                System.out.println("* Unexpected success with invalid MCP server");
                processMCPResponse(response, "Error Handling");
            } else {
                System.out.println("* Properly handled MCP error: HTTP " + response.getStatusCode());
                System.out.println("* Error message: " + 
                    (response.getData() != null && response.getData().getError() != null ? 
                     response.getData().getError().toString() : "Unknown error"));
            }
            
        } catch (Exception e) {
            System.out.println("* Properly caught MCP exception: " + e.getClass().getSimpleName());
            System.out.println("* Error message: " + e.getMessage());
        }
    }
    
    /**
     * Validates and creates an MCP tool definition with proper error handling.
     * 
     * @param serverLabel the server label (required)
     * @param serverDescription the server description
     * @param serverUrl the server URL (required)  
     * @param requireApproval the approval requirement
     * @return a properly configured MCPToolDefinition
     * @throws IllegalArgumentException if required fields are missing
     */
    private static MCPToolDefinition createValidatedMCPTool(String serverLabel, String serverDescription, 
                                                           String serverUrl, String requireApproval) {
        if (serverLabel == null || serverLabel.trim().isEmpty()) {
            throw new IllegalArgumentException("MCP server_label is required");
        }
        if (serverUrl == null || serverUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("MCP server_url is required");
        }
        
        MCPToolDefinition tool = new MCPToolDefinition();
        tool.setServerLabel(serverLabel.trim());
        tool.setServerDescription(serverDescription != null ? serverDescription.trim() : "");
        tool.setServerUrl(serverUrl.trim());
        tool.setRequireApproval(requireApproval != null ? requireApproval : "never");
        
        return tool;
    }
}
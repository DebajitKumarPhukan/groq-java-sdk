package com.groq.sdk.examples;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groq.sdk.client.GroqClient;
import com.groq.sdk.models.GroqResponse;
import com.groq.sdk.models.chat.ChatCompletion;
import com.groq.sdk.models.chat.ChatCompletionRequest;
import com.groq.sdk.models.chat.ChatMessage;
import com.groq.sdk.models.chat.ChatNamedToolChoice;
import com.groq.sdk.models.chat.ChatTool;
import com.groq.sdk.models.chat.ChatToolCall;
import com.groq.sdk.models.chat.FunctionDefinition;
import com.groq.sdk.models.embeddings.EmbeddingRequest;
import com.groq.sdk.models.embeddings.EmbeddingResponse;
import com.groq.sdk.models.models.ModelList;

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
    
    // Mock weather data for demonstration
    private static final Map<String, WeatherData> WEATHER_DATA = Map.of(
        "san francisco", new WeatherData(18.0, "celsius", "partly cloudy", 65, 15.2),
        "new york", new WeatherData(22.0, "celsius", "sunny", 55, 10.5),
        "london", new WeatherData(12.0, "celsius", "rainy", 85, 8.3),
        "tokyo", new WeatherData(25.0, "celsius", "clear", 60, 12.7),
        "sydney", new WeatherData(28.0, "celsius", "sunny", 45, 20.1)
    );
    
    // Regular expression patterns for calculator
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(?:\\.\\d+)?");
    private static final Pattern OPERATOR_PATTERN = Pattern.compile("[+\\-*/]");
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("-?\\d+(?:\\.\\d+)?|[+\\-*/()]");
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Groq Java SDK Demo ===");            
            GroqClient client = initializeClient();
            // Demonstrate all functionalities
            demonstrateChatCompletions(client);
            demonstrateToolCalls(client);
            demonstrateEmbeddings(client);
            demonstrateModelListing(client);
            demonstrateErrorHandling(client);
            
            // Interactive demo
            runInteractiveDemo(client);            
        } catch (Exception e) {
            System.err.println("Fatal error in demo: " + e.getMessage());
            e.printStackTrace();
        }
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
                    System.out.println("✓ " + model + ": " + content);
                    break; // Success, no need to try other models
                } else {
                    System.err.println("✗ " + model + " failed with status: " + response.getStatusCode());
                }
                
            } catch (Exception e) {
                System.err.println("✗ " + model + " error: " + e.getMessage());
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
            System.err.println("✗ Tool calls demo error: " + e.getMessage());
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
                    System.out.println("✓ Model decided to use tools:");
                    for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
                        System.out.println("  - Tool: " + toolCall.getFunction().getName());
                        System.out.println("  - Arguments: " + toolCall.getFunction().getArguments());
                    }
                } else {
                    System.out.println("✓ Model chose to respond directly: " + 
                                     (assistantMessage.getContent() != null ? assistantMessage.getContent() : "[No content]"));
                }
            } else {
                System.err.println("✗ Auto tool selection failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("✗ Auto tool selection error: " + e.getMessage());
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
                    System.out.println("✓ Model used forced tool:");
                    for (ChatToolCall toolCall : assistantMessage.getToolCalls()) {
                        System.out.println("  - Tool: " + toolCall.getFunction().getName());
                        System.out.println("  - Arguments: " + toolCall.getFunction().getArguments());
                    }
                } else {
                    System.out.println("✓ Model responded directly: " + 
                                     (assistantMessage.getContent() != null ? assistantMessage.getContent() : "[No content]"));
                }
            } else {
                System.err.println("✗ Forced tool usage failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("✗ Forced tool usage error: " + e.getMessage());
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
                    System.out.println("✓ Model requested tool call:");
                    
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
                            System.out.println("✓ Final response: " + finalResponse);
                        } else {
                            System.err.println("✗ Second request failed with status: " + secondResponse.getStatusCode());
                        }
                    }
                } else {
                    System.out.println("✓ Model responded directly without tools: " + 
                                     (assistantMessage.getContent() != null ? assistantMessage.getContent() : "[No content]"));
                }
            } else {
                System.err.println("✗ First request failed with status: " + firstResponse.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("✗ Tool call with real execution error: " + e.getMessage());
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
        Map<String, Object> params = objectMapper.readValue(arguments, Map.class);
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
        Map<String, Object> params = objectMapper.readValue(arguments, Map.class);
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
        Map<String, Object> params = objectMapper.readValue(arguments, Map.class);
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
                System.out.println("✓ Generated " + embeddingResponse.getData().size() + " embeddings");
                System.out.println("✓ Token usage: " + embeddingResponse.getUsage().getTotalTokens());
                
                // Show sample of first embedding
                List<Double> firstEmbedding = embeddingResponse.getData().get(0).getEmbedding();
                System.out.println("✓ First embedding sample: " + 
                                 firstEmbedding.subList(0, Math.min(3, firstEmbedding.size())));
            } else {
                System.err.println("✗ Embeddings failed with status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("✗ Embeddings error: " + e.getMessage());
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
                System.out.println("✓ Available models (" + modelList.getData().size() + "):");
                
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
                System.err.println("✗ Model listing failed with status: " + response.getStatusCode());
                showFallbackModels();
            }
            
        } catch (Exception e) {
            System.err.println("✗ Model listing error: " + e.getMessage());
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
                System.out.println("✓ Properly handled invalid model error: HTTP " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.out.println("✓ Properly caught exception for invalid model: " + e.getClass().getSimpleName());
        }
        
        // Test with empty API key scenario
        try {
            GroqClient invalidClient = GroqClient.builder()
                    .apiKey("")
                    .build();
                    
            GroqResponse<ModelList> response = invalidClient.models().list();
            System.out.println("✗ Unexpected success with empty API key");
            
        } catch (Exception e) {
            System.out.println("✓ Properly handled empty API key: " + e.getMessage());
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
            System.out.println("✗ Unexpected success with invalid tool configuration");
            
        } catch (Exception e) {
            System.out.println("✓ Properly handled invalid tool configuration: " + e.getClass().getSimpleName());
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
        System.out.println("  'real tools' - Use real tool execution (default: simulated)");
        System.out.println("  'sim tools' - Use simulated tool execution");
        
        Scanner scanner = new Scanner(System.in);
        boolean toolsEnabled = false;
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
                
                System.out.print("AI: ");
                GroqResponse<ChatCompletion> response = client.chat().createCompletion(request);
                
                if (response.isSuccessful()) {
                    ChatMessage assistantMessage = response.getData().getChoices().get(0).getMessage();
                    
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
}
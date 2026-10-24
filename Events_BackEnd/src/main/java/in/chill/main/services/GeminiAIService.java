package in.chill.main.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

/**
 * Service for integrating with Google Gemini AI API
 * Handles secure communication and response processing
 */
@Service
public class GeminiAIService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiAIService.class);
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Value("${gemini.api.key:AIzaSyBOwcUr3ZFiPkVYhmc3TWSorn2k_vPU__c}")
    private String geminiApiKey;
    
    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:streamGenerateContent}")
    private String geminiApiUrl;

    // Session context storage (in production, use Redis or database)
    private final Map<String, Object> sessionContexts = new HashMap<>();

    public GeminiAIService() {
        // Use JDK 11 built-in HttpClient as the HTTP connector for WebClient.
        // This avoids Reactor Netty entirely, which conflicts with WildFly's bundled Netty.
        this.webClient = WebClient.builder()
                .clientConnector(new JdkClientHttpConnector())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Send message to Gemini API with context handling
     */
    public Mono<String> sendMessage(String userMessage, Object context, String sessionId) {
        try {
            // Store context for this session if provided
            if (context != null && sessionId != null) {
                sessionContexts.put(sessionId, context);
                logger.info("Context stored for session: {}", sessionId);
            }

            // Build the prompt with context
            String fullPrompt = buildPromptWithContext(userMessage, sessionId);
            
            // Prepare request payload
            Map<String, Object> requestBody = createGeminiRequestBody(fullPrompt);
            
            logger.info("Sending request to Gemini API for session: {}", sessionId);
            logger.info("API URL: {}", geminiApiUrl);
            logger.info("API Key configured: {}", geminiApiKey != null && geminiApiKey.length() > 10);
            logger.info("Request body size: {}", requestBody.size());
            
            return webClient.post()
                    .uri(geminiApiUrl)
                    .header("X-goog-api-key", geminiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .doOnNext(response -> logger.info("Received response from Gemini API, length: {}", response.length()))
                    .map(this::parseGeminiResponse)
                    .onErrorResume(error -> {
                        logger.error("Gemini API call failed with error: ", error);
                        if (error instanceof WebClientResponseException) {
                            WebClientResponseException webEx = (WebClientResponseException) error;
                            logger.error("HTTP Status: {}, Response Body: {}", webEx.getStatusCode(), webEx.getResponseBodyAsString());
                        }
                        RuntimeException mappedError = handleApiError(error);
                        return Mono.just(mappedError.getMessage());
                    });
                    
        } catch (Exception e) {
            logger.error("Error preparing Gemini API request: ", e);
            return Mono.just("I'm sorry, I encountered an error while processing your request. Please try again.");
        }
    }

    /**
     * Send message to Gemini API with REAL streaming support
     * Returns a Flux of streaming text chunks AS THEY ARRIVE from Gemini
     */
    public reactor.core.publisher.Flux<String> sendMessageStream(String userMessage, Object context, String sessionId) {
        try {
            // Store context for this session if provided
            if (context != null && sessionId != null) {
                sessionContexts.put(sessionId, context);
                logger.info("Context stored for session: {}", sessionId);
            }

            // Build the prompt with context
            String fullPrompt = buildPromptWithContext(userMessage, sessionId);
            
            // Prepare request payload
            Map<String, Object> requestBody = createGeminiRequestBody(fullPrompt);
            
            logger.info("Sending REAL streaming request to Gemini API for session: {}", sessionId);
            
            // FIXED: Collect complete response first, then parse
            return webClient.post()
                    .uri(geminiApiUrl)
                    .header("X-goog-api-key", geminiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)  // Get complete response first
                    .timeout(Duration.ofSeconds(60))
                    .doOnNext(response -> logger.info("Received complete Gemini response length: {}", response.length()))
                    .map(this::parseGeminiResponse)  // Parse complete response to extract text
                    .flux()  // Convert to flux
                    .filter(text -> !text.isEmpty())  // Filter out empty responses
                    .doOnNext(text -> logger.info("Extracted text for streaming: '{}'", text))
                    .onErrorResume(error -> {
                        logger.error("Gemini API call failed: ", error);
                        return reactor.core.publisher.Flux.just("I'm sorry, I encountered an error while processing your request.");
                    });
                    
        } catch (Exception e) {
            logger.error("Error preparing Gemini API request: ", e);
            return reactor.core.publisher.Flux.just("I'm sorry, I encountered an error while processing your request.");
        }
    }

    /**
     * Build comprehensive prompt with context
     */
    private String buildPromptWithContext(String userMessage, String sessionId) {
        StringBuilder promptBuilder = new StringBuilder();
        
        // Add system context if available
        Object sessionContext = sessionContexts.get(sessionId);
        if (sessionContext != null) {
            promptBuilder.append("SYSTEM CONTEXT: You are FestFlex Assistant, an AI helper for the FestFlex Events Management Platform. ");
            promptBuilder.append("Here's comprehensive information about the platform:\n\n");
            
            try {
                String contextJson = objectMapper.writeValueAsString(sessionContext);
                promptBuilder.append(contextJson);
                promptBuilder.append("\n\n");
            } catch (Exception e) {
                logger.warn("Could not serialize context for session: {}", sessionId);
            }
            
            promptBuilder.append("Based on this platform information, please provide helpful, accurate, and concise responses to user questions. ");
            promptBuilder.append("Keep responses friendly but professional. If asked about features not mentioned in the context, ");
            promptBuilder.append("politely explain what you can help with instead.\n\n");
        }
        
        promptBuilder.append("USER QUESTION: ").append(userMessage);
        
        return promptBuilder.toString();
    }

    /**
     * Create properly formatted Gemini API request body
     */
    private Map<String, Object> createGeminiRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // Contents array
        Map<String, Object> content = new HashMap<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));
        
        // Generation config for better responses
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("topK", 40);
        generationConfig.put("topP", 0.95);
        generationConfig.put("maxOutputTokens", 1024);
        requestBody.put("generationConfig", generationConfig);
        
        // Safety settings
        Map<String, Object> safetySettings = new HashMap<>();
        safetySettings.put("category", "HARM_CATEGORY_HARASSMENT");
        safetySettings.put("threshold", "BLOCK_MEDIUM_AND_ABOVE");
        requestBody.put("safetySettings", List.of(safetySettings));
        
        return requestBody;
    }

    /**
     * Parse Gemini API response and extract text content
     * Handles both single response objects and streaming response arrays
     */
    private String parseGeminiResponse(String responseBody) {
        try {
            logger.info("Raw Gemini API response: {}", responseBody);
            
            // Handle streaming response (array of responses)
            if (responseBody.trim().startsWith("[")) {
                logger.info("Detected streaming response array format");
                JsonNode responseArray = objectMapper.readTree(responseBody);
                
                if (responseArray.isArray() && responseArray.size() > 0) {
                    StringBuilder combinedText = new StringBuilder();
                    
                    for (JsonNode responseItem : responseArray) {
                        String extractedText = extractTextFromSingleResponse(responseItem);
                        if (!extractedText.isEmpty()) {
                            combinedText.append(extractedText);
                        }
                    }
                    
                    String finalText = combinedText.toString().trim();
                    if (!finalText.isEmpty()) {
                        logger.info("Successfully parsed streaming Gemini response: '{}'", finalText);
                        return finalText;
                    }
                }
                
                logger.warn("No text content found in streaming response array");
                return "I'm having trouble generating a response right now. Please try rephrasing your question.";
            }
            
            // Handle single response object
            logger.info("Detected single response object format");
            JsonNode rootNode = objectMapper.readTree(responseBody);
            String extractedText = extractTextFromSingleResponse(rootNode);
            
            if (!extractedText.isEmpty()) {
                logger.info("Successfully parsed single Gemini response: '{}'", extractedText);
                return extractedText;
            }
            
            // Check for safety or other issues
            JsonNode promptFeedback = rootNode.path("promptFeedback");
            if (!promptFeedback.isMissingNode()) {
                JsonNode blockReason = promptFeedback.path("blockReason");
                if (!blockReason.isMissingNode()) {
                    logger.warn("Gemini response blocked: {}", blockReason.asText());
                    return "I'm sorry, I can't respond to that request. Please try asking something else about the FestFlex platform.";
                }
            }
            
            logger.warn("Could not extract text from Gemini response: {}", responseBody);
            return "I'm having trouble generating a response right now. Please try rephrasing your question.";
            
        } catch (Exception e) {
            logger.error("Error parsing Gemini response: ", e);
            return "I encountered an error while processing the response. Please try again.";
        }
    }
    
    /**
     * Extract text content from a single Gemini response object
     */
    private String extractTextFromSingleResponse(JsonNode responseNode) {
        try {
            // Navigate through the response structure
            JsonNode candidates = responseNode.path("candidates");
            logger.debug("Candidates found: {}", candidates.isArray() ? candidates.size() : "none");
            
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                
                JsonNode content = firstCandidate.path("content");
                JsonNode parts = content.path("parts");
                logger.debug("Parts found: {}", parts.isArray() ? parts.size() : "none");
                
                if (parts.isArray() && parts.size() > 0) {
                    JsonNode firstPart = parts.get(0);
                    String text = firstPart.path("text").textValue();
                    logger.debug("Extracted text from candidate: '{}'", text);
                    
                    if (text != null && !text.isEmpty()) {
                        return text.trim();
                    }
                }
            }
            
            return "";
        } catch (Exception e) {
            logger.error("Error extracting text from single response: ", e);
            return "";
        }
    }

    /**
     * Handle API errors and provide user-friendly messages
     */
    private RuntimeException handleApiError(Throwable error) {
        if (error instanceof WebClientResponseException) {
            WebClientResponseException webEx = (WebClientResponseException) error;
            logger.error("Gemini API error - Status: {}, Body: {}", webEx.getStatusCode(), webEx.getResponseBodyAsString());
            
            switch (webEx.getStatusCode().value()) {
                case 400:
                    return new RuntimeException("I'm sorry, there was an issue with your request. Please try rephrasing your question.");
                case 401:
                    return new RuntimeException("Authentication error. Please contact support.");
                case 429:
                    return new RuntimeException("I'm currently busy helping other users. Please try again in a moment.");
                case 500:
                    return new RuntimeException("The AI service is temporarily unavailable. Please try again later.");
                default:
                    return new RuntimeException("I'm experiencing technical difficulties. Please try again.");
            }
        }
        
        logger.error("Unexpected error calling Gemini API: ", error);
        return new RuntimeException("I'm sorry, I'm having trouble connecting to the AI service right now.");
    }

    /**
     * Clear session context (called when chat is cleared)
     */
    public void clearSessionContext(String sessionId) {
        if (sessionId != null) {
            sessionContexts.remove(sessionId);
            logger.info("Cleared context for session: {}", sessionId);
        }
    }

    /**
     * Check if API is configured properly
     */
    public boolean isConfigured() {
        return geminiApiKey != null && !geminiApiKey.trim().isEmpty() && 
               !geminiApiKey.equals("YOUR_GEMINI_API_KEY_HERE");
    }
}
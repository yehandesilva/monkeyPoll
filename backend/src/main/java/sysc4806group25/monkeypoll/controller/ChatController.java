package sysc4806group25.monkeypoll.controller;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class ChatController {

    private static final Logger logger = Logger.getLogger(ChatController.class.getName());
    private final VertexAiGeminiChatModel chatModel;

    @Autowired
    public ChatController(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping("user/ai/generate")
    @HystrixCommand(fallbackMethod = "handleAIProcessingError", commandProperties = {@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"), @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000")})
    public ResponseEntity<Map<String, ArrayList<String>>> generate(@RequestBody Map<String, String> request) {
        // Log when entering the endpoint
        logger.info("[/GENERATE ENDPOINT] Entered the endpoint to generate questions...");

        String message = request.getOrDefault("message", "").trim();

        // Validate the incoming message
        if (message.isEmpty()) {
            logger.warning("Received an empty message.");
            return new ResponseEntity<>(Map.of("questions", new ArrayList<>(Arrays.asList("Message parameter is required"))), HttpStatus.BAD_REQUEST);
        }

        // Construct the prompt message
        String promptMessage = "Please return exactly 5 survey questions (only the questions and without numbering), each on a new line. " +
                "Based on the topic: " + message;

        // Call the model
        UserMessage userMessage = new UserMessage(promptMessage);
        String generation = this.chatModel.call(userMessage);

        // Clean and validate the response
        ArrayList<String> questions = cleanAndConvertToArrayList(generation);
        if (questions.size() != 5) {
            logger.warning("The model response does not contain 5 questions.");
            return new ResponseEntity<>(Map.of("questions", new ArrayList<>(Arrays.asList("The model response must contain exactly 5 questions"))), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return the cleaned questions
        return ResponseEntity.ok(Map.of("questions", questions));
    }

    private ArrayList<String> cleanAndConvertToArrayList(String generation) {
        // Trim the response to remove any leading or trailing spaces/newlines
        String cleanedResponse = generation.trim();

        // Split by newline characters to separate individual questions
        String[] questionsArray = cleanedResponse.split("\\r?\\n");

        // If there are not exactly 5 questions, log and return an empty list
        if (questionsArray.length != 5) {
            logger.warning("Invalid number of questions returned: " + questionsArray.length);
            return new ArrayList<>();
        }

        // Convert the array into an ArrayList and return it
        return new ArrayList<>(Arrays.asList(questionsArray));
    }

    /**
     * The handleAIGenerationError method models the fallback method for the
     * @HystrixCommand, which is the generate() endpoint method. When an exception
     * is thrown in the endpoint method, it will use this method as its fallback to
     * handle the exception. In this case, this method will handle the exception by
     * returning a SERVICE_UNAVAILABLE response IF the circuit is opened. Otherwise,
     * an INTERNAL_SERVER_ERROR response is returned.
     * @param request - the incoming request
     * @return a SERVICE_UNAVAILABLE response if circuit is open, otherwise return
     * INTERNAL_SERVER_ERROR.
     */
    private ResponseEntity<Map<String, ArrayList<String>>> handleAIProcessingError(@RequestBody Map<String, String> request) {
        // Get instance of circuit for 'generate' endpoint
        HystrixCircuitBreaker circuitBreaker = HystrixCircuitBreaker.Factory.getInstance(HystrixCommandKey.Factory.asKey("generate"));

        // Check if circuit is open
        if (circuitBreaker.isOpen()) {
            // Log corresponding message
            logger.severe("[HYSTRIX FALLBACK METHOD] Exception was thrown during AI processing. CIRCUIT IS OPEN! Returning 503 response to notify frontend that this service will be temporarily unavailable...");
            return new ResponseEntity<>(Map.of("questions", new ArrayList<>(Arrays.asList("(Hystrix) An exception occurred while generating the survey questions. Service will temporarily be unavailable"))), HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            // Circuit is closed (or half-open)
            logger.severe("[HYSTRIX FALLBACK METHOD] Exception was thrown during AI processing. Returning response 500 response to frontend...");
            return new ResponseEntity<>(Map.of("questions", new ArrayList<>(Arrays.asList("(Hystrix) An exception occurred while generating the survey questions"))), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

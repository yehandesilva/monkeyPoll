package sysc4806group25.monkeypoll.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
    @HystrixCommand(fallbackMethod = "handleAIGenerationError")
    public ResponseEntity<Map<String, ArrayList<String>>> generate(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").trim();

        // Validate the incoming message
        if (message.isEmpty()) {
            logger.warning("Received an empty message.");
            return new ResponseEntity<>(Map.of("questions", new ArrayList<>(Arrays.asList("Message parameter is required"))), HttpStatus.BAD_REQUEST);
        }

        // Construct the prompt message
        String promptMessage = "Please return exactly 5 survey questions (only the questions and without numbering), each on a new line. " +
                "Based on the topic: " + message;

        try {
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

        } catch (Exception e) {
            logger.severe("EXCEPTION THROWN DURING AI PROCESSING");
            throw new RuntimeException();
        }
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
}

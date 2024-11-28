package sysc4806group25.monkeypoll.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


@RestController
public class ChatController {

    private final VertexAiGeminiChatModel chatModel;

    @Autowired
    public ChatController(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }


    @PostMapping("user/ai/generate")
    public Map<String, ArrayList<String>> generate(@RequestBody Map<String, String> request) {
        // Get the message or use a default one
        String message = request.getOrDefault("message", "Tell me a joke");

        // Construct the prompt message
        String promptMessage = "Please return exactly 5 survey questions (only the questions), each on a new line. " +
                "Based on the topic: " + message;

        // Simulate the call to the model (replace with actual API call)
        UserMessage userMessage = new UserMessage(promptMessage);
        String generation = this.chatModel.call(userMessage);

        // Clean and convert the response to an ArrayList
        ArrayList<String> questions = cleanAndConvertToArrayList(generation);

        // Return the cleaned questions as a map
        return Map.of("questions", questions);
    }

    private ArrayList<String> cleanAndConvertToArrayList(String generation) {
        // Trim the response to remove any leading or trailing spaces/newlines
        String cleanedResponse = generation.trim();

        // Split by newline characters to separate individual questions
        String[] questionsArray = cleanedResponse.split("\\r?\\n");

        // Convert the array into an ArrayList and return it
        return new ArrayList<>(Arrays.asList(questionsArray));
    }
}
package sysc4806group25.monkeypoll.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public class SurveyResponseDTO {
    private String email;
    private List<ResponseDTO> responses;

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ResponseDTO> getResponses() {
        return responses;
    }

    public void setResponses(List<ResponseDTO> responses) {
        this.responses = responses;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TextResponseDTO.class, name = "text"),
            @JsonSubTypes.Type(value = NumberResponseDTO.class, name = "number"),
            @JsonSubTypes.Type(value = ChoiceResponseDTO.class, name = "choice")
    })
    public static abstract class ResponseDTO {
        private long questionId;

        // Getters and Setters

        public long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(long questionId) {
            this.questionId = questionId;
        }
    }

    public static class TextResponseDTO extends ResponseDTO {
        private String response;

        // Getters and Setters

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    public static class NumberResponseDTO extends ResponseDTO {
        private int response;

        // Getters and Setters

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }
    }

    public static class ChoiceResponseDTO extends ResponseDTO {
        private String response;

        // Getters and Setters

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}
package sysc4806group25.monkeypoll.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * The TextQuestion class models the text question entity for the MonkeyPoll application.
 * A type of question that can be asked on a survey is a text question expecting a text response from the user.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
public class TextQuestion extends Question {

    /**
     * Empty constructor with no args for JPA
     */
    public TextQuestion() {
        super();
    }

    /**
     * Constructor for testing purposes
     */
    public TextQuestion(String question, Survey survey) {
        super(question, survey);
    }

    // Fields
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextResponse> responses = new ArrayList<>();

    // GETTERS

    /**
     * @return responses to the question.
     */
    public List<TextResponse> getResponses() {
        return responses;
    }

    // SETTERS

    /**
     * @param responses - responses to the question.
     */
    public void setResponses(List<TextResponse> responses) {
        this.responses = responses;
    }

    /**
     * @param response - user response to the question.
     */
    public void addResponse(TextResponse response) {
        this.responses.add(response);
        response.setQuestion(this);
    }

    /**
     * @param response - user response to the question.
     */
    public void removeResponse(TextResponse response) {
        this.responses.remove(response);
        response.setQuestion(null);
    }
}

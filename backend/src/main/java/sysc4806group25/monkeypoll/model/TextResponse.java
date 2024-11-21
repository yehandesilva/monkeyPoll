package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The TextResponse class models the text response entity for the MonkeyPoll application.
 * A user can respond to a text question with a text response.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
@SequenceGenerator(name="textResponseSeq")
public class TextResponse {

    // Fields
    @Id
    @GeneratedValue(generator = "textResponseSeq")
    private long responseId;
    private String response;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private TextQuestion question;

    // GETTERS

    /**
     * @return unique identifier of the response.
     */
    public long getResponseId() {
        return responseId;
    }

    /**
     * @return user response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * @return corresponding question to response.
     */
    public TextQuestion getQuestion() {
        return question;
    }

    // SETTERS

    /**
     * @param responseId - unique identifier of the response.
     */
    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    /**
     * @param response - user response.
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @param question - corresponding question to the response.
     */
    public void setQuestion(TextQuestion question) {
        this.question = question;
    }
}

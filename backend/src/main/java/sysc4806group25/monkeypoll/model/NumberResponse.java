package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The NumberResponse class models the number response entity for the MonkeyPoll application.
 * A user can respond to a text question with a number response within the given range.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
@SequenceGenerator(name="numberResponseSeq")
public class NumberResponse implements Serializable {

    // Fields
    @Id
    @GeneratedValue(generator = "numberResponseSeq")
    private long responseId;
    private int response;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private NumberQuestion question;

    /**
     * Empty constructor with no args for JPA
     */
    public NumberResponse() {

    }

    /**
     * Constructor for testing purposes
     */
    public NumberResponse(int response, NumberQuestion question) {
        this.response = response;
        this.question = question;
    }

    // GETTERS

    /**
     * @return unique identifier for the response.
     */
    public long getResponseId() {
        return responseId;
    }

    /**
     * @return user response to number question.
     */
    public int getResponse() {
        return response;
    }

    /**
     * @return corresponding question to response.
     */
    public NumberQuestion getQuestion() {
        return question;
    }

    // SETTERS

    /**
     * @param responseId - unique identifier for the response.
     */
    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    /**
     * @param response - user response to number question.
     */
    public void setResponse(int response) {
        this.response = response;
    }

    /**
     * @param question - corresponding question to response.
     */
    public void setQuestion(NumberQuestion question) {
        this.question = question;
    }
}

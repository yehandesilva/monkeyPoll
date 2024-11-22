package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

/**
 * The ChoiceResponse class models the choice response entity for the MonkeyPoll application.
 * A user can respond to a choice question with a choice response from the given options.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
@SequenceGenerator(name="choiceResponseSeq")
public class ChoiceResponse {

    // Fields
    @Id
    @GeneratedValue(generator = "choiceResponseSeq")
    private long responseId;

    @ManyToOne
    @JoinColumn(name = "optionId", nullable = false)
    private ChoiceOption response;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private ChoiceQuestion question;

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
    public ChoiceOption getResponse() {
        return response;
    }

    /**
     * @return corresponding question to response.
     */
    public ChoiceQuestion getQuestion() {
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
    public void setResponse(ChoiceOption response) {
        this.response = response;
    }

    /**
     * @param question - corresponding question to response.
     */
    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }
}

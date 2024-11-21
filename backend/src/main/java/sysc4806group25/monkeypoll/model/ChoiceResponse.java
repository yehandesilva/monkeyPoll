package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@SequenceGenerator(name="choiceResponseSeq")
public class ChoiceResponse implements Serializable {

    // Fields
    private long responseId;
    private long response;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private ChoiceQuestion question;

    // GETTERS

    @Id
    @GeneratedValue(generator = "choiceResponseSeq")
    public long getResponseId() {
        return responseId;
    }

    public long getResponse() {
        return response;
    }

    public ChoiceQuestion getQuestion() {
        return question;
    }

    // SETTERS

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public void setResponse(long response) {
        this.response = response;
    }

    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }
}

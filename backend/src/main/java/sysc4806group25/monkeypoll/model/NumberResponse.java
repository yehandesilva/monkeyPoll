package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@SequenceGenerator(name="numberResponseSeq")
public class NumberResponse implements Serializable {

    // Fields
    private long responseId;
    private int response;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private NumberQuestion question;

    // GETTERS

    @Id
    @GeneratedValue(generator = "numberResponseSeq")
    public long getResponseId() {
        return responseId;
    }

    public int getResponse() {
        return response;
    }

    public NumberQuestion getQuestion() {
        return question;
    }

    // SETTERS

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public void setQuestion(NumberQuestion question) {
        this.question = question;
    }
}

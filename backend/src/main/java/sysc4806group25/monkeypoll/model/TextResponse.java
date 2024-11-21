package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@SequenceGenerator(name="textResponseSeq")
public class TextResponse implements Serializable {

    // Fields
    private long responseId;
    private String response;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private TextQuestion question;

    // GETTERS

    @Id
    @GeneratedValue(generator = "textResponseSeq")
    public long getResponseId() {
        return responseId;
    }

    public String getResponse() {
        return response;
    }

    public TextQuestion getQuestion() {
        return question;
    }

    // SETTERS

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setQuestion(TextQuestion question) {
        this.question = question;
    }
}

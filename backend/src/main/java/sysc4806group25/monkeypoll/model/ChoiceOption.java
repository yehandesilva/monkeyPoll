package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@SequenceGenerator(name="choiceOptionSeq")
public class ChoiceOption implements Serializable {

    // Fields
    private long choiceOptionId;
    private String description;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private ChoiceQuestion question;

    // GETTERS

    @Id
    @GeneratedValue(generator="choiceOptionSeq")
    public long getChoiceOptionId() {
        return choiceOptionId;
    }

    public String getDescription() {
        return description;
    }

    public ChoiceQuestion getQuestion() {
        return question;
    }

    // SETTERS

    public void setChoiceOptionId(long choiceOptionId) {
        this.choiceOptionId = choiceOptionId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }
}

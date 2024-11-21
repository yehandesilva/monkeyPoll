package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

@MappedSuperclass
@SequenceGenerator(name="questionSeq")
public class Question implements Serializable {

    // Fields
    private long questionId;
    private String question;

    @ManyToOne
    @JoinColumn(name = "surveyId", nullable = false)
    private Survey survey;

    public Question() {

    }

    // GETTERS

    @Id
    @GeneratedValue(generator="questionSeq")
    public long getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    public Survey getSurvey() {
        return survey;
    }

    // SETTERS

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}

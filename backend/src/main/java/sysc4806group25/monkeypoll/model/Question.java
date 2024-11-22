package sysc4806group25.monkeypoll.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * The Question class models the question entity for the MonkeyPoll application.
 * Surveys are composed of a variety of questions. Because there are subtypes of questions,
 * inheritance is used with Question being the super class.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name="questionSeq")
public class Question {

    // Fields
    @Id
    @GeneratedValue(generator="questionSeq")
    private long questionId;
    private String question;

    @ManyToOne
    @JoinColumn(name = "surveyId", nullable = false)
    @JsonBackReference
    private Survey survey;

    /**
     * Empty constructor with no args for JPA
     */
    public Question() {

    }

    /**
     * Constructor for testing purposes
     */
    public Question(String question, Survey survey) {
        this.question = question;
        this.survey = survey;
    }

    // GETTERS

    /**
     * @return unique identifier of the question
     */
    public long getQuestionId() {
        return questionId;
    }

    /**
     * @return question statement.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return corresponding survey that holds the question.
     */
    public Survey getSurvey() {
        return survey;
    }

    // SETTERS

    /**
     * @param questionId - unique identifier of the question.
     */
    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    /**
     * @param question - question statement of the question.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @param survey - corresponding survey of the question.
     */
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}

package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@SequenceGenerator(name="surveyCompletionSeq")
public class SurveyCompletion {

    // Fields
    private long surveyCompletionId;
    private String email;

    @ManyToOne
    @JoinColumn(name = "surveyId", nullable = false)
    private Survey survey;

    /**
     * Empty constructor with no args for JPA
     */
    public SurveyCompletion() {

    }

    public SurveyCompletion(String email, Survey survey) {
        this.email = email;
        this.survey = survey;
    }

    // GETTER methods

    /**
     * @return unique identifier for survey completion.
     */
    @Id
    @GeneratedValue(generator="surveyCompletionSeq")
    public long getSurveyCompletionId() {
        return this.surveyCompletionId;
    }

    /**
     * @return email of survey completion.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return survey that was completed.
     */
    public Survey getSurvey() {
        return survey;
    }

    // SETTER methods

    /**
     * @param surveyCompletionId - Unique identifier of survey completion record.
     */
    public void setSurveyCompletionId(long surveyCompletionId) {
        this.surveyCompletionId = surveyCompletionId;
    }

    /**
     * @param email - Email for response.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param survey - Survey completed.
     */
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}

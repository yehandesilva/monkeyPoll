package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

/**
 * The Survey class models the survey entity for the MonkeyPoll application.
 * Users can create surveys composed of multiple survey questions and share them with other people to complete.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
@SequenceGenerator(name="surveySeq")
public class Survey {

    // Fields
    private long surveyId;
    private String description;
    private Boolean closed;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    /**
     * Empty constructor with no args for JPA
     */
    public Survey() {

    }

    // GETTER methods

    @Id
    @GeneratedValue(generator="surveySeq")
    public long getSurveyId() {
        return this.surveyId;
    }

    /**
     * @return account who created the survey.
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * @return survey's description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return true if the survey is closed, false otherwise
     */
    public Boolean getClosed() {
        return this.closed;
    }

    // Setter methods

    /**
     * @param surveyId - survey's unique identifier.
     */
    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    /**
     * @param account - account the survey belongs to.
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @param description - description of the survey.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param closed - true if the survey is closed, false otherwise.
     */
    public void setClosed(Boolean closed) {
        this.closed = closed;
    }
}

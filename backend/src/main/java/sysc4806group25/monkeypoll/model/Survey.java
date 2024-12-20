package sysc4806group25.monkeypoll.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Id
    @GeneratedValue(generator="surveySeq")
    private long surveyId;
    private String description;
    private Boolean closed = false;

    // Do not print account info with each survey
    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SurveyCompletion> completions = new ArrayList<>();

    // managed reference prevents Json from being printed recursively (bidirectional)
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Question> questions;

    /**
     * Empty constructor with no args for JPA
     */
    public Survey() {

    }

    /**
     * Constructor for testing purposes
     */
    public Survey(String description, boolean closed, Account account) {
        this.description = description;
        this.closed = closed;
        this.account = account;
        this.questions = new ArrayList<>();
    }

    /**
     * Constructor for testing purposes
     */
    public Survey(Account account) {
        this.account = account;
    }

    // GETTER methods

    /**
     * @return unique identifier of survey.
     */
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

    /**
     * @return successful completions of the survey
     */
    public List<SurveyCompletion> getCompletions() {
        return this.completions;
    }

    /**
     * @return successful completions of the survey
     */
    public List<Question> getQuestions() {
        return this.questions;
    }

    // SETTER methods

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

    /**
     * @param completions - completions of the survey.
     */
    public void setCompletions(List<SurveyCompletion> completions) {
        this.completions = completions;
    }

    /**
     * @param questions - questions of the survey.
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * @param question - question to add to survey.
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setSurvey(this);
    }

    /**
     * @param question - question to remove from survey.
     */
    public void removeQuestion(Question question) {
        this.questions.remove(question);
        question.setSurvey(null);
    }

    /**
     * @param completion - completion to add to survey.
     */
    public void addCompletion(SurveyCompletion completion) {
        this.completions.add(completion);
        completion.setSurvey(this);
    }

    /**
     * @param completion - completion to remove from survey.
     */
    public void removeCompletion(SurveyCompletion completion) {
        this.completions.remove(completion);
        completion.setSurvey(null);
    }

}

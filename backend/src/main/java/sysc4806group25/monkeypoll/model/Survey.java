package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

@Entity
@SequenceGenerator(name="surveySeq")
public class Survey {

    private long surveyId;
    private String description;
    private Boolean closed;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    public Survey() {

    }

    // GETTER methods

    @Id
    @GeneratedValue(generator="surveySeq")
    public long getSurveyId() {
        return this.surveyId;
    }

    public Account getAccount() {
        return this.account;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getClosed() {
        return this.closed;
    }

    // Setter methods

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }
}

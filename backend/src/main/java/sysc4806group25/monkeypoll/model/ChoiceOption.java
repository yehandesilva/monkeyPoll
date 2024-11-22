package sysc4806group25.monkeypoll.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The ChoiceOption class models the choice option entity for the MonkeyPoll application.
 * A choice question requires a list of possible options to choose from.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
@SequenceGenerator(name="choiceOptionSeq")
public class ChoiceOption {

    // Fields
    @Id
    @GeneratedValue(generator="choiceOptionSeq")
    private long choiceOptionId;
    private String description;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private ChoiceQuestion question;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoiceResponse> responses = new ArrayList<>();

    // GETTERS

    /**
     * @return unique identifier for choice option.
     */
    public long getChoiceOptionId() {
        return choiceOptionId;
    }

    /**
     * @return description of the choice option.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return corresponding question of the choice option.
     */
    public ChoiceQuestion getQuestion() {
        return question;
    }

    /**
     * @return responses that chose choice option.
     */
    public List<ChoiceResponse> getResponses() {
        return responses;
    }

    // SETTERS

    /**
     * @param choiceOptionId - unique identifier for choice option.
     */
    public void setChoiceOptionId(long choiceOptionId) {
        this.choiceOptionId = choiceOptionId;
    }

    /**
     * @param description - description of the choice option.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param question - corresponding question of the choice option.
     */
    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }

    /**
     * @param responses - responses that chose choice option.
     */
    public void setResponses(List<ChoiceResponse> responses) {
        this.responses = responses;
    }

    /**
     * @param response - response that chose choice option.
     */
    public void addResponse(ChoiceResponse response) {
        this.responses.add(response);
        response.setResponse(this);
    }

    /**
     * @param response - response that no longer chose choice option.
     */
    public void removeResponse(ChoiceResponse response) {
        this.responses.remove(response);
        response.setResponse(null);
    }
}

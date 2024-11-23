package sysc4806group25.monkeypoll.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * The ChoiceQuestion class models the choice question entity for the MonkeyPoll application.
 * A type of question that can be asked on a survey is a choice question expecting a user to choose one option from a list
 * of options.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
public class ChoiceQuestion extends Question {

    // Fields
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoiceOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChoiceResponse> responses = new ArrayList<>();

    /**
     * Empty constructor with no args for JPA
     */
    public ChoiceQuestion() {
        super();
    }

    /**
     * Constructor for testing purposes
     */
    public ChoiceQuestion(String question, Survey survey) {
        super(question, survey);
    }

    // GETTERS

    /**
     * @return options user can choose from.
     */
    public List<ChoiceOption> getOptions() {
        return options;
    }

    /**
     * @return responses to the choice question.
     */
    public List<ChoiceResponse> getResponses() {
        return responses;
    }

    // SETTERS

    /**
     * @param options - options user can choose from.
     */
    public void setOptions(List<ChoiceOption> options) {
        this.options = options;
    }

    /**
     * @param option - option to add to question.
     */
    public void addOption(ChoiceOption option) {
        this.options.add(option);
        option.setQuestion(this);
    }

    /**
     * @param option - option to remove from question.
     */
    public void removeOption(ChoiceOption option) {
        this.options.remove(option);
        option.setQuestion(null);
    }

    /**
     * @param responses - responses to the choice question.
     */
    public void setResponses(List<ChoiceResponse> responses) {
        this.responses = responses;
    }

    /**
     * @param response - response to add to the question.
     */
    public void addResponse(ChoiceResponse response) {
        this.responses.add(response);
        response.setQuestion(this);
    }

    /**
     * @param response - response to remove from the question.
     */
    public void removeResponse(ChoiceResponse response) {
        this.responses.remove(response);
        response.setQuestion(null);
    }
}

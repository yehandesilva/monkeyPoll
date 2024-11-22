package sysc4806group25.monkeypoll.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * The NumberQuestion class models the number question entity for the MonkeyPoll application.
 * A type of question that can be asked on a survey is a number question expecting a number response from the user that
 * is between a max and min value.
 *
 * @author Yehan De Silva
 * @date November 21st, 2024
 */
@Entity
public class NumberQuestion extends Question {

    /**
     * Empty constructor with no args for JPA
     */
    public NumberQuestion() {
        super();
    }

    /**
     * Constructor for testing purposes
     */
    public NumberQuestion(String string, Survey survey) {
        super(string, survey);
    }

    // Fields
    private int minValue;
    private int maxValue;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NumberResponse> responses = new ArrayList<>();

    // GETTERS

    /**
     * @return minimum value of the number question.
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * @return maximum value of the number question.
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * @return responses to the number question.
     */
    public List<NumberResponse> getResponses() {
        return responses;
    }

    // SETTERS

    /**
     * @param minValue - minimum value of the number question.
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * @param maxValue - maximum value of the number question.
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @param responses - responses to the number question.
     */
    public void setResponses(List<NumberResponse> responses) {
        this.responses = responses;
    }

    /**
     * @param response - response to add to number question.
     */
    public void addResponse(NumberResponse response) {
        this.responses.add(response);
        response.setQuestion(this);
    }

    /**
     * @param response - response to add to number question.
     */
    public void removeResponse(NumberResponse response) {
        this.responses.remove(response);
        response.setQuestion(null);
    }
}

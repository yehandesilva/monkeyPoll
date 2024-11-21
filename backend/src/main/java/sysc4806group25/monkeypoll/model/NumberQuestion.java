package sysc4806group25.monkeypoll.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NumberQuestion extends Question {

    // Fields
    private int minValue;
    private int maxValue;

    private List<NumberResponse> responses = new ArrayList<>();

    // GETTERS

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<NumberResponse> getResponses() {
        return responses;
    }

    // SETTERS

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setResponses(List<NumberResponse> responses) {
        this.responses = responses;
    }

    public void addResponse(NumberResponse response) {
        this.responses.add(response);
        response.setQuestion(this);
    }

    public void removeResponse(NumberResponse response) {
        this.responses.remove(response);
        response.setQuestion(null);
    }
}

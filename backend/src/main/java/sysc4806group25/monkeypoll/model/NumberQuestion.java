package sysc4806group25.monkeypoll.model;

import jakarta.persistence.Entity;

@Entity
public class NumberQuestion extends Question {

    // Fields
    private int minValue;
    private int maxValue;

    // GETTERS

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    // SETTERS

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}

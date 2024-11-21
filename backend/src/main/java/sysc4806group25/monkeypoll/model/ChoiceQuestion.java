package sysc4806group25.monkeypoll.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ChoiceQuestion extends Question {

    // Fields
    private List<ChoiceOption> options = new ArrayList<>();

    // GETTERS

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ChoiceOption> getOptions() {
        return options;
    }

    // SETTERS

    public void setOptions(List<ChoiceOption> options) {
        this.options = options;
    }

    public void addOption(ChoiceOption option) {
        this.options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(ChoiceOption option) {
        this.options.remove(option);
        option.setQuestion(null);
    }
}

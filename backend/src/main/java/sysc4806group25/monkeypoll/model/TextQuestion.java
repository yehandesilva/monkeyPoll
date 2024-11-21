package sysc4806group25.monkeypoll.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TextQuestion extends Question {

    // Fields
    private List<TextResponse> responses = new ArrayList<>();

    // GETTERS

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TextResponse> getResponses() {
        return responses;
    }

    // SETTERS

    public void setResponses(List<TextResponse> responses) {
        this.responses = responses;
    }

    public void addResponse(TextResponse response) {
        this.responses.add(response);
        response.setQuestion(this);
    }

    public void removeResponse(TextResponse response) {
        this.responses.remove(response);
        response.setQuestion(null);
    }
}

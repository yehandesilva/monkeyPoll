package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;
import sysc4806group25.monkeypoll.model.ChoiceQuestion;

public interface ChoiceQuestionRepository extends CrudRepository<ChoiceQuestion, Long> {
}

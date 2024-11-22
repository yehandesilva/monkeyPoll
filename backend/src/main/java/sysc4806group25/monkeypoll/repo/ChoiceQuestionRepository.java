package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;
import sysc4806group25.monkeypoll.model.ChoiceQuestion;

/**
 * The ChoiceQuestionRepository is responsible for managing the creation and modification of
 * ChoiceQuestion entities for persistent storage.
 *
 * @author Yehan De Silva, 101185388
 * @date November 21st, 2024
 */
public interface ChoiceQuestionRepository extends CrudRepository<ChoiceQuestion, Long> {
}

package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;

import sysc4806group25.monkeypoll.model.NumberQuestion;

/**
 * The NumberQuestionRepository is responsible for managing the creation and modification of
 * NumberQuestion entities for persistent storage.
 *
 * @author Yehan De Silva, 101185388
 * @date November 21st, 2024
 */
public interface NumberQuestionRepository extends CrudRepository<NumberQuestion, Long> {
}

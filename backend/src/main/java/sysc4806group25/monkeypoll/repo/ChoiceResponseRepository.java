package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;
import sysc4806group25.monkeypoll.model.ChoiceResponse;

/**
 * The ChoiceResponseRepository is responsible for managing the creation and modification of
 * ChoiceResponse entities for persistent storage.
 *
 * @author Yehan De Silva, 101185388
 * @date November 21st, 2024
 */
public interface ChoiceResponseRepository extends CrudRepository<ChoiceResponse, Long> {
}

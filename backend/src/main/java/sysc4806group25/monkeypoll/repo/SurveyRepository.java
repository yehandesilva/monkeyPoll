package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sysc4806group25.monkeypoll.model.Survey;

/**
 * The SurveyRepository is responsible for managing the creation and modification of
 * Survey entities for persistent storage.
 *
 * @author Yehan De Silva, 101185388
 * @date November 21st, 2024
 */
@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long>{

}

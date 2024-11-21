package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sysc4806group25.monkeypoll.model.SurveyCompletion;

@Repository
public interface SurveyCompletionRepository extends CrudRepository<SurveyCompletion, Long> {
}

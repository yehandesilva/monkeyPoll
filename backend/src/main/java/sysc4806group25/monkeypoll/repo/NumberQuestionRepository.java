package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;

import sysc4806group25.monkeypoll.model.NumberQuestion;

public interface NumberQuestionRepository extends CrudRepository<NumberQuestion, Long> {
}

package sysc4806group25.monkeypoll.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sysc4806group25.monkeypoll.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
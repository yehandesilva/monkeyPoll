package sysc4806group25.monkeypoll.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sysc4806group25.monkeypoll.model.Question;

/**
 * Repository interface for Question entities.
 * Extends JpaRepository to provide CRUD operations for Question entities.
 * Provides common interface for question types.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
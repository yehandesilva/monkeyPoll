package sysc4806group25.monkeypoll.repo;

import org.springframework.data.repository.CrudRepository;
import sysc4806group25.monkeypoll.model.NumberResponse;

public interface NumberResponseRepository extends CrudRepository<NumberResponse, Long> {
}

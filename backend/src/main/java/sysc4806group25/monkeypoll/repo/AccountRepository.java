package sysc4806group25.monkeypoll.repo;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import sysc4806group25.monkeypoll.model.Account;

import java.util.Optional;

/**
 * The AccountRepository is responsible for managing the creation and modification of
 * Account entities for persistent storage.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date November 8th, 2024
 */

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    // TODO: Any custom queries can be added here

    Optional<Account> findByEmail(String email);
}

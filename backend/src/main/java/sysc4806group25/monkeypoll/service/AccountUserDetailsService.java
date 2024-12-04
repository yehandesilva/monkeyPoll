package sysc4806group25.monkeypoll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.controller.AccountController;

import java.util.Optional;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Account> account = accountRepository.findByEmail(username);

        if (account.isPresent()) {
            return account.get();
        }

        throw new UsernameNotFoundException("Invalid user with username: "+ username);
    }

    /**
     * Alternative to loadUserByUsername() returning a boolean instead of throwing UsernameNotFoundException,
     * so that we don't have to handle the exception every time we want to check an email from other classes.
     *
     * @param email The email to check
     * @return true if email does not exist in any Account, otherwise false
     */
    public boolean isEmailUnique(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        return account.isEmpty();
    }

    public Account registerNewAccount(AccountController.RegisterRequest registerRequest) {
        Account account = new Account();
        account.setFirstName(registerRequest.firstName());
        account.setLastName(registerRequest.lastName());
        account.setEmail(registerRequest.email());
        account.setPassword(passwordEncoder.encode(registerRequest.password()));
        return accountRepository.save(account);
    }


}

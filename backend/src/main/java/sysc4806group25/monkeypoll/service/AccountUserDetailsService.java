package sysc4806group25.monkeypoll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sysc4806group25.monkeypoll.controller.AccountController;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    //TODO: Need to merge Account Entity code to for this to work; Account entity must implement UserDetails
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Account> account = accountRepository.findByEmail(username);
        if (account.isPresent()) {
            return account.get();
        }

        throw new UsernameNotFoundException("Invalid user with username: "+ username);
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

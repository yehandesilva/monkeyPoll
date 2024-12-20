package sysc4806group25.monkeypoll.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.service.AccountUserDetailsService;

import java.util.Optional;

@RestController
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Autowired
    AccountUserDetailsService accountUserDetailsService;

    @Autowired
    AccountRepository accountRepository;

    private SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AccountController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Account loginAccount(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.email(), loginRequest.password());

        try {
            Authentication authentication = this.authenticationManager.authenticate(token);
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            this.securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            return (Account) context.getAuthentication().getPrincipal();

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username or Password.");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAccount(@RequestBody RegisterRequest registerRequest) {

        if (accountUserDetailsService.isEmailUnique(registerRequest.email())) {
            Account newAccount = accountUserDetailsService.registerNewAccount(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Account successfully created!\"}");
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with that email already exists.");
    }

    @GetMapping("/user")
    public ResponseEntity<Optional<Account>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Account currentUser) {
            return ResponseEntity.ok(accountRepository.findById(currentUser.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public record LoginRequest(String email, String password) {}
    public record RegisterRequest(String email, String password, String firstName, String lastName) {}
}

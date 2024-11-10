package sysc4806group25.monkeypoll.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sysc4806group25.monkeypoll.Account;
import sysc4806group25.monkeypoll.service.AccountUserDetailsService;

@RestController
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Autowired
    AccountUserDetailsService accountUserDetailsService;

    public AccountController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Account loginAccount(@RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.email(), loginRequest.password());

        try {
            Authentication authentication = this.authenticationManager.authenticate(token);
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            this.securityContextHolderStrategy.setContext(context);
            return (Account) context.getAuthentication().getPrincipal();

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username or Password.");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAccount(@RequestBody RegisterRequest registerRequest) {
        //TODO: Validate inputs of RegisterRequest, which should be done in the record or a DTO

        if (accountUserDetailsService.isEmailUnique(registerRequest.email())) {
            Account newAccount = accountUserDetailsService.registerNewAccount(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Account successfully created!\"}");
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with that email already exists.");
    }

    public record LoginRequest(String email, String password) {}
    public record RegisterRequest(String email, String password, String firstName, String lastName) {}
}

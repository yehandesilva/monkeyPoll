package sysc4806group25.monkeypoll.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.service.AccountUserDetailsService;
import sysc4806group25.monkeypoll.service.SurveyService;

import java.util.Optional;

@RestController
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Autowired
    AccountUserDetailsService accountUserDetailsService;

    @Autowired
    SurveyService surveyService;

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
        //TODO: Validate inputs of RegisterRequest, which should be done in the record or a DTO

        if (accountUserDetailsService.isEmailUnique(registerRequest.email())) {
            Account newAccount = accountUserDetailsService.registerNewAccount(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Account successfully created!\"}");
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with that email already exists.");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}/survey/{surveyId}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable long userId, @PathVariable long surveyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account authenticatedAccount = (Account) authentication.getPrincipal();

        if (authenticatedAccount.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Survey> survey = surveyService.getSurveyById(surveyId);
        return survey.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    public record LoginRequest(String email, String password) {}
    public record RegisterRequest(String email, String password, String firstName, String lastName) {}
}

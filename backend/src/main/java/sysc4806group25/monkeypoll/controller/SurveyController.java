package sysc4806group25.monkeypoll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.Question;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.service.SurveyService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    private static final Logger logger = Logger.getLogger(SurveyController.class.getName());

    /**
     * Retrieves a survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return a ResponseEntity containing the survey if found, or a 404 status if not found
     */
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<?> getSurveyById(@PathVariable long surveyId) {
        Optional<Survey> survey = surveyService.getSurveyById(surveyId);
        return survey.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new survey.
     *
     * @param survey the survey to be created
     * @return a ResponseEntity containing the created survey with a 201 status
     */
    @PostMapping("/{userId}/survey/create")
    public ResponseEntity<String> createSurvey(@RequestBody Survey survey, @PathVariable long userId) {
        logger.info("Received survey creation request: " + survey);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // check if the Authentication object is an instance of Account before casting
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof Account authenticatedAccount)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"User is not authenticated.\"}");
        }

        if (authenticatedAccount.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"User ID does not match the authenticated user.\"}");
        }

        survey.setAccount(authenticatedAccount);
        Survey createdSurvey = surveyService.createSurvey(survey);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Survey successfully created!\", \"surveyId\":" + createdSurvey.getSurveyId() + "}");
    }
}

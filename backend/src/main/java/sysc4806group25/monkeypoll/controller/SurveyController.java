package sysc4806group25.monkeypoll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sysc4806group25.monkeypoll.model.*;
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
    @PostMapping("/user/survey")
    public ResponseEntity<String> createSurvey(@RequestBody Survey survey) {
        logger.info("Received survey creation request: " + survey);
        Account authenticatedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Ensure Account is set on the Survey
        survey.setAccount(authenticatedAccount);

        // Create the Survey and associate it with the Account
        Survey createdSurvey = surveyService.createSurvey(survey);

        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Survey successfully created!\", \"surveyId\":" + createdSurvey.getSurveyId() + "}");
    }

    @PostMapping("/survey/{surveyId}")
    public ResponseEntity<String> submitSurveyResponse(@RequestBody SurveyCompletion surveyCompletion, @PathVariable long surveyId) {
        Optional<Survey> surveyOpt = surveyService.getSurveyById(surveyId);
        if (surveyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Survey not found!\"}");
        }

        Survey survey = surveyOpt.get();

        if (survey.getClosed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Survey is closed!\"}");
        }

        Optional<SurveyCompletion> existingCompletion = surveyService.findSurveyCompletionBySurveyAndEmail(survey, surveyCompletion.getEmail());
        if (existingCompletion.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"This email has already submitted the survey!\"}");
        }

        // Set the survey property on the SurveyCompletion entity
        surveyCompletion.setSurvey(survey);

        surveyService.saveSurveyResponse(surveyCompletion);

        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Survey response successfully submitted!\"}");
    }



    //    @GetMapping("/questions")
//    public ResponseEntity<List<Question>> getAllQuestions() {
//        List<Question> questions = surveyService.getAllQuestions();
//        return ResponseEntity.ok(questions);
//    }
//    @GetMapping("/questions/responses")
//    public ResponseEntity<List<Question>> getAllQuestionsWithResponses() {
//        List<Question> questions = surveyService.getAllQuestionsWithResponses();
//        return ResponseEntity.ok(questions);
//    }

    @GetMapping("/survey/{surveyId}/responses")
    public ResponseEntity<?> getSurveyResponses(@PathVariable long surveyId) {
        Optional<Survey> survey = surveyService.getSurveyById(surveyId);
        if (survey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Survey not found!\"}");
        }

        List<SurveyCompletion> responses = surveyService.getSurveyResponses(surveyId);
        return ResponseEntity.ok(responses);
    }

}

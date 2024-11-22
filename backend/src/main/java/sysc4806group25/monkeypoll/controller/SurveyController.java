package sysc4806group25.monkeypoll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sysc4806group25.monkeypoll.model.Question;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.service.SurveyService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    /**
     * Retrieves a survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return a ResponseEntity containing the survey if found, or a 404 status if not found
     */
    @GetMapping("/{surveyId}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable long surveyId) {
        Optional<Survey> survey = surveyService.getSurveyById(surveyId);
        System.out.println("surveys:" + surveyService.findAll());
        return survey.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Retrieves the questions of a survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return a ResponseEntity containing the list of questions in the survey
     */
    @GetMapping("/{surveyId}/questions")
    public ResponseEntity<List<Question>> getSurveyQuestions(@PathVariable long surveyId) {
        List<Question> questions = surveyService.getSurveyQuestions(surveyId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Creates a new survey.
     *
     * @param survey the survey to be created
     * @return a ResponseEntity containing the created survey with a 201 status
     */
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {
        Survey createdSurvey = surveyService.createSurvey(survey.getDescription(), survey.getClosed());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSurvey);
    }
}

package sysc4806group25.monkeypoll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sysc4806group25.monkeypoll.model.*;
import sysc4806group25.monkeypoll.repo.ChoiceQuestionRepository;
import sysc4806group25.monkeypoll.repo.SurveyCompletionRepository;
import sysc4806group25.monkeypoll.repo.SurveyRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    @Autowired
    private SurveyCompletionRepository surveyCompletionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ChoiceQuestionRepository choiceQuestionRepository;

    /**
     * Creates a new survey.
     *
     * @param survey the survey to be created
     * @return the created survey
     */
    public Survey createSurvey(Survey survey) {
        // Ensure the questions reference the correct Survey before saving
        for (Question question : survey.getQuestions()) {
            question.setSurvey(survey);
        }

        // Save the survey (and cascade the save operation to questions, if configured correctly)
        return surveyRepository.save(survey);
    }
    /**
     * Retrieves a survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return an Optional containing the survey if found, or empty if not found
     */
    public Optional<Survey> getSurveyById(long surveyId) {
        return surveyRepository.findById(surveyId);
    }

    /**
     * Retrieves the questions of a survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return a list of questions in the survey, or an empty list if the survey is not found
     */
    public List<Question> getSurveyQuestions(long surveyId) {
        return surveyRepository.findById(surveyId)
                .map(Survey::getQuestions)
                .orElse(Collections.emptyList());
    }


    public Optional<SurveyCompletion> findSurveyCompletionBySurveyAndEmail(Survey survey, String email) {
        return surveyCompletionRepository.findBySurveyAndEmail(survey, email);
    }
    /**
     * Retrieves all surveys.
     *
     * @return a list of all surveys
     */
    public List<Survey> findAll() {
        return (List<Survey>) surveyRepository.findAll();
    }

    public void saveSurveyResponse(SurveyCompletion surveyCompletion) {
        surveyCompletionRepository.save(surveyCompletion);
    }

    public List<SurveyCompletion> getSurveyResponses(long surveyId) {
return surveyRepository.findById(surveyId)
                .map(Survey::getCompletions)
                .orElse(Collections.emptyList());

    }

    public List<Long> getAllChoiceQuestionIds() {
        return choiceQuestionRepository.findAllQuestionIds();
    }

//    public List<Question> getAllQuestionsWithResponses() {
//        List<Survey> surveys = (List<Survey>) surveyRepository.findAll();
//        List<Question> allQuestions = new ArrayList<>();
//
//        for (Survey survey : surveys) {
//            for (Question question : survey.getQuestions()) {
//                allQuestions.add(question);
//                List<String> responses = question.getResponses();
//                // Process the responses as needed
//            }
//        }
//
//        return allQuestions;
//    }
}
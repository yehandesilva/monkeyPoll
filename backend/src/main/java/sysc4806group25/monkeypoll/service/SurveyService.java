package sysc4806group25.monkeypoll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sysc4806group25.monkeypoll.model.*;
import sysc4806group25.monkeypoll.repo.*;

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

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TextResponseRepository textResponseRepository;

    @Autowired
    private NumberResponseRepository numberResponseRepository;

    @Autowired
    private ChoiceResponseRepository choiceResponseRepository;

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
     * Finds a survey completion by survey and email.
     *
     * @param survey the survey
     * @param email the email of the respondent
     * @return an Optional containing the survey completion if found, or empty if not found
     */
    public Optional<SurveyCompletion> findSurveyCompletionBySurveyAndEmail(Survey survey, String email) {
        return surveyCompletionRepository.findBySurveyAndEmail(survey, email);
    }

    /**
     * Saves a survey response.
     *
     * @param surveyCompletion the survey completion to be saved
     */
    public void saveSurveyResponse(SurveyCompletion surveyCompletion) {
        surveyCompletionRepository.save(surveyCompletion);
    }

    /**
     * Retrieves a question by its ID.
     *
     * @param questionId the ID of the question
     * @return an Optional containing the question if found, or empty if not found
     */
    public Optional<Question> getQuestionById(long questionId) {
        return questionRepository.findById(questionId);
    }

    /**
     * Saves a text response.
     *
     * @param textResponse the text response to be saved
     */
    public void saveTextResponse(TextResponse textResponse) {
        textResponseRepository.save(textResponse);
    }

    /**
     * Saves a number response.
     *
     * @param numberResponse the number response to be saved
     */
    public void saveNumberResponse(NumberResponse numberResponse) {
        numberResponseRepository.save(numberResponse);
    }

    /**
     * Saves a choice response.
     *
     * @param choiceResponse the choice response to be saved
     */
    public void saveChoiceResponse(ChoiceResponse choiceResponse) {
        choiceResponseRepository.save(choiceResponse);
    }
}
package sysc4806group25.monkeypoll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sysc4806group25.monkeypoll.model.Question;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.repo.SurveyRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    /**
     * Creates a new survey with the given description and closed status.
     *
     * @param survey the survey to be created
     * @return the created survey
     */
    public Survey createSurvey(Survey survey) {

        for (Question question : survey.getQuestions()) {
            question.setSurvey(survey);
        }
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

    /**
     * Retrieves all surveys.
     *
     * @return a list of all surveys
     */
    public List<Survey> findAll() {
        return (List<Survey>) surveyRepository.findAll();
    }
}
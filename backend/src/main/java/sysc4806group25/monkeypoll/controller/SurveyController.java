package sysc4806group25.monkeypoll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sysc4806group25.monkeypoll.dto.SurveyResponseDTO;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.ChoiceOption;
import sysc4806group25.monkeypoll.model.ChoiceQuestion;
import sysc4806group25.monkeypoll.model.ChoiceResponse;
import sysc4806group25.monkeypoll.model.NumberQuestion;
import sysc4806group25.monkeypoll.model.NumberResponse;
import sysc4806group25.monkeypoll.model.Question;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.model.SurveyCompletion;
import sysc4806group25.monkeypoll.model.TextQuestion;
import sysc4806group25.monkeypoll.model.TextResponse;
import sysc4806group25.monkeypoll.service.SurveyService;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    /**
     * Retrieves the questions and responses for a specific survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return a ResponseEntity containing a list of questions and their responses, or a 404 status if the survey is not found
     */
    @GetMapping("/user/survey/{surveyId}/responses")
    public ResponseEntity<?> getSurveyQuestionsAndResponses(@PathVariable long surveyId) {
        // Retrieve the survey by its ID
        Optional<Survey> surveyOpt = surveyService.getSurveyById(surveyId);
        if (surveyOpt.isEmpty()) {
            // Return a 404 status if the survey is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Survey not found!\"}");
        }

        Survey survey = surveyOpt.get();
        // Map the questions and their responses to a list of objects
        List<Object> questionResponses = survey.getQuestions().stream().map(question -> {
            if (question instanceof TextQuestion) {
                // Map TextQuestion to a response object
                return Map.of(
                        "questionId", question.getQuestionId(),
                        "question", question.getQuestion(),
                        "type", "TextQuestion",
                        "responses", ((TextQuestion) question).getResponses().stream()
                                .map(TextResponse::getResponse)
                                .collect(Collectors.toList())
                );
            } else if (question instanceof NumberQuestion) {
                // Map NumberQuestion to a response object
                return Map.of(
                        "questionId", question.getQuestionId(),
                        "question", question.getQuestion(),
                        "type", "NumberQuestion",
                        "responses", ((NumberQuestion) question).getResponses().stream()
                                .map(response -> String.valueOf(response.getResponse()))
                                .collect(Collectors.toList())
                );
            } else if (question instanceof ChoiceQuestion) {
                // Map ChoiceQuestion to a response object
                return Map.of(
                        "questionId", question.getQuestionId(),
                        "question", question.getQuestion(),
                        "type", "ChoiceQuestion",
                        "responses", ((ChoiceQuestion) question).getResponses().stream()
                                .map(response -> response.getResponse().getDescription())
                                .collect(Collectors.toList())
                );
            }
            return null;
        }).collect(Collectors.toList());

        // Return the list of questions and their responses
        return ResponseEntity.ok(questionResponses);
    }

    @GetMapping("/user/survey/{surveyId}/results")
    public ResponseEntity<?> getSurveyResults(@PathVariable long surveyId) {
        // Retrieve the survey by its ID
        Optional<Survey> surveyOpt = surveyService.getSurveyById(surveyId);
        if (surveyOpt.isEmpty()) {
            // Return a 404 status if the survey is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Survey not found!\"}");
        }
        Survey survey = surveyOpt.get();

        // Map the questions and their responses to a list of objects
        List<Object> questionResponses = survey.getQuestions().stream().map(question -> {
            if (question instanceof TextQuestion) {
                // Map TextQuestion to a response object
                return Map.of(
                        "question", question.getQuestion(),
                        "questionType", "TextQuestion",
                        "responses", ((TextQuestion) question).getResponses().stream()
                                .map(TextResponse::getResponse)
                                .collect(Collectors.toList())
                );
            } else if (question instanceof NumberQuestion) {
                // For NumberQuestion, structure the value of 'responses' key as the following:
                // [
                //    {"number1" : count},
                //    {"number2" : count},
                // ]

                // Get all NumberResponses for the question
                List<NumberResponse> allNumberResponses = ((NumberQuestion) question).getResponses();

                // Create list of HashMap 'entries' - One HashMap<"number", number> for every unique number response
                ArrayList<HashMap<String, Integer>> analyticResponses = new ArrayList<>();
                for (NumberResponse response : allNumberResponses) {
                    // Check if HashMap 'entry' already exists for this number
                    boolean existingEntry = false;
                    for (HashMap<String, Integer> entry : analyticResponses) {
                        if (entry.containsKey(String.valueOf(response.getResponse()))) {
                            existingEntry = true;
                            break;
                        }
                    }
                    // Create HashMap for this NumberResponse (unique number)
                    if (!existingEntry) {
                        int count = 0;
                        // Compute number of occurrences
                        for (NumberResponse innerResponse : allNumberResponses) {
                            if (response.getResponse() == innerResponse.getResponse()) {
                                // Match found
                                count++;
                            }
                        }
                        // Create the HashMap 'entry' and add to list
                        HashMap<String, Integer> responseMap = new HashMap<>();
                        responseMap.put(String.valueOf(response.getResponse()), count);
                        analyticResponses.add(responseMap);
                    }
                }

                // Map NumberQuestion to a response object
                return Map.of(
                        "question", question.getQuestion(),
                        "questionType", "NumberQuestion",
                        "responses", analyticResponses
                );
            } else if (question instanceof ChoiceQuestion) {
                // For ChoiceQuestion, structure value of 'responses' key as the following:
                // [
                //    {"choice1 description" : choice1Count},
                //    {"choice2 description" : choice2Count},
                // ]

                // Get all NumberResponses for the question
                List<ChoiceResponse> allChoiceResponses = ((ChoiceQuestion) question).getResponses();

                // Create list of HashMap 'entries' - One HashMap<"choiceDescription", count> for every unique choice response
                ArrayList<HashMap<String, Integer>> analyticResponses = new ArrayList<>();
                for (ChoiceResponse response : allChoiceResponses) {
                    // Check if HashMap 'entry' already exists for this description
                    boolean existingEntry = false;
                    for (HashMap<String, Integer> entry : analyticResponses) {
                        if (entry.containsKey(response.getResponse().getDescription())) {
                            existingEntry = true;
                            break;
                        }
                    }
                    // Create HashMap for this ChoiceResponse (unique number)
                    if (!existingEntry) {
                        int count = 0;
                        // Compute number of occurrences
                        for (ChoiceResponse innerResponse : allChoiceResponses) {
                            // Note: Comparing ChoiceOptions by choiceOptionId (ChoiceOption PK)
                            if (response.getResponse().getChoiceOptionId() == innerResponse.getResponse().getChoiceOptionId()) {
                                // Match found
                                count++;
                            }
                        }
                        // Create the HashMap 'entry' and add to list
                        HashMap<String, Integer> responseMap = new HashMap<>();
                        responseMap.put(response.getResponse().getDescription(), count);
                        analyticResponses.add(responseMap);
                    }
                }

                // Map ChoiceQuestion to a response object
                return Map.of(
                        "question", question.getQuestion(),
                        "questionType", "ChoiceQuestion",
                        "responses", analyticResponses
                );
            }
            return null;
        }).collect(Collectors.toList());

        // Return the list of questions and their responses
        return ResponseEntity.ok(questionResponses);
    }

    /**
     * Returns the number of times the specified NumberResponse occurs
     * in the provided list of NumberResponses
     * @param responses - list of NumberResponses
     * @return the total count of the specified NumberResponse
     */
    private int getNumberResponseCount(List<NumberResponse> responses, NumberResponse targetResponse) {
        int responseCount = 0;
        for (NumberResponse response : responses) {
            if (response.getResponse() == targetResponse.getResponse()) {
                responseCount++;
            }
        }
        return responseCount;
    }

    /**
     * Returns the number of times the specified ChoiceResponse occurs
     * in the provided list of ChoiceResponses
     * @param responses - list of ChoiceResponses
     * @return the total count of the specified ChoiceResponse
     *
     * (Note: ChoiceOptions can be compared based on their unique choiceOptionId (instead of their description))
     */
    private int getChoiceOptionCount(List<ChoiceResponse> responses, ChoiceResponse targetResponse) {
        int responseCount = 0;
        for (ChoiceResponse response : responses) {
            if (response.getResponse().getChoiceOptionId() == targetResponse.getResponse().getChoiceOptionId()) {
                responseCount++;
            }
        }
        return responseCount;
    }

    /**
     * Submits a survey response for a specific survey by its ID.
     *
     * @param surveyResponseDTO the survey response data transfer object containing the responses
     * @param surveyId the ID of the survey
     * @return a ResponseEntity containing a success message with a 201 status, or an error message with an appropriate status
     */
    @PostMapping("/survey/{surveyId}")
    public ResponseEntity<String> submitSurveyResponse(@RequestBody SurveyResponseDTO surveyResponseDTO, @PathVariable long surveyId) {
        // Retrieve the survey by its ID
        Optional<Survey> surveyOpt = surveyService.getSurveyById(surveyId);
        if (surveyOpt.isEmpty()) {
            // Return a 404 status if the survey is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Survey not found!\"}");
        }

        Survey survey = surveyOpt.get();

        // Check if the survey is closed
        if (survey.getClosed()) {
            // Return a 403 status if the survey is closed
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Survey is closed!\"}");
        }

        // Check if the email has already submitted the survey
        Optional<SurveyCompletion> existingCompletion = surveyService.findSurveyCompletionBySurveyAndEmail(survey, surveyResponseDTO.getEmail());
        if (existingCompletion.isPresent()) {
            // Return a 409 status if the email has already submitted the survey
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"This email has already submitted the survey!\"}");
        }

        SurveyCompletion surveyCompletion = new SurveyCompletion(surveyResponseDTO.getEmail(), survey);

        // Process each response
        for (SurveyResponseDTO.ResponseDTO responseDTO : surveyResponseDTO.getResponses()) {
            Optional<Question> questionOpt = surveyService.getQuestionById(responseDTO.getQuestionId());
            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();
                switch (question) {
                    case TextQuestion textQuestion when responseDTO instanceof SurveyResponseDTO.TextResponseDTO -> {
                        // Process TextQuestion response
                        TextResponse textResponse = new TextResponse(((SurveyResponseDTO.TextResponseDTO) responseDTO).getResponse(), textQuestion);
                        textQuestion.addResponse(textResponse);
                        surveyService.saveTextResponse(textResponse); // Save the updated question
                    }
                    case NumberQuestion numberQuestion when responseDTO instanceof SurveyResponseDTO.NumberResponseDTO -> {
                        // Process NumberQuestion response
                        NumberResponse numberResponse = new NumberResponse(((SurveyResponseDTO.NumberResponseDTO) responseDTO).getResponse(), numberQuestion);
                        numberQuestion.addResponse(numberResponse);
                        surveyService.saveNumberResponse(numberResponse); // Save the updated question
                    }
                    case ChoiceQuestion choiceQuestion when responseDTO instanceof SurveyResponseDTO.ChoiceResponseDTO -> {
                        // Process ChoiceQuestion response
                        String response = ((SurveyResponseDTO.ChoiceResponseDTO) responseDTO).getResponse();
                        Optional<ChoiceOption> choiceOptionOpt = choiceQuestion.getOptions().stream()
                                .filter(option -> option.getDescription().equals(response))
                                .findFirst();
                        if (choiceOptionOpt.isPresent()) {
                            ChoiceResponse choiceResponse = new ChoiceResponse(choiceOptionOpt.get(), choiceQuestion);
                            choiceQuestion.addResponse(choiceResponse);
                            surveyService.saveChoiceResponse(choiceResponse); // Save the updated question
                        } else {
                            // Return a 400 status if the choice option is invalid
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Invalid choice option!\"}");
                        }
                    }
                    default -> {
                        // Return a 400 status if the question type is invalid
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Invalid question type!\"}");
                    }
                }
            }
        }

        // Save the survey response
        surveyService.saveSurveyResponse(surveyCompletion);

        // Return a 201 status with a success message
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Survey response successfully submitted!\"}");
    }

}

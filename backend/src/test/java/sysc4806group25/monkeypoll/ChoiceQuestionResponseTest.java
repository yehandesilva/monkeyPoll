package sysc4806group25.monkeypoll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sysc4806group25.monkeypoll.model.*;
import sysc4806group25.monkeypoll.repo.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Test driver to test the Choice entities.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChoiceQuestionResponseTest {

    @Autowired
    private ChoiceOptionRepository choiceOptionRepository;
    @Autowired
    private ChoiceResponseRepository choiceResponseRepository;
    @Autowired
    private ChoiceQuestionRepository choiceQuestionRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final Account account = new Account();
    private final Survey survey = new Survey();

    /**
     * Test response addition and deletion of question.
     */
    @Test
    public void testQuestionResponse() {
        ChoiceQuestion question = new ChoiceQuestion();
        assertEquals(0, question.getResponses().size());

        ChoiceResponse response = new ChoiceResponse();
        question.addResponse(response);
        assertEquals(1, question.getResponses().size());

        question.removeResponse(response);
        assertEquals(0, question.getResponses().size());
    }

    /**
     * Test corresponding survey of question.
     */
    @Test
    public void testSurvey() {
        ChoiceQuestion question = new ChoiceQuestion();
        question.setSurvey(survey);
        assertEquals(survey, question.getSurvey());
    }

    /**
     * Test the question statement of the question.
     */
    @Test
    public void testQuestion() {
        String questionStatement = "Is this a test question?";
        ChoiceQuestion question = new ChoiceQuestion();
        question.setQuestion(questionStatement);
        assertEquals(questionStatement, question.getQuestion());
    }

    /**
     * Test the response statement of a response.
     */
    @Test
    public void testResponse() {
        ChoiceResponse response = new ChoiceResponse();
        ChoiceOption newResponse = new ChoiceOption();
        response.setResponse(newResponse);
        assertEquals(newResponse, response.getResponse());
    }

    /**
     * Test the question of a response.
     */
    @Test
    public void testResponseQuestion() {
        ChoiceResponse response = new ChoiceResponse();
        ChoiceQuestion question = new ChoiceQuestion();
        response.setQuestion(question);
        assertEquals(question, response.getQuestion());
    }

    /**
     * Test persistence of choice question and response record in database.
     */
    @Test
    public void testPersistence() {

        // Create account/survey for text question
        account.addSurvey(survey);
        accountRepository.save(account);

        ArrayList<ChoiceQuestion> persistedChoiceQuestions = new ArrayList<>();
        choiceQuestionRepository.findAll().forEach(persistedChoiceQuestions::add);
        ArrayList<ChoiceResponse> persistedChoiceResponses = new ArrayList<>();
        choiceResponseRepository.findAll().forEach(persistedChoiceResponses::add);
        ArrayList<ChoiceOption> persistedChoiceOptions = new ArrayList<>();
        choiceOptionRepository.findAll().forEach(persistedChoiceOptions::add);

        // Save two text question instances
        ChoiceQuestion question1 = new ChoiceQuestion("Question 1", survey);
        ChoiceQuestion question2 = new ChoiceQuestion("Question 2", survey);
        choiceQuestionRepository.save(question1);
        choiceQuestionRepository.save(question2);

        // Add options
        ChoiceOption option1 = new ChoiceOption();
        ChoiceOption option2 = new ChoiceOption();
        question1.addOption(option1);
        question2.addOption(option2);
        choiceOptionRepository.save(option1);
        choiceOptionRepository.save(option2);

        // Add responses
        ChoiceResponse response1 = new ChoiceResponse();
        ChoiceResponse response2 = new ChoiceResponse();
        question1.addResponse(response1);
        question2.addResponse(response2);
        response1.setResponse(option1);
        response2.setResponse(option2);
        choiceResponseRepository.save(response1);
        choiceResponseRepository.save(response2);


        // Verify questions are persisted
        int initialQuestionSize = persistedChoiceQuestions.size();
        persistedChoiceQuestions.clear();
        choiceQuestionRepository.findAll().forEach(persistedChoiceQuestions::add);
        assertEquals(initialQuestionSize + 2, persistedChoiceQuestions.size());

        // Verify options are persisted
        int initialOptionSize = persistedChoiceOptions.size();
        persistedChoiceOptions.clear();
        choiceOptionRepository.findAll().forEach(persistedChoiceOptions::add);
        assertEquals(initialOptionSize + 2, persistedChoiceOptions.size());

        // Verify responses are persisted
        int initialResponseSize = persistedChoiceResponses.size();
        persistedChoiceResponses.clear();
        choiceResponseRepository.findAll().forEach(persistedChoiceResponses::add);
        assertEquals(initialResponseSize + 2, persistedChoiceResponses.size());
    }
}

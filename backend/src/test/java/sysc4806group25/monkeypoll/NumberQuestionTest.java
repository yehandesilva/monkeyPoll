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
 * Test driver to test the TextQuestion entity.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NumberQuestionTest {

    @Autowired
    private NumberResponseRepository numberResponseRepository;
    @Autowired
    private NumberQuestionRepository numberQuestionRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final Account account = new Account();
    private final Survey survey = new Survey();

    /**
     * Test response addition and deletion of question.
     */
    @Test
    public void testQuestionResponse() {
        NumberQuestion question = new NumberQuestion();
        assertEquals(0, question.getResponses().size());

        NumberResponse response = new NumberResponse();
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
        NumberQuestion question = new NumberQuestion();
        question.setSurvey(survey);
        assertEquals(survey, question.getSurvey());
    }

    /**
     * Test the question statement of the question.
     */
    @Test
    public void testQuestion() {
        String questionStatement = "Is this a test question?";
        NumberQuestion question = new NumberQuestion();
        question.setQuestion(questionStatement);
        assertEquals(questionStatement, question.getQuestion());
    }

    /**
     * Test the response statement of a response.
     */
    @Test
    public void testResponse() {
        NumberResponse response = new NumberResponse();
        int newResponse = 1;
        response.setResponse(newResponse);
        assertEquals(newResponse, response.getResponse());
    }

    /**
     * Test the question of a response.
     */
    @Test
    public void testResponseQuestion() {
        NumberResponse response = new NumberResponse();
        NumberQuestion question = new NumberQuestion();
        response.setQuestion(question);
        assertEquals(question, response.getQuestion());
    }

    /**
     * Test persistence of number question and response record in database.
     */
    @Test
    public void testPersistence() {

        // Create account/survey for text question
        account.addSurvey(survey);
        accountRepository.save(account);

        ArrayList<NumberQuestion> persistedNumberQuestions = new ArrayList<>();
        numberQuestionRepository.findAll().forEach(persistedNumberQuestions::add);
        ArrayList<NumberResponse> persistedNumberResponses = new ArrayList<>();
        numberResponseRepository.findAll().forEach(persistedNumberResponses::add);

        // Save two text question instances
        NumberQuestion question1 = new NumberQuestion("Question 1", survey);
        NumberQuestion question2 = new NumberQuestion("Question 2", survey);
        numberQuestionRepository.save(question1);
        numberQuestionRepository.save(question2);

        // Add responses
        NumberResponse response1 = new NumberResponse();
        NumberResponse response2 = new NumberResponse();
        question1.addResponse(response1);
        question2.addResponse(response2);

        // Verify questions are persisted
        int initialQuestionSize = persistedNumberQuestions.size();
        persistedNumberQuestions.clear();
        numberQuestionRepository.findAll().forEach(persistedNumberQuestions::add);
        assertEquals(initialQuestionSize + 2, persistedNumberQuestions.size());

        // Verify responses are persisted
        int initialResponseSize = persistedNumberResponses.size();
        persistedNumberResponses.clear();
        numberResponseRepository.findAll().forEach(persistedNumberResponses::add);
        assertEquals(initialResponseSize + 2, persistedNumberQuestions.size());
        for (NumberResponse response : persistedNumberResponses) {
            assertEquals(question1, response.getQuestion());
        }
    }
}


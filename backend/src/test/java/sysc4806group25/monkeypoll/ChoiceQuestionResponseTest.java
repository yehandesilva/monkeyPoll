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
     * Test persistence of text question and response record in database.
     */
    @Test
    public void testPersistence() {

        // Create account/survey for text question
        account.addSurvey(survey);
        accountRepository.save(account);

        ArrayList<ChoiceQuestion> persistedTextQuestions = new ArrayList<>();
        choiceQuestionRepository.findAll().forEach(persistedTextQuestions::add);
        ArrayList<ChoiceResponse> persistedTextResponses = new ArrayList<>();
        choiceResponseRepository.findAll().forEach(persistedTextResponses::add);

        // Save two text question instances
        ChoiceQuestion question1 = new ChoiceQuestion("Question 1", survey);
        ChoiceQuestion question2 = new ChoiceQuestion("Question 2", survey);
        choiceQuestionRepository.save(question1);
        choiceQuestionRepository.save(question2);

        // Add responses
        ChoiceResponse response1 = new ChoiceResponse();
        ChoiceResponse response2 = new ChoiceResponse();
        question1.addResponse(response1);
        question2.addResponse(response2);

        // Verify questions are persisted
        int initialQuestionSize = persistedTextQuestions.size();
        persistedTextQuestions.clear();
        choiceQuestionRepository.findAll().forEach(persistedTextQuestions::add);
        assertEquals(initialQuestionSize + 2, persistedTextQuestions.size());

        // Verify responses are persisted
        int initialResponseSize = persistedTextResponses.size();
        persistedTextResponses.clear();
        choiceResponseRepository.findAll().forEach(persistedTextResponses::add);
        assertEquals(initialResponseSize + 2, persistedTextQuestions.size());
        for (ChoiceResponse response : persistedTextResponses) {
            assertEquals(question1, response.getQuestion());
        }
    }
}

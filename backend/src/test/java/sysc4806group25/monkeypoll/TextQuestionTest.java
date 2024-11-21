package sysc4806group25.monkeypoll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.model.TextQuestion;
import sysc4806group25.monkeypoll.model.TextResponse;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.repo.SurveyRepository;
import sysc4806group25.monkeypoll.repo.TextQuestionRepository;
import sysc4806group25.monkeypoll.repo.TextResponseRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Test driver to test the TextQuestion entity.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TextQuestionTest {

    @Autowired
    private TextResponseRepository textResponseRepository;
    @Autowired
    private TextQuestionRepository textQuestionRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final Account account = new Account();
    private final Survey survey = new Survey();

    /**
     * Test response addition and deletion of question.
     */
    @Test
    public void testQuestionResponse() {
        TextQuestion question = new TextQuestion();
        assertEquals(0, question.getResponses().size());

        TextResponse response = new TextResponse();
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
        TextQuestion question = new TextQuestion();
        question.setSurvey(survey);
        assertEquals(survey, question.getSurvey());
    }

    /**
     * Test the question statement of the question.
     */
    @Test
    public void testQuestion() {
        String questionStatement = "Is this a test question?";
        TextQuestion question = new TextQuestion();
        question.setQuestion(questionStatement);
        assertEquals(questionStatement, question.getQuestion());
    }

    /**
     * Test the response statement of a response.
     */
    @Test
    public void testResponse() {
        TextResponse response = new TextResponse();
        String newResponse = "New response";
        response.setResponse(newResponse);
        assertEquals(newResponse, response.getResponse());
    }

    /**
     * Test the question of a response.
     */
    @Test
    public void testResponseQuestion() {
        TextResponse response = new TextResponse();
        TextQuestion question = new TextQuestion();
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

        ArrayList<TextQuestion> persistedTextQuestions = new ArrayList<>();
        textQuestionRepository.findAll().forEach(persistedTextQuestions::add);
        ArrayList<TextResponse> persistedTextResponses = new ArrayList<>();
        textResponseRepository.findAll().forEach(persistedTextResponses::add);

        // Save two text question instances
        TextQuestion question1 = new TextQuestion("Question 1", survey);
        TextQuestion question2 = new TextQuestion("Question 2", survey);
        textQuestionRepository.save(question1);
        textQuestionRepository.save(question2);

        // Add responses
        TextResponse response1 = new TextResponse();
        TextResponse response2 = new TextResponse();
        question1.addResponse(response1);
        question2.addResponse(response2);

        // Verify questions are persisted
        int initialQuestionSize = persistedTextQuestions.size();
        persistedTextQuestions.clear();
        textQuestionRepository.findAll().forEach(persistedTextQuestions::add);
        assertEquals(initialQuestionSize + 2, persistedTextQuestions.size());

        // Verify responses are persisted
        int initialResponseSize = persistedTextResponses.size();
        persistedTextResponses.clear();
        textResponseRepository.findAll().forEach(persistedTextResponses::add);
        assertEquals(initialResponseSize + 2, persistedTextQuestions.size());
        for (TextResponse response : persistedTextResponses) {
            assertEquals(question1, response.getQuestion());
        }
    }
}

package sysc4806group25.monkeypoll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.repo.SurveyRepository;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test driver to test the Survey entity.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SurveyTest {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final Account account = new Account();

    /**
     * Test account relationship of survey.
     */
    @Test
    public void testAccount() {
        Survey survey = new Survey("Test survey", false, account);
        assertEquals(account, survey.getAccount());

        Account account2 = new Account();
        survey.setAccount(account2);
        assertEquals(account2, survey.getAccount());
    }

    /**
     * Test description of survey.
     */
    @Test
    public void testDescription() {
        String description1 = "This is a test description";
        Survey survey = new Survey(description1, false, account);
        assertEquals(description1, survey.getDescription());

        String description2 = "This is another test description";
        survey.setDescription(description2);
        assertEquals(description2, survey.getDescription());
    }

    /**
     * Test closed status of survey.
     */
    @Test
    public void testClosed() {
        Survey survey = new Survey("Test survey", false, account);
        assertFalse(survey.getClosed());

        survey.setClosed(true);
        assertTrue(survey.getClosed());
    }

    /**
     * Test persistence of survey record in database.
     */
    @Test
    public void testPersistence() {

        // Create account for survey
        accountRepository.save(account);
        ArrayList<Survey> persistedSurveys = new ArrayList<>();
        surveyRepository.findAll().forEach(persistedSurveys::add);

        // Save two survey instances
        Survey survey1 = new Survey("Test survey 1", false, account);
        Survey survey2 = new Survey("Test survey 2", true, account);
        surveyRepository.save(survey1);
        surveyRepository.save(survey2);

        // Verify surveys are persisted
        int initialSize = persistedSurveys.size();
        persistedSurveys.clear();
        surveyRepository.findAll().forEach(persistedSurveys::add);
        assertEquals(initialSize + 2, persistedSurveys.size());
    }
}

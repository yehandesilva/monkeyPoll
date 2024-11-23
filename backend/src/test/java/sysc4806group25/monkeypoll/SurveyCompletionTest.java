package sysc4806group25.monkeypoll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.model.SurveyCompletion;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.repo.SurveyCompletionRepository;
import sysc4806group25.monkeypoll.repo.SurveyRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SurveyCompletionTest {

    @Autowired
    private SurveyCompletionRepository surveyCompletionRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private AccountRepository accountRepository;
    private final Account account = new Account();
    private final Survey survey = new Survey(account);

    @Test
    public void testEmail() {
        String email = "johndoe@email.com";
        SurveyCompletion surveyCompletion = new SurveyCompletion(email, survey);
        assertEquals(email, surveyCompletion.getEmail());

        String email2 = "johndoe@email2.com";
        surveyCompletion.setEmail(email2);
        assertEquals(email2, surveyCompletion.getEmail());
    }

    @Test
    public void testSurvey() {
        SurveyCompletion surveyCompletion = new SurveyCompletion("johndoe@email.com", survey);
        assertEquals(survey, surveyCompletion.getSurvey());

        Survey survey2 = new Survey();
        surveyCompletion.setSurvey(survey2);
        assertEquals(survey2, surveyCompletion.getSurvey());
    }

    @Test
    public void testSurveyCompletionPersistence() {
        // Create account and survey for survey completion
        accountRepository.save(account);
        surveyRepository.save(survey);
        ArrayList<SurveyCompletion> persistedSurveyCompletions = new ArrayList<>();
        surveyCompletionRepository.findAll().forEach(persistedSurveyCompletions::add);

        // Create two instances of SurveyCompletion
        SurveyCompletion surveyCompletion1 = new SurveyCompletion("first@email.com", survey);
        SurveyCompletion surveyCompletion2 = new SurveyCompletion("second@email.com", survey);

        // Save SurveyCompletion instances
        surveyCompletionRepository.save(surveyCompletion1);
        surveyCompletionRepository.save(surveyCompletion2);

        // Verify surveys completions are persisted
        int initialSize = persistedSurveyCompletions.size();
        persistedSurveyCompletions.clear();
        surveyCompletionRepository.findAll().forEach(persistedSurveyCompletions::add);
        assertEquals(initialSize + 2, persistedSurveyCompletions.size());
    }
}

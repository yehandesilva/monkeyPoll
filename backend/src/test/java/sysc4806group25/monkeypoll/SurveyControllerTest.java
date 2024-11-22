package sysc4806group25.monkeypoll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806group25.monkeypoll.model.*;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.repo.SurveyRepository;
import sysc4806group25.monkeypoll.service.SurveyService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Survey survey;

    @BeforeEach
    public void setUp() {
        // Create and save an account
        Account account = new Account();
        accountRepository.save(account);

        // Create and save a survey associated with the account
        survey = new Survey("Test Survey", false, account);
        account.addSurvey(survey);
        surveyRepository.save(survey);

        // Create and add questions to the survey
        TextQuestion question1 = new TextQuestion("Question 1", survey);
        TextQuestion question2 = new TextQuestion("Question 2", survey);
        survey.addQuestion(question1);
        survey.addQuestion(question2);
        surveyRepository.save(survey);

        // Mock the surveyService to return the created survey when queried by ID
        when(surveyService.getSurveyById(survey.getSurveyId())).thenReturn(Optional.of(survey));
    }

    @Test
    @WithMockUser
    public void testGetSurveyById() throws Exception {
        // Perform a GET request to retrieve the survey by ID and verify the response
        mockMvc.perform(get("/surveys/" + survey.getSurveyId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"surveyId\":1,\"description\":\"Test Survey\",\"closed\":false,\"completions\":[],\"questions\":[{\"questionId\":0,\"question\":\"Question 1\"},{\"questionId\":0,\"question\":\"Question 2\"}]}"));
    }

}
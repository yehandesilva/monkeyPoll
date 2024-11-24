package sysc4806group25.monkeypoll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.service.AccountUserDetailsService;
import sysc4806group25.monkeypoll.service.SurveyService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountUserDetailsService userDetailsService;

    @MockBean
    private SurveyService surveyService;

    /**
     * Test to create a survey without authorization.
     * Expects a 401 Unauthorized status.
     */
    @Test
    public void testCreateSurveyNotAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/survey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Survey\",\"closed\": false,\"questions\": [" +
                                "{\"question\": \"Describe your work experience.\",\"type\": \"TextQuestion\"}," +
                                "{\"question\": \"What's your favorite video game?\",\"type\": \"ChoiceQuestion\",\"options\": [" +
                                "{\"description\": \"Fortnite\"}," +
                                "{\"description\": \"Forza\"}," +
                                "{\"description\": \"Mario-Kart\"}]}," +
                                "{\"question\": \"What's your age?\",\"type\": \"NumberQuestion\",\"minValue\": 0,\"maxValue\": 100}," +
                                "{\"question\": \"Describe your work experience.\",\"type\": \"TextQuestion\"}]}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    /**
     * Test to create a survey with authorization.
     * Expects a 201 Created status.
     */
    @Test
    public void testCreateSurveyAuthorized() throws Exception {
        Account account = new Account();
        account.setEmail("johndoe@email.com");
        account.setPassword("password123");

        Mockito.when(userDetailsService.loadUserByUsername("johndoe@email.com"))
                .thenReturn(account);

        Survey mockSurvey = new Survey();
        mockSurvey.setDescription("Survey");
        mockSurvey.setClosed(false);

        when(surveyService.createSurvey(Mockito.any(Survey.class))).thenReturn(mockSurvey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/survey")
                        .with(SecurityMockMvcRequestPostProcessors.user(account))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Survey\",\"closed\": false,\"questions\": [" +
                                "{\"question\": \"Describe your work experience.\",\"type\": \"TextQuestion\"}," +
                                "{\"question\": \"What's your favorite video game?\",\"type\": \"ChoiceQuestion\",\"options\": [" +
                                "{\"description\": \"Fortnite\"}," +
                                "{\"description\": \"Forza\"}," +
                                "{\"description\": \"Mario-Kart\"}]}," +
                                "{\"question\": \"What's your age?\",\"type\": \"NumberQuestion\",\"minValue\": 0,\"maxValue\": 100}," +
                                "{\"question\": \"Describe your work experience.\",\"type\": \"TextQuestion\"}]}"))
                .andExpect(status().isCreated());
    }

    /**
     * Test to create a survey and then retrieve it by ID.
     * Expects a 201 Created status for creation and 200 OK status for retrieval.
     */
    @Test
    public void testCreateSurveyAndGetSurveyById() throws Exception {
        Account account = new Account();
        account.setEmail("johndoe@email.com");
        account.setPassword("password123");

        Mockito.when(userDetailsService.loadUserByUsername("johndoe@email.com"))
                .thenReturn(account);

        Survey mockSurvey = new Survey();
        mockSurvey.setDescription("Survey");
        mockSurvey.setClosed(false);

        when(surveyService.createSurvey(Mockito.any(Survey.class))).thenReturn(mockSurvey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/survey")
                        .with(SecurityMockMvcRequestPostProcessors.user(account))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Survey\",\"closed\": false,\"questions\": [" +
                                "{\"question\": \"Describe your work experience.\",\"type\": \"TextQuestion\"}," +
                                "{\"question\": \"What's your favorite video game?\",\"type\": \"ChoiceQuestion\",\"options\": [" +
                                "{\"description\": \"Fortnite\"}," +
                                "{\"description\": \"Forza\"}," +
                                "{\"description\": \"Mario-Kart\"}]}," +
                                "{\"question\": \"What's your age?\",\"type\": \"NumberQuestion\",\"minValue\": 0,\"maxValue\": 100}," +
                                "{\"question\": \"Describe your work experience.\",\"type\": \"TextQuestion\"}]}"))
                .andExpect(status().isCreated());

        when(surveyService.getSurveyById(Mockito.anyLong())).thenReturn(Optional.of(mockSurvey));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/survey/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"description\": \"Survey\",\"closed\": false}"));
    }

    /**
     * Test to get a survey that doesn't exist.
     * Expects a 404 Not Found status.
     */
    @Test
    public void testGetNonExistentSurvey() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/survey/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
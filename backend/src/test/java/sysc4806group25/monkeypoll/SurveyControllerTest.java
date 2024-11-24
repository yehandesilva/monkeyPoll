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
import sysc4806group25.monkeypoll.model.*;
import sysc4806group25.monkeypoll.service.AccountUserDetailsService;
import sysc4806group25.monkeypoll.service.SurveyService;

import java.util.List;
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

    // No Setup() method needed as we are using @MockBean, and it also causes issues with post and get requests

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

        Mockito.when(userDetailsService.loadUserByUsername(account.getEmail()))
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

        Mockito.when(userDetailsService.loadUserByUsername(account.getEmail()))
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

    /**
     * Test to submit a successful survey response.
     * Expects a 201 Created status.
     */
    @Test
    public void testSubmitSuccessfulSurveyResponse() throws Exception {
        Account account = new Account();
        account.setEmail("johndoe@email.com");
        account.setPassword("password123");

        Mockito.when(userDetailsService.loadUserByUsername(account.getEmail()))
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

        this.mockMvc.perform(MockMvcRequestBuilders.post("/survey/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(account))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"potato@hotmail.com\",\"responses\":[" +
                                "{\"questionId\":1,\"response\":\"i am testing\",\"type\":\"text\"}," +
                                "{\"questionId\":2,\"response\":\"Fortnite\",\"type\":\"choice\"}," +
                                "{\"questionId\":3,\"response\":26,\"type\":\"number\"}," +
                                "{\"questionId\":4,\"response\":\"this is a test\",\"type\":\"text\"}]}"))
                .andExpect(status().isCreated());
    }

    /**
     * Test to submit a bad request survey response.
     * Expects a 400 Bad Request status.
     */
    @Test
    public void testSubmitBadRequestSurveyResponse() throws Exception {
        Account account = new Account();
        account.setEmail("johndoe@email.com");
        account.setPassword("password123");

        Mockito.when(userDetailsService.loadUserByUsername(account.getEmail()))
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

        this.mockMvc.perform(MockMvcRequestBuilders.post("/survey/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(account))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"potato@hotmail.com\",\"responses\":[" +
                                "{\"questionId\":1,\"response\":\"i am testing\"}," +
                                "{\"questionId\":2,\"response\":\"Fortnite\"}," +
                                "{\"questionId\":3,\"response\":26}," +
                                "{\"questionId\":4,\"response\":\"this is a test\"}]}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test to submit a survey response and then retrieve the responses.
     * Expects a 201 Created status for submission and 200 OK status for retrieval.
     */
    @Test
    public void testSubmitSurveyResponseAndGetResponses() throws Exception {
        Account account = new Account();
        account.setEmail("johndoe@email.com");
        account.setPassword("password123");

        Mockito.when(userDetailsService.loadUserByUsername(account.getEmail()))
                .thenReturn(account);

        Survey mockSurvey = new Survey();
        mockSurvey.setDescription("Survey");
        mockSurvey.setClosed(false);

        ChoiceQuestion choiceQuestion = new ChoiceQuestion("What's your favorite video game?", mockSurvey);
        TextQuestion textQuestion = new TextQuestion("Describe your work experience.", mockSurvey);

        List<ChoiceOption> options = List.of(
                new ChoiceOption("Fortnite", choiceQuestion),
                new ChoiceOption("Forza", choiceQuestion),
                new ChoiceOption("Mario-Kart", choiceQuestion)
        );
        choiceQuestion.setOptions(options);

        List<Question> questions = List.of(choiceQuestion, textQuestion);
        mockSurvey.setQuestions(questions);

        ChoiceResponse choiceResponse = new ChoiceResponse(options.get(0), choiceQuestion);
        choiceQuestion.setResponses(List.of(choiceResponse));

        when(surveyService.createSurvey(Mockito.any(Survey.class))).thenReturn(mockSurvey);
        when(surveyService.getSurveyById(Mockito.anyLong())).thenReturn(Optional.of(mockSurvey));

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

        this.mockMvc.perform(MockMvcRequestBuilders.post("/survey/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(account))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"potato@hotmail.com\",\"responses\":[" +
                                "{\"questionId\":1,\"response\":\"i am testing\",\"type\":\"text\"}," +
                                "{\"questionId\":2,\"response\":\"Fortnite\",\"type\":\"choice\"}," +
                                "{\"questionId\":3,\"response\":26,\"type\":\"number\"}," +
                                "{\"questionId\":4,\"response\":\"this is a test\",\"type\":\"text\"}]}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/survey/1/responses")
                        .with(SecurityMockMvcRequestPostProcessors.user(account))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"question\":\"What's your favorite video game?\"," +
                        "\"responses\":[\"Fortnite\"],\"type\":\"ChoiceQuestion\",\"questionId\":0}," +
                        "{\"question\":\"Describe your work experience.\",\"responses\":[],\"type\":\"TextQuestion\"," +
                        "\"questionId\":0}]"));
    }
}
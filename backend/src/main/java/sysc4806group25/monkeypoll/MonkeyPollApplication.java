package sysc4806group25.monkeypoll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import sysc4806group25.monkeypoll.model.Account;
import sysc4806group25.monkeypoll.model.Survey;
import sysc4806group25.monkeypoll.model.TextQuestion;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.repo.SurveyRepository;

@SpringBootApplication
@EntityScan(basePackages = "sysc4806group25.monkeypoll.model")
public class MonkeyPollApplication {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    SurveyRepository surveyRepository;

    public static void main(String[] args) {
        SpringApplication.run(MonkeyPollApplication.class, args);
    }

    @Bean
    public CommandLineRunner running() {
        return args -> {
            Account account = new Account("janedoe@email.com", "password456", "Jane", "Doe");
            accountRepository.save(account);

            // Create and save a new survey
            Survey survey = new Survey("Test Survey", false, account);
            TextQuestion question1 = new TextQuestion("Question 1", survey);
            TextQuestion question2 = new TextQuestion("Question 2", survey);
            survey.addQuestion(question1);
            survey.addQuestion(question2);
            surveyRepository.save(survey);


        };
    }


}

package sysc4806group25.monkeypoll;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import sysc4806group25.monkeypoll.model.*;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.repo.SurveyRepository;

@SpringBootApplication
@EntityScan(basePackages = "sysc4806group25.monkeypoll.model")
public class MonkeyPollApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyPollApplication.class, args);
    }

    @Bean
    public CommandLineRunner createSurvey(SurveyRepository surveyRepository, AccountRepository accountRepository) {
        return (args) -> {
            // Create and save an account
            Account account = new Account();
            accountRepository.save(account);

            // Create and save a survey associated with the account
            Survey survey = new Survey("Test Survey", false, account);
            account.addSurvey(survey);
            surveyRepository.save(survey);

            // Create and add questions to the survey
            TextQuestion question1 = new TextQuestion("Describe your work experience.", survey);

            ChoiceQuestion question2 = new ChoiceQuestion("What's your favorite video game?", survey);
            question2.addOption(new ChoiceOption("Fortnite", question2));
            question2.addOption(new ChoiceOption("Valorant", question2));
            question2.addOption(new ChoiceOption("Mario-Kart", question2));

            NumberQuestion question3 = new NumberQuestion("What's your age?", survey);
            question3.setMinValue(0);
            question3.setMaxValue(100);

            survey.addQuestion(question1);
            survey.addQuestion(question2);
            survey.addQuestion(question3);
            surveyRepository.save(survey);
        };
    }

}

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




}

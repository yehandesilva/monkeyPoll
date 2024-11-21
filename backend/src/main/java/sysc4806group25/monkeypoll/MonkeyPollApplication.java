package sysc4806group25.monkeypoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "sysc4806group25.monkeypoll.model")
public class MonkeyPollApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyPollApplication.class, args);
    }

}

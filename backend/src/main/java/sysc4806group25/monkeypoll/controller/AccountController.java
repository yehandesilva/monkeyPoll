package sysc4806group25.monkeypoll.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @PostMapping("/signIn")
    public void signIntoAccount(@RequestParam String email, @RequestParam String password) {
        //TODO: authenticate the user
    }

    @PostMapping("/user")
    public void signIntoAccount(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                @RequestParam String password) {
        //TODO: create the user
    }
}

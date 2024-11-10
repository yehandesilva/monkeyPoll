package sysc4806group25.monkeypoll;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests the AccountController and its endpoints. This includes testing account authentication,
 * as authentication is handled within the AccountController
 */

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registerAccount() throws Exception {
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"janedoe@email.com\", \"password\":\"password456\",\"firstName\":\"Jane\",\"lastName\":\"Doe\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();

        // if we try to register again, we should expect a 409 conflict
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"janedoe@email.com\", \"password\":\"password456\",\"firstName\":\"Jane\",\"lastName\":\"Doe\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().is(409))
                .andReturn();
    }

    @Test
    public void loginAccount() throws Exception {

        // We expect to get a 401 unauthorized since no accounts have been created
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"johndoe@email.com\", \"password\":\"password123\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        // now we can register and login again
        this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"johndoe@email.com\", \"password\":\"password123\",\"firstName\":\"John\",\"lastName\":\"Doe\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();

        // Try to use the wrong password, we expect a 401
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"johndoe@email.com\", \"password\":\"WRONG\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        // Now log in with the right credentials, we expect success
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"johndoe@email.com\", \"password\":\"password123\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();
    }

}

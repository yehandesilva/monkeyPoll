package sysc4806group25.monkeypoll;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringRunner;
import sysc4806group25.monkeypoll.repo.AccountRepository;
import sysc4806group25.monkeypoll.model.Account;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * The AccountTest class is responsible for testing whether the
 * Account class is correctly implemented, and whether an Account
 * class can be persisted properly.
 * (Note: @RunWith(SpringRunner.class) is needed if JUnit4 is being used)
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountTest {

    // Have an instance of the AccountRepository interface injected
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Tests whether the Account's first name can be retrieved
     */
    @Test
    public void getFirstName() {
        // Create new instance of Account
        String firstName = "John";
        Account account = new Account(firstName, "Doe", "johndoe@email.com", "password123");
        assertEquals(firstName, account.getFirstName());
    }

    /**
     * Tests whether the Account's last name can be retrieved
     */
    @Test
    public void getLastName() {
        // Create new instance of Account
        String lastName = "Doe";
        Account account = new Account("John", lastName, "johndoe@email.com", "password123");
        assertEquals(lastName, account.getLastName());
    }

    /**
     * Tests whether the Account's email address can be retrieved
     */
    @Test
    public void getEmailAddress() {
        // Create new instance of Account
        String emailAddress = "johndoe@email.com";
        Account account = new Account("John", "Doe", emailAddress, "password123");
        assertEquals(emailAddress, account.getEmail());
    }

    /**
     * Tests whether the Account's password can be retrieved
     */
    @Test
    public void getPassword() {
        // Create new instance of Account
        String password = "password123";
        Account account = new Account("John", "Doe", password, "password123");
        assertEquals(password, account.getPassword());
    }

    /**
     * Tests whether the Account's string representation is correct
     */
    @Test
    public void getToString() {
        // Create new instance of Account
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@email.com";
        String password = "password123";

        String expectedAccountStr = String.format("First name: %s\nLast name: %s\nEmail: %s\nPassword: %s", firstName, lastName, email, password);
        Account account = new Account(firstName, lastName, email, password);
        assertEquals(expectedAccountStr, account.toString());
    }

    /**
     * Tests whether Account entities can be properly persisted (to H2 database)
     */
    @Test
    public void verifyAccountPersistence() {
        // Create two instances of Account
        Account account1 = new Account("John", "Doe", "johndoe@email.com", "password123");
        Account account2 = new Account("Mark", "David", "markdavid@gmail.com", "somepassword?");
        accountRepository.deleteAll();

        // Use repository to persist both instances
        System.out.println("Persisting the following Account: \n" + account1);
        accountRepository.save(account1);
        System.out.println("\nPersisting the following Account: \n" + account2);
        accountRepository.save(account2);

        // Retrieve all persisted Accounts
        ArrayList<Account> persistedAccounts = new ArrayList<>();
        accountRepository.findAll().forEach(persistedAccounts::add);
        System.out.println(persistedAccounts.size());

        // Assert each persisted Account
        assertEquals(account1.toString(), persistedAccounts.getFirst().toString());
        assertEquals(account2.toString(), persistedAccounts.getLast().toString());
        accountRepository.deleteAll();
    }
}

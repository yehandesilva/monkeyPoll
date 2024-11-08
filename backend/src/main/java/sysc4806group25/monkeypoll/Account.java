package sysc4806group25.monkeypoll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

/**
 * The Account class models the Account entity for the MonkeyPoll application.
 * An account is created by users who want to create and manage polls.
 * An account must have a first and last name, email (a.k.a. username), and password.
 *
 * @author Pathum Danthanarayana, 101181411
 * @date November 8th, 2024
 */
@Entity
@SequenceGenerator(name="accountSeq")
public class Account {

    // Fields
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    //TODO: Once Survey entity is implemented, Account entity should have a list of Survey objects
    // (i.e. List<Survey> surveys)

    /**
     * Empty constructor with no args for JPA
     */
    protected Account() {

    }

    /**
     * Regular constructor for class testing
     */
    public Account(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /** GETTER methods **/

    /**
     * @return the unique identifier of the Account entity.
     * (The 'id' attribute will be the single unique identifier for this entity,
     * and will be generated using the 'accountSeq' generator)
     */
    @Id
    @GeneratedValue(generator="accountSeq")
    public long getId() {
        return id;
    }

    /**
     * @return the first name of the Account holder
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the last name of the Account holder
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the email of the Account holder
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the password of the Account holder
     */
    public String getPassword() {
        return password;
    }


    /** SETTER methods **/

    /**
     * Sets the id of the Account
     * @param id - the id to set to
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets the first name of the Account
     * @param  firstName - the first name to set to
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the Account
     * @param lastName - the last name to set to
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the email of the Account
     * @param email - the email to set to
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param password - the password to set to
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

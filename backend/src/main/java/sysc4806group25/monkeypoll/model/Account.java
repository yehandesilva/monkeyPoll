package sysc4806group25.monkeypoll.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class Account implements UserDetails {

    // Fields
    @Id
    @GeneratedValue(generator="accountSeq")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Survey> surveys = new ArrayList<>();

    /**
     * Empty constructor with no args for JPA
     */
    public Account() {

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

    /**
     * @return surveys created by account.
     */
    public List<Survey> getSurveys() {
        return this.surveys;
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

    /**
     * @param surveys - surveys belonging to the user.
     */
    public void setSurveys(List<Survey> surveys) {
        this.surveys = surveys;
    }

    /**
     * @param survey - survey to add to account
     */
    public void addSurvey(Survey survey) {
        this.surveys.add(survey);
        survey.setAccount(this);
    }

    /**
     * @param survey - survey to remove from account
     */
    public void removeSurvey(Survey survey) {
        this.surveys.remove(survey);
        survey.setAccount(null);
    }

    /** Methods required by UserDetails interface, used for authentication **/

    /**
     * Accounts are assigned the role "ROLE_SURVEYOR" as account holders can make and manage surveys
     * @return the list of Authorities for this account
     */
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of(new SimpleGrantedAuthority("ROLE_SURVEYOR"));
    }



    @Override
    @Transient
    public String getUsername() {
        return getEmail();
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /**
     * @return a String representation of the Account entity
     */
    @Override
    public String toString() {
        return String.format("First name: %s\nLast name: %s\nEmail: %s\nPassword: %s", firstName, lastName, email, password);
    }
}

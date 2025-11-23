package use_case.login;

import entity.User;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if a user with the given email exists.
     * 
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Verifies if the given password matches the password of a user with the given email.
     *
     * @param email the email to look for
     * @return true if given password matches the password of a user with the given email; false otherwise
     */
    boolean verifyPassword(String email, String password);

    /**
     * Returns the user with the given email.
     * 
     * @param email the email to look up
     * @return the user with the given email
     */
    User getUser(String email);
}

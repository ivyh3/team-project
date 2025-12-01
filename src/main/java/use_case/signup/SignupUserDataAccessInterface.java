package use_case.signup;

import entity.User;

/**
 * DAO interface for the Signup Use Case.
 */
public interface SignupUserDataAccessInterface {

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Returns the user with the given email.
     *
     * @param email the email to look up
     * @return the User object with the given email
     */
    User getUserByEmail(String email);

    /**
     * Creates a new user with the given email and password.
     *
     * @param email    the email of the new user
     * @param password the password of the new user
     */
    void createUser(String email, String password);
}

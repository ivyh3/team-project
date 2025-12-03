package frameworks_drivers.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * In-memory Data Access Object for User entities.
 * Provides the same functionality as FirebaseUserDataAccessObject but stores data in memory.
 * Useful for testing and development without requiring Firebase connection.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final Map<String, User> usersByEmail = new HashMap<>();
    private final Map<String, User> usersByUserId = new HashMap<>();
    private final Map<String, String> passwordsByEmail = new HashMap<>();
    private final UserFactory userFactory;
    private int userIdCounter;

    /**
     * Constructs a new InMemoryUserDataAccessObject with the specified UserFactory.
     *
     * @param userFactory the factory used to create User entities
     */
    public InMemoryUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
        this.userIdCounter = 0;
    }

    /**
     * Returns the user with the given email.
     *
     * @param email the email to look up
     * @return the user with the given email, or null if not found
     */
    @Override
    public User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    /**
     * Returns the user with the given user ID.
     *
     * @param userId the user ID to look up
     * @return the user with the given user ID, or null if not found
     */
    @Override
    public User getUserByUserId(String userId) {
        return usersByUserId.get(userId);
    }

    /**
     * Creates a new user with the given email and password.
     *
     * @param email    the email of the new user
     * @param password the password of the new user
     */
    @Override
    public void createUser(String email, String password) {
        final String userId = "user" + userIdCounter++;
        final User user = userFactory.create(userId, email, LocalDateTime.now());

        usersByEmail.put(email, user);
        usersByUserId.put(userId, user);
        passwordsByEmail.put(email, password);
    }

    /**
     * Verifies if the given password matches the password of a user with the given
     * email.
     *
     * @param email    the email to look for
     * @param password the given password to verify
     * @return true if given password matches the password of a user with the given
     *         email; false otherwise
     */
    @Override
    public boolean verifyPassword(String email, String password) {
        final String storedPassword = passwordsByEmail.get(email);
        return storedPassword != null && storedPassword.equals(password);
    }

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    @Override
    public boolean existsByEmail(String email) {
        return usersByEmail.containsKey(email);
    }

    /**
     * Updates the user's password.
     *
     * @param userId      the user ID whose password is to be updated
     * @param newPassword the new password
     */
    @Override
    public void changePassword(String userId, String newPassword) {
        final User user = usersByUserId.get(userId);
        if (user != null) {
            passwordsByEmail.put(user.getEmail(), newPassword);
        }
    }
}

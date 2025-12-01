package entity;

import java.time.LocalDateTime;

/**
 * Factory for creating User objects.
 * Provides convenient factory methods for constructing User instances
 * with different parameter combinations.
 */
public class UserFactory {

    /**
     * Creates a new User with the specified userId and email.
     * The creation timestamp will be set to null.
     *
     * @param userId the unique identifier for the user
     * @param email the user's email address
     * @return a new User instance with null createdAt timestamp
     */
    public User create(String userId, String email) {
        return new User(userId, email, null);
    }

    /**
     * Creates a new User with the specified userId, email, and creation timestamp.
     *
     * @param userId the unique identifier for the user
     * @param email the user's email address
     * @param createdAt the timestamp when the user account was created
     * @return a new User instance with all fields populated
     */
    public User create(String userId, String email, LocalDateTime createdAt) {
        return new User(userId, email, createdAt);
    }
}

package entity;

import java.time.LocalDateTime;

/**
 * Represents a user in the AI Study Companion application.
 * This is an immutable entity that contains core user information.
 */
public class User {
    private final String userId;
    private final String email;
    private final LocalDateTime createdAt;

    // TODO: Most likely will store more data after implementation.

    /**
     * Constructs a new User with the specified userId, email, and creation timestamp.
     *
     * @param userId the unique identifier for the user (cannot be empty)
     * @param email the user's email address (cannot be empty)
     * @param createdAt the timestamp when the user account was created (cannot be null)
     * @throws IllegalArgumentException if userId is empty
     * @throws IllegalArgumentException if email is empty
     * @throws IllegalArgumentException if createdAt is null
     */
    public User(String userId, String email, LocalDateTime createdAt) {
        if ("".equals(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        if ("".equals(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt cannot be empty");
        }
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
    }

    /**
     * Returns the unique identifier for this user.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the email address of this user.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the timestamp when this user account was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
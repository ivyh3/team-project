package entity;

import java.time.LocalDateTime;

/**
 * Represents a user in the AI Study Companion application.
 */
public class User {
    private final String userId;
    private final String email;
    private final LocalDateTime createdAt;

    // TODO: Most likely will store more data after implementation.

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

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
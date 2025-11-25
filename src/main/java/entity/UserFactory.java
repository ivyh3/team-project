package entity;

import java.time.LocalDateTime;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String userId, String email) {
        return new User(userId, email, null);
    }

    public User create(String userId, String email, LocalDateTime createdAt) {
        return new User(userId, email, createdAt);
    }
}
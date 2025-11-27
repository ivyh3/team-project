package entity;

import java.util.Objects;

/**
 * Domain entity representing a course in the AI Study Companion application.
 * Immutable after creation to maintain domain integrity.
 */
public class Course {

    private final String id;
    private final String userId;
    private final String code;
    private final String title;

    public Course(String id, String userId, String code, String title) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.code = Objects.requireNonNull(code, "code cannot be null");
        this.title = Objects.requireNonNull(title, "title cannot be null");
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    // Optional domain behavior
    public boolean belongsToUser(String userId) {
        return this.userId.equals(userId);
    }
}

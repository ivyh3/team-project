package entity;

import java.time.LocalDateTime;

public class ReferenceMaterial {
    private final String id;
    private final String userId; // associate material with a user
    private final String title;
    private final String filePath;
    private final LocalDateTime uploadedAt;

    public ReferenceMaterial(String id, String userId, String title, String filePath, LocalDateTime uploadedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getFilePath() { return filePath; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}
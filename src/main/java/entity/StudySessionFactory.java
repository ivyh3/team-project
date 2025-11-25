package entity;

import java.time.LocalDateTime;

public class StudySessionFactory {
    public StudySession create(String id, LocalDateTime startTime, LocalDateTime endTime) {
        return new StudySession(id, startTime, endTime);
    }

    public StudySession create(LocalDateTime startTime, LocalDateTime endTime) {
        return new StudySession(null, startTime, endTime);
    }
}

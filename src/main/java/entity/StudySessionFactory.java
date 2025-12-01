package entity;

import java.time.LocalDateTime;

/**
 * Factory for Study Session Entities.
 */
public class StudySessionFactory {
    /**
     * Creates a study session entity with a database ID.
     *
     * @param id        The ID for this study session
     * @param startTime The start time for this studysession
     * @param endTime   The end time for this study session
     * @return The StudySession entity
     */
    public StudySession create(String id, LocalDateTime startTime, LocalDateTime endTime) {
        return new StudySession(id, startTime, endTime);
    }

    /**
     * Creates an ID-less study session.
     *
     * @param startTime The start time for this session
     * @param endTime   The end time for this session
     * @return The StudySession entity
     */
    public StudySession create(LocalDateTime startTime, LocalDateTime endTime) {
        return new StudySession(null, startTime, endTime);
    }
}

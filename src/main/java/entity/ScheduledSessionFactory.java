package entity;

import java.time.LocalDateTime;

/**
 * Factory for scheduledsession entities.
 */
public class ScheduledSessionFactory {
    /**
     * Creates a scheduled study session object, with no ID.
     *
     * @param startTime The start time of the scheduled session
     * @param endTime   The end time of the scheduled session
     * @param title   The title of the scheduled session
     * @return The ScheduledSession entity
     */
    public ScheduledSession create(LocalDateTime startTime, LocalDateTime endTime, String title) {
        return new ScheduledSession(null, startTime, endTime, title);
    }

    /**
     * Creates a scheduled study session object with, with an ID.
     *
     * @param id        The ID of this scheduled session in the firebase
     * @param startTime The start time of the scheduled session
     * @param endTime   The end time of the scheduled session
     * @param title   The title of the scheduled session
     * @return The ScheduledSession entitty
     */
    public ScheduledSession create(String id, LocalDateTime startTime, LocalDateTime endTime, String title) {
        return new ScheduledSession(id, startTime, endTime, title);
    }
}

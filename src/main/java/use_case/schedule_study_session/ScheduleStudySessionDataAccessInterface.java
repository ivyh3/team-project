package use_case.schedule_study_session;

import java.util.List;

import entity.ScheduledSession;

public interface ScheduleStudySessionDataAccessInterface {
    /**
     * Saves a scheduled session for the specified user.
     *
     * @param userId the ID of the user
     * @param session the scheduled session to save
     * @return the saved ScheduledSession object, potentially with updated fields (e.g., ID)
     */
    ScheduledSession saveSession(String userId, ScheduledSession session);

    /**
     * Retrieves all scheduled sessions for the specified user.
     *
     * @param userId the ID of the user
     * @return a list of all ScheduledSession objects for the user
     */
    List<ScheduledSession> getAllSessions(String userId);

    /**
     * Retrieves a specific scheduled session by its ID for the specified user.
     *
     * @param userId the ID of the user
     * @param sessionId the ID of the scheduled session to retrieve
     * @return the ScheduledSession with the given ID, or null if not found
     */
    ScheduledSession getScheduledSessionById(String userId, String sessionId);

    /**
     * Deletes a scheduled session for the specified user.
     *
     * @param userId the ID of the user
     * @param session the scheduled session to delete
     */
    void deleteSession(String userId, ScheduledSession session);
}

package use_case.end_study_session;

import entity.StudySession;

/**
 * Data Access Interface for Ending Study Sessions.
 */
public interface EndStudySessionDataAccessInterface {
    /**
     * Saves the study session in to persistent storage.
     *
     * @param userId  The user id associated with the study session
     * @param session The study session to save.
     * @return The saved study session object.
     */
    StudySession addStudySession(String userId, StudySession session);

    /**
     * Gets a study session with the provided session id. Return null if none found.
     *
     * @param userId    The user id to look for
     * @param sessionId The session id to look for
     * @return The session associated with the id, null if not found.
     */
    StudySession getStudySessionById(String userId, String sessionId);
}

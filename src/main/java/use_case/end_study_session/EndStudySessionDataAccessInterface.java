package use_case.end_study_session;

import entity.StudySession;

public interface EndStudySessionDataAccessInterface {
    /**
     * Saves the study session in to persistent storage.
     *
     * @param session The study session to save.
     * @return The saved study session object.
     */
    public StudySession addStudySession(StudySession session);

    /**
     * Gets a study session with the provided session id. Return null if none found.
     * @param sessionId The session id to look for
     * @return The session associated with the id, null if not found.
     */
    public StudySession getStudySession(int sessionId);
}

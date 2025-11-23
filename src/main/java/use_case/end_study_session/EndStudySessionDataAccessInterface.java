package use_case.end_study_session;

import entity.StudySession;

public interface EndStudySessionDataAccessInterface {
    /**
     * Saves the study session in to persistent storage.
     *
     * @param session The study session to save.
     * @return The saved study session object.
     */
    public StudySession saveStudySession(StudySession session);
}

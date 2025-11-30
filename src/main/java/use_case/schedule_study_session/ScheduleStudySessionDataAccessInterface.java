package use_case.schedule_study_session;

import java.util.List;
import entity.ScheduledSession;

public interface ScheduleStudySessionDataAccessInterface {
    void saveSession(String userId, ScheduledSession session);
    List<ScheduledSession> getAllSessions(String userId);
}
package use_case.schedule_study_session;

import entity.ScheduledSession;

public class ScheduleStudySessionOutputData {
    private final ScheduledSession session;

    public ScheduleStudySessionOutputData(ScheduledSession session) {
        this.session = session;
    }

    public ScheduledSession getSession() {
        return session;
    }
}

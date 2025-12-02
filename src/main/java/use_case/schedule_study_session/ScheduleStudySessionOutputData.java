package use_case.schedule_study_session;

public class ScheduleStudySessionOutputData {
    private final String id;
    private final String title;
    private final String startTime;
    private final String endTime;
    private final boolean isSuccess;

    public ScheduleStudySessionOutputData(String id, String title, String startTime,
                                          String endTime, boolean isSuccess) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSuccess = isSuccess;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }
}

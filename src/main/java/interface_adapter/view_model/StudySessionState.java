package interface_adapter.view_model;


import java.time.Duration;
import java.time.LocalDateTime;

public class StudySessionState {

    private StudySessionConfigState.SessionType sessionType;
    private LocalDateTime startTime;
    private Integer targetDurationMinutes;
    private boolean isActive;
    private String prompt;
    private String referenceFile;
    public StudySessionState(StudySessionConfigState config, LocalDateTime startTime) {
        this.sessionType = config.getSessionType();
        this.startTime = startTime;
        this.prompt = config.getPrompt();
        this.referenceFile = config.getReferenceFile();
        targetDurationMinutes = config.getTargetDuration();

        isActive = true;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDurationElapsed() {
        return Duration.between(startTime, LocalDateTime.now());
    }

    public StudySessionConfigState.SessionType getSessionType() {
        return sessionType;
    }

    public Duration getRemainingDuration() {
        LocalDateTime targetEndTime = startTime.plusMinutes(targetDurationMinutes);

        Duration remaining = Duration.between(LocalDateTime.now(), targetEndTime);

        if (remaining.isNegative()) {
            return Duration.ZERO;
        }

        return remaining;
    }
    public void setSessionType(StudySessionConfigState.SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Integer getTargetDurationMinutes() {
        return targetDurationMinutes;
    }

    public void setDurationMinutes(Integer targetDurationMinutes) {
        this.targetDurationMinutes = targetDurationMinutes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(String referenceFile) {
        this.referenceFile = referenceFile;
    }

    @Override
    public String toString() {
        return "StudySessionState{" +
                "sessionType=" + sessionType +
                ", startTime=" + startTime +
                ", targetDurationMinutes=" + targetDurationMinutes +
                ", isActive=" + isActive +
                ", prompt='" + prompt + '\'' +
                ", referenceFile='" + referenceFile + '\'' +
                '}';
    }
}

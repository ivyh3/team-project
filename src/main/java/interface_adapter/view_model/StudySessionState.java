package interface_adapter.view_model;


import java.time.Duration;
import java.time.LocalDateTime;

public class StudySessionState {
    private StudySessionConfigState.SessionType sessionType;
    private LocalDateTime startTime;
    private boolean isActive;
    private String prompt;
    private String referenceFile;
    private Integer targetDurationMinutes; // TODO: Find a better way to work with this

    public StudySessionState(StudySessionConfigState config, LocalDateTime startTime) {
        this.sessionType = config.getSessionType();
        this.targetDurationMinutes = config.getTotalTargetDurationMinutes();
        this.startTime = startTime;
        this.prompt = config.getPrompt();
        this.referenceFile = config.getReferenceFile();

        isActive = true;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDurationElapsed() {
        return Duration.between(startTime, LocalDateTime.now());
    }

    public Duration getRemainingDuration() {
        LocalDateTime targetEndTime = startTime.plusMinutes(targetDurationMinutes);

        Duration remaining = Duration.between(LocalDateTime.now(), targetEndTime);

        if (remaining.isNegative()) {
            return Duration.ZERO;
        }

        return remaining;
    }

    public Integer getTargetDurationMinutes() {
        return targetDurationMinutes;
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

    public StudySessionConfigState.SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(StudySessionConfigState.SessionType sessionType) {
        this.sessionType = sessionType;
    }
}

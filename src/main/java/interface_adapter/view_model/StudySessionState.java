package interface_adapter.view_model;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * State for an active study session.
 */
public class StudySessionState {
    private Integer targetDurationMinutes;
    private StudySessionConfigState.SessionType sessionType;
    private LocalDateTime startTime;
    private boolean isActive;
    private String prompt;
    private File referenceFile;

    // Constructor using a StudySessionConfigState directly
    public StudySessionState(StudySessionConfigState config, LocalDateTime startTime) {
        this.sessionType = config.getSessionType();
        this.startTime = startTime;
        this.targetDurationMinutes = config.getTotalTargetDurationMinutes();
        this.isActive = true;
        this.prompt = config.getPrompt();
        this.referenceFile = config.getReferenceFile() != null ? new File(config.getReferenceFile()) : null;
    }

    /**
     * Full constructor (optional, can still use)
     */
    public StudySessionState(StudySessionConfigState.SessionType sessionType, LocalDateTime startTime,
                             int targetDurationMinutes, boolean isActive, String prompt, File referenceFile) {
        this.sessionType = sessionType;
        this.startTime = startTime;
        this.targetDurationMinutes = targetDurationMinutes;
        this.isActive = isActive;
        this.prompt = prompt;
        this.referenceFile = referenceFile;
    }

    /** Empty state */
    public StudySessionState() { }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDurationElapsed() {
        return Duration.between(startTime, LocalDateTime.now());
    }

    public Duration getRemainingDuration() {
        if (sessionType == StudySessionConfigState.SessionType.VARIABLE) return Duration.ZERO;
        LocalDateTime targetEndTime = startTime.plusMinutes(targetDurationMinutes);
        Duration remaining = Duration.between(LocalDateTime.now(), targetEndTime);
        return remaining.isNegative() ? Duration.ZERO : remaining;
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

    public File getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(File referenceFile) {
        this.referenceFile = referenceFile;
    }

    public StudySessionConfigState.SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(StudySessionConfigState.SessionType sessionType) {
        this.sessionType = sessionType;
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
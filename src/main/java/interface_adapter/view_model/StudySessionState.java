package interface_adapter.view_model;


import java.time.Duration;
import java.time.LocalDateTime;

/**
 * State for an active study session.
 */
public class StudySessionState {
    private final Integer targetDurationMinutes;
    private StudySessionConfigState.SessionType sessionType;
    private LocalDateTime startTime;
    private boolean isActive;
    private String prompt;
    private String referenceFile;

    public StudySessionState(StudySessionConfigState config, LocalDateTime startTime) {
        this.sessionType = config.getSessionType();
        this.targetDurationMinutes = sessionType == StudySessionConfigState.SessionType.FIXED ?
                config.getTotalTargetDurationMinutes() : 0;
        this.startTime = startTime;
        this.prompt = config.getPrompt();
        this.referenceFile = config.getReferenceFile();

        isActive = true;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDurationElapsed() {
        return Duration.between(startTime, LocalDateTime.now());
    }

    /**
     * Return the remaining duration of the study session for a fixed session.
     * If this is a variable session, return Duration.ZERO.
     *
     * @return The remaining duration (if fixed session), or Duration.ZERO (if variable session).
     */
    public Duration getRemainingDuration() {
        if (sessionType == StudySessionConfigState.SessionType.VARIABLE) return Duration.ZERO;

        LocalDateTime targetEndTime = startTime.plusMinutes(targetDurationMinutes);

        Duration remaining = Duration.between(LocalDateTime.now(), targetEndTime);

        if (remaining.isNegative()) {
            return Duration.ZERO;
        }

        return remaining;
    }

    /**
     * Gets the set target duration, in minutes. If this session is a variable session, return 0.
     *
     * @return The target duration, in minutes
     */
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

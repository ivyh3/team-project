package interface_adapter.view_model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * State for the StudySessionEndView.
 *
 * Includes the prompt and references used, along with duration information.
 */
public class StudySessionEndState {
    private String prompt;
    private String referenceFile;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public StudySessionEndState(StudySessionState studySessionState, LocalDateTime endTime) {
        this.startTime = studySessionState.getStartTime();
        this.endTime = endTime;
        this.prompt = studySessionState.getPrompt();

        // Convert File to String path (or name)
        if (studySessionState.getReferenceFile() != null) {
            this.referenceFile = studySessionState.getReferenceFile().getPath();
        }
        else {
            this.referenceFile = null;
        }

        this.duration = Duration.between(startTime, endTime);
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

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "StudySessionEndState{"
                + "prompt='" + prompt + '\''
                + ", referenceFile='" + referenceFile + '\''
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + ", duration=" + duration
                + '}';
    }
}

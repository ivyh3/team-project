package interface_adapter.view_model;

import java.time.Duration;
import java.time.LocalDateTime;

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
        this.referenceFile = studySessionState.getReferenceFile();
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "StudySessionEndState{" +
                "prompt='" + prompt + '\'' +
                ", referenceFile='" + referenceFile + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }
}
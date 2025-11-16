package interface_adapter.view_model;


import java.util.ArrayList;
import java.util.List;

import entity.StudySession;

public class StudySessionConfigState {
    private List<String> fileOptions;
    private SessionType sessionType;
    private Integer targetDurationMinutes; // null if variable session.
    private String prompt;
    private String referenceFile;
    private ConfigStep step;
    private List<String> errors;
    public StudySessionConfigState() {
        this.sessionType = null;
        this.targetDurationMinutes = null;
        this.prompt = null;
        this.referenceFile = null;
        this.errors = new ArrayList<>();
        this.step = ConfigStep.CHOOSE_TYPE;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Integer getTargetDuration() {
        return targetDurationMinutes;
    }

    public void setTargetDuration(Integer targetDurationMinutes) {
        this.targetDurationMinutes = targetDurationMinutes;
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

    public void setStep(ConfigStep step) {
        this.step = step;
    }

    public ConfigStep getStep() {
        return step;
    }

    public List<String> getFileOptions() {
        return fileOptions;
    }

    public void setFileOptions(List<String> fileOptions) {
        this.fileOptions = fileOptions;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public enum SessionType {
        FIXED,
        VARIABLE
    }

    public enum ConfigStep {
        CHOOSE_TYPE,
        CHOOSE_DURATION,
        CHOOSE_REFERENCE,
    }

    public StudySessionConfigState copy() {
        StudySessionConfigState copy = new StudySessionConfigState();
        copy.setFileOptions(this.fileOptions);
        copy.setSessionType(this.sessionType);
        copy.setTargetDuration(this.targetDurationMinutes);
        copy.setPrompt(this.prompt);
        copy.setReferenceFile(this.referenceFile);
        copy.setStep(this.step);
        copy.setErrors(new ArrayList<String>(errors));
        return copy;
    }

    @Override
    public String toString() {
        return "StudySessionConfigState{" +
                "fileOptions=" + fileOptions +
                ", sessionType=" + sessionType +
                ", targetDurationMinutes=" + targetDurationMinutes +
                ", prompt='" + prompt + '\'' +
                ", referenceFile='" + referenceFile + '\'' +
                ", step=" + step +
                ", errors=" + errors +
                '}';
    }
}

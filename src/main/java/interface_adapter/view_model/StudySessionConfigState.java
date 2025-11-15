package interface_adapter.view_model;


public class StudySessionConfigState {
    private SessionType sessionType;
    private Integer targetDurationMinutes; // null if variable session.
    private String prompt;
    private String referenceFile;
    private ConfigStep step;

    public StudySessionConfigState() {
        this.sessionType = null;
        this.targetDurationMinutes = null;
        this.prompt = null;
        this.referenceFile = null;
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

    public enum SessionType {
        FIXED,
        VARIABLE
    }

    public enum ConfigStep {
        CHOOSE_TYPE,
        CHOOSE_DURATION,
        CHOOSE_REFERENCE,
    }

    @Override
    public String toString() {
        return "StudySessionConfigState{" +
                "sessionType=" + sessionType +
                ", targetDurationMinutes=" + targetDurationMinutes +
                ", prompt='" + prompt + '\'' +
                ", referenceFile='" + referenceFile + '\'' +
                ", step=" + step +
                '}';
    }
}

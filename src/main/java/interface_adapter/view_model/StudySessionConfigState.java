package interface_adapter.view_model;


import java.util.Arrays;
import java.util.List;

public class StudySessionConfigState {
    // Todo: need this to be brought from somewhere.
    private List<String> fileOptions = Arrays.asList("mat223.pdf", "longer_textbook_name_adfasdf.pdf", "csc222.pdf", "pdf.pdf");
    private SessionType sessionType;
    private Integer targetDurationHours;
    private Integer targetDurationMinutes;
    private String prompt;
    private String referenceFile;
    private String error;

    public StudySessionConfigState() {
        this.sessionType = SessionType.VARIABLE;
        this.targetDurationMinutes = 0;
        this.targetDurationHours = 0;

        this.prompt = "";
        this.referenceFile = !fileOptions.isEmpty() ? fileOptions.get(0) : "";
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Integer getTargetDurationMinutes() {
        return targetDurationMinutes;
    }

    public void setTargetDurationMinutes(Integer targetDurationMinutes) {
        this.targetDurationMinutes = targetDurationMinutes;
    }

    public Integer getTotalTargetDurationMinutes() {
        int mins = this.targetDurationMinutes == null ? 0 : this.targetDurationMinutes;
        int hours = this.targetDurationHours == null ? 0 : this.targetDurationHours;
        return mins + hours * 60;
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

    public List<String> getFileOptions() {
        return fileOptions;
    }

    public void setFileOptions(List<String> fileOptions) {
        this.fileOptions = fileOptions;
    }

    public Integer getTargetDurationHours() {
        return targetDurationHours;
    }

    public void setTargetDurationHours(Integer targetDurationHours) {
        this.targetDurationHours = targetDurationHours;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public enum SessionType {
        FIXED,
        VARIABLE
    }

    public StudySessionConfigState copy() {
        StudySessionConfigState copy = new StudySessionConfigState();
        copy.setFileOptions(this.fileOptions);
        copy.setSessionType(this.sessionType);
        copy.setTargetDurationMinutes(this.targetDurationMinutes);
        copy.setTargetDurationHours(this.targetDurationHours);
        copy.setPrompt(this.prompt);
        copy.setReferenceFile(this.referenceFile);
        return copy;
    }

    @Override
    public String toString() {
        return "StudySessionConfigState{" +
                "sessionType=" + sessionType +
                ", fileOptions=" + fileOptions +
                ", sessionType=" + sessionType +
                ", targetDurationMinutes=" + targetDurationMinutes +
                ", targetDurationHours=" + targetDurationHours +
                ", prompt='" + prompt + '\'' +
                ", referenceFile='" + referenceFile + '\'' +
                '}';
    }
}
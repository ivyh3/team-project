package interface_adapter.view_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * State for configuring a study session.
 */
public class StudySessionConfigState {
    private static final int MIN_PER_HOUR = 60;
    // Todo: need this to be brought from somewhere.
    private List<String> fileOptions;
    private SessionType sessionType;
    private Integer targetDurationHours;
    private Integer targetDurationMinutes;
    private String prompt;
    private String referenceFile;
    private String error;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public StudySessionConfigState() {
        this.sessionType = SessionType.VARIABLE;
        this.targetDurationMinutes = 0;
        this.targetDurationHours = 0;
        this.fileOptions = new ArrayList<>();
        this.prompt = "";
        this.referenceFile = "";
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * Get the minutes COMPONENT of the target duration, not the total.
     *
     * @return The minutes portion of total study target duration.
     */
    public Integer getTargetDurationMinutes() {
        return targetDurationMinutes;
    }

    public void setTargetDurationMinutes(Integer targetDurationMinutes) {
        this.targetDurationMinutes = targetDurationMinutes;
    }

    /**
     * Return the TOTAL amount of time set as the target, in minutes.
     *
     * @return The total amount of time set as the target duration.
     */
    public Integer getTotalTargetDurationMinutes() {
        final int mins;
        mins = Objects.requireNonNullElse(this.targetDurationMinutes, 0);
        final int hours;
        hours = Objects.requireNonNullElse(this.targetDurationHours, 0);
        return mins + hours * MIN_PER_HOUR;
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

    // In StudySessionConfigState
    public void setFileOptions(List<String> newFileOptions) {
        List<String> oldOptions = this.fileOptions;
        this.fileOptions = newFileOptions;
        pcs.firePropertyChange("fileOptions", oldOptions, newFileOptions);
    }

    /**
     * Return the hours COMPONENT of the target duration, not the total.
     *
     * @return The hours portion of the total study target duration.
     */
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

    /**
     * Return a copy of this state.
     *
     * @return A copy of this state.
     */
    public StudySessionConfigState copy() {
        final StudySessionConfigState copy = new StudySessionConfigState();
        copy.setFileOptions(this.fileOptions);
        copy.setSessionType(this.sessionType);
        copy.setTargetDurationMinutes(this.targetDurationMinutes);
        copy.setTargetDurationHours(this.targetDurationHours);
        copy.setPrompt(this.prompt);
        copy.setReferenceFile(this.referenceFile);
        return copy;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "StudySessionConfigState{"
            + "sessionType=" + sessionType
            + ", fileOptions=" + fileOptions
            + ", sessionType=" + sessionType
            + ", targetDurationMinutes=" + targetDurationMinutes
            + ", targetDurationHours=" + targetDurationHours
            + ", prompt='" + prompt + '\''
            + ", referenceFile='" + referenceFile + '\''
            + '}';
    }

    /**
     * Enum for the two possible configurable session types.
     */
    public enum SessionType {
        FIXED,
        VARIABLE
    }
}
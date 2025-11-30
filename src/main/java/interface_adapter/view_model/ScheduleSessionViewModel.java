package interface_adapter.view_model;

import entity.ScheduledSession;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleSessionViewModel {
    private final PropertyChangeSupport support;
    private final List<ScheduledSession> scheduledSessions;

    private String statusMessage;
    private String errorMessage;  // <-- add this

    public ScheduleSessionViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.scheduledSessions = new ArrayList<>();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public List<ScheduledSession> getScheduledSessions() {
        return new ArrayList<>(scheduledSessions);
    }

    public void addScheduledSession(ScheduledSession session) {
        List<ScheduledSession> oldValue = new ArrayList<>(scheduledSessions);
        this.scheduledSessions.add(session);
        support.firePropertyChange("scheduledSessions", oldValue, scheduledSessions);
    }

    public void setStatusMessage(String message) {
        String old = this.statusMessage;
        this.statusMessage = message;
        support.firePropertyChange("statusMessage", old, message);
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setErrorMessage(String message) {
        String old = this.errorMessage;
        this.errorMessage = message;
        support.firePropertyChange("errorMessage", old, message);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void clearSessions() {
        scheduledSessions.clear();
    }

    public String[] getSessionStrings() {
        return scheduledSessions.stream()
                .map(ScheduledSession::toString)
                .toArray(String[]::new);
    }

    public List<ScheduledSession> getSessionsForDate(LocalDate date) {
        return scheduledSessions.stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());
    }

    public void removeScheduledSession(ScheduledSession session) {
        List<ScheduledSession> oldValue = new ArrayList<>(scheduledSessions);
        if (scheduledSessions.remove(session)) {
            support.firePropertyChange("scheduledSessions", oldValue, scheduledSessions);
        }
    }

}

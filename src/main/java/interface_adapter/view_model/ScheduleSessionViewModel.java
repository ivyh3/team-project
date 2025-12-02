package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewModel for the Schedule Session view.
 * Stores the state and data that the schedule session view needs to display.
 */
public class ScheduleSessionViewModel {
    private final List<ScheduleSessionState> scheduledSessions;
    private final PropertyChangeSupport support;
    private String statusMessage;
    private String errorMessage;

    public ScheduleSessionViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.scheduledSessions = new ArrayList<>();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public List<ScheduleSessionState> getScheduledSessions() {
        return new ArrayList<>(scheduledSessions);
    }

    public void addScheduledSession(ScheduleSessionState sessionState) {
        final List<ScheduleSessionState> oldValue = new ArrayList<>(scheduledSessions);
        this.scheduledSessions.add(sessionState);
        support.firePropertyChange("scheduledSessions", oldValue, scheduledSessions);
    }

    public void setStatusMessage(String message) {
        final String old = this.statusMessage;
        this.statusMessage = message;
        support.firePropertyChange("statusMessage", old, message);
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setErrorMessage(String message) {
        final String old = this.errorMessage;
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
                .map(ScheduleSessionState::toString)
                .toArray(String[]::new);
    }

    public List<ScheduleSessionState> getSessionsForDate(LocalDate date) {
        return scheduledSessions.stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public void removeScheduledSession(ScheduleSessionState sessionState) {
        List<ScheduleSessionState> oldValue = new ArrayList<>(scheduledSessions);
        if (scheduledSessions.remove(sessionState)) {
            support.firePropertyChange("scheduledSessions", oldValue, scheduledSessions);
        }
    }

    /**
     * Removes a scheduled session by its ID.
     * @param sessionId The ID of the session to remove.
     * @return true if a session was removed, false otherwise.
     */
    public boolean removeScheduledSessionById(String sessionId) {
        final List<ScheduleSessionState> oldValue = new ArrayList<>(scheduledSessions);

        final boolean removed = scheduledSessions.removeIf(s -> s.getId().equals(sessionId));

        if (removed) {
            support.firePropertyChange("scheduledSessions", oldValue, scheduledSessions);
        }
        return removed;
    }
}

package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Schedule Session view.
 * Stores the state and data that the schedule session view needs to display.
 */
public class ScheduleSessionViewModel {
	private final PropertyChangeSupport support;
	
	private List<String> scheduledSessions;
	private String selectedCourse;
	private String selectedDate;
	private String selectedStartTime;
	private String selectedEndTime;
	private boolean syncWithCalendar;
	private String statusMessage;
	private String errorMessage;
	private boolean scheduleInProgress;
	
	public ScheduleSessionViewModel() {
		this.support = new PropertyChangeSupport(this);
		this.scheduledSessions = new ArrayList<>();
		this.selectedCourse = "";
		this.selectedDate = "";
		this.selectedStartTime = "";
		this.selectedEndTime = "";
		this.syncWithCalendar = false;
		this.statusMessage = "";
		this.errorMessage = "";
		this.scheduleInProgress = false;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	public List<String> getScheduledSessions() {
		return new ArrayList<>(scheduledSessions);
	}
	
	public void setScheduledSessions(List<String> scheduledSessions) {
		List<String> oldValue = this.scheduledSessions;
		this.scheduledSessions = new ArrayList<>(scheduledSessions);
		support.firePropertyChange("scheduledSessions", oldValue, this.scheduledSessions);
	}
	
	public void addScheduledSession(String session) {
		List<String> oldValue = new ArrayList<>(this.scheduledSessions);
		this.scheduledSessions.add(session);
		support.firePropertyChange("scheduledSessions", oldValue, this.scheduledSessions);
	}
	
	public String getSelectedCourse() {
		return selectedCourse;
	}
	
	public void setSelectedCourse(String selectedCourse) {
		String oldValue = this.selectedCourse;
		this.selectedCourse = selectedCourse;
		support.firePropertyChange("selectedCourse", oldValue, selectedCourse);
	}
	
	public String getSelectedDate() {
		return selectedDate;
	}
	
	public void setSelectedDate(String selectedDate) {
		String oldValue = this.selectedDate;
		this.selectedDate = selectedDate;
		support.firePropertyChange("selectedDate", oldValue, selectedDate);
	}
	
	public String getSelectedStartTime() {
		return selectedStartTime;
	}
	
	public void setSelectedStartTime(String selectedStartTime) {
		String oldValue = this.selectedStartTime;
		this.selectedStartTime = selectedStartTime;
		support.firePropertyChange("selectedStartTime", oldValue, selectedStartTime);
	}
	
	public String getSelectedEndTime() {
		return selectedEndTime;
	}
	
	public void setSelectedEndTime(String selectedEndTime) {
		String oldValue = this.selectedEndTime;
		this.selectedEndTime = selectedEndTime;
		support.firePropertyChange("selectedEndTime", oldValue, selectedEndTime);
	}
	
	public boolean isSyncWithCalendar() {
		return syncWithCalendar;
	}
	
	public void setSyncWithCalendar(boolean syncWithCalendar) {
		boolean oldValue = this.syncWithCalendar;
		this.syncWithCalendar = syncWithCalendar;
		support.firePropertyChange("syncWithCalendar", oldValue, syncWithCalendar);
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public void setStatusMessage(String statusMessage) {
		String oldValue = this.statusMessage;
		this.statusMessage = statusMessage;
		support.firePropertyChange("statusMessage", oldValue, statusMessage);
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		String oldValue = this.errorMessage;
		this.errorMessage = errorMessage;
		support.firePropertyChange("errorMessage", oldValue, errorMessage);
	}
	
	public boolean isScheduleInProgress() {
		return scheduleInProgress;
	}
	
	public void setScheduleInProgress(boolean scheduleInProgress) {
		boolean oldValue = this.scheduleInProgress;
		this.scheduleInProgress = scheduleInProgress;
		support.firePropertyChange("scheduleInProgress", oldValue, scheduleInProgress);
	}
}


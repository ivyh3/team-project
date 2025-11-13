package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for the Study Session view.
 * Stores the state and data that the study session view needs to display.
 */
public class StudySessionViewModel {
	private final PropertyChangeSupport support;
	
	private String sessionId;
	private String courseName;
	private String timerDisplay;
	private boolean sessionActive;
	private String statusMessage;
	private String errorMessage;
	
	public StudySessionViewModel() {
		this.support = new PropertyChangeSupport(this);
		this.sessionId = "";
		this.courseName = "";
		this.timerDisplay = "00:00:00";
		this.sessionActive = false;
		this.statusMessage = "";
		this.errorMessage = "";
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		String oldValue = this.sessionId;
		this.sessionId = sessionId;
		support.firePropertyChange("sessionId", oldValue, sessionId);
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setCourseName(String courseName) {
		String oldValue = this.courseName;
		this.courseName = courseName;
		support.firePropertyChange("courseName", oldValue, courseName);
	}
	
	public String getTimerDisplay() {
		return timerDisplay;
	}
	
	public void setTimerDisplay(String timerDisplay) {
		String oldValue = this.timerDisplay;
		this.timerDisplay = timerDisplay;
		support.firePropertyChange("timerDisplay", oldValue, timerDisplay);
	}
	
	public boolean isSessionActive() {
		return sessionActive;
	}
	
	public void setSessionActive(boolean sessionActive) {
		boolean oldValue = this.sessionActive;
		this.sessionActive = sessionActive;
		support.firePropertyChange("sessionActive", oldValue, sessionActive);
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
}


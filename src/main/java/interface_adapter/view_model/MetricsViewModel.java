package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel for the Metrics view.
 * Stores the state and data that the metrics view needs to display.
 */
public class MetricsViewModel {
	private final PropertyChangeSupport support;
	
	private String averageWeeklyStudyTime;
	private String averageQuizScore;
	private String mostStudiedSubject;
	private Map<String, String> dailyStudyDurations;
	private Map<String, String> courseScores;
	private String selectedCourse;
	private String selectedTimeFilter;
	private String errorMessage;
	
	public MetricsViewModel() {
		this.support = new PropertyChangeSupport(this);
		this.averageWeeklyStudyTime = "--";
		this.averageQuizScore = "--";
		this.mostStudiedSubject = "--";
		this.dailyStudyDurations = new HashMap<>();
		this.courseScores = new HashMap<>();
		this.selectedCourse = "All Courses";
		this.selectedTimeFilter = "This Week";
		this.errorMessage = "";
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	public String getAverageWeeklyStudyTime() {
		return averageWeeklyStudyTime;
	}
	
	public void setAverageWeeklyStudyTime(String averageWeeklyStudyTime) {
		String oldValue = this.averageWeeklyStudyTime;
		this.averageWeeklyStudyTime = averageWeeklyStudyTime;
		support.firePropertyChange("averageWeeklyStudyTime", oldValue, averageWeeklyStudyTime);
	}
	
	public String getAverageQuizScore() {
		return averageQuizScore;
	}
	
	public void setAverageQuizScore(String averageQuizScore) {
		String oldValue = this.averageQuizScore;
		this.averageQuizScore = averageQuizScore;
		support.firePropertyChange("averageQuizScore", oldValue, averageQuizScore);
	}
	
	public String getMostStudiedSubject() {
		return mostStudiedSubject;
	}
	
	public void setMostStudiedSubject(String mostStudiedSubject) {
		String oldValue = this.mostStudiedSubject;
		this.mostStudiedSubject = mostStudiedSubject;
		support.firePropertyChange("mostStudiedSubject", oldValue, mostStudiedSubject);
	}
	
	public Map<String, String> getDailyStudyDurations() {
		return new HashMap<>(dailyStudyDurations);
	}
	
	public void setDailyStudyDurations(Map<String, String> dailyStudyDurations) {
		Map<String, String> oldValue = this.dailyStudyDurations;
		this.dailyStudyDurations = new HashMap<>(dailyStudyDurations);
		support.firePropertyChange("dailyStudyDurations", oldValue, this.dailyStudyDurations);
	}
	
	public Map<String, String> getCourseScores() {
		return new HashMap<>(courseScores);
	}
	
	public void setCourseScores(Map<String, String> courseScores) {
		Map<String, String> oldValue = this.courseScores;
		this.courseScores = new HashMap<>(courseScores);
		support.firePropertyChange("courseScores", oldValue, this.courseScores);
	}
	
	public String getSelectedCourse() {
		return selectedCourse;
	}
	
	public void setSelectedCourse(String selectedCourse) {
		String oldValue = this.selectedCourse;
		this.selectedCourse = selectedCourse;
		support.firePropertyChange("selectedCourse", oldValue, selectedCourse);
	}
	
	public String getSelectedTimeFilter() {
		return selectedTimeFilter;
	}
	
	public void setSelectedTimeFilter(String selectedTimeFilter) {
		String oldValue = this.selectedTimeFilter;
		this.selectedTimeFilter = selectedTimeFilter;
		support.firePropertyChange("selectedTimeFilter", oldValue, selectedTimeFilter);
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


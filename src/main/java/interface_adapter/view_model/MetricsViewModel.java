package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel for the Metrics view.
 * Stores the state and data that the metrics view needs to display.
 */
public class MetricsViewModel {
    private final PropertyChangeSupport support;

    private String averageWeeklyStudyTime;
    private String averageQuizScore; // TODO: change to a mapping
    private String mostStudiedSubject;
    private Map<String, Duration> dailyStudyDurations;
    private Map<String, String> courseScores;
    private String selectedCourse;
    private LocalDateTime startDate;
    private String errorMessage;

    public MetricsViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.averageWeeklyStudyTime = "--";
        this.averageQuizScore = "--";
        this.mostStudiedSubject = "--";
        this.dailyStudyDurations = new HashMap<>();
        this.courseScores = new HashMap<>();
        this.selectedCourse = "All Courses";
        this.startDate = LocalDateTime.now(); // TODO: remove
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

    public Map<String, Duration> getDailyStudyDurations() {
        return new HashMap<>(dailyStudyDurations);
    }

    public void setDailyStudyDurations(Map<String, Duration> dailyStudyDurations) {
        Map<String, Duration> oldValue = this.dailyStudyDurations;
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

    public LocalDateTime getStartDate() { // TODO: remove
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) { // TODO: remove
        LocalDateTime oldValue = this.startDate;
        this.startDate = startDate;
        support.firePropertyChange("startDate", oldValue, startDate);
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

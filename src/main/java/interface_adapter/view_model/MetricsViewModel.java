package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

/**
 * ViewModel for the Metrics view.
 * Stores the state and data that the metrics view needs to display.
 */
public class MetricsViewModel {
    private final PropertyChangeSupport support;
    private Map<DayOfWeek, Duration> dailyStudyDurations;
    private Map<DayOfWeek, Float> averageQuizScores;

    private LocalDateTime startDate;
    private String errorMessage;

    public MetricsViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.averageQuizScores = new EnumMap<>(DayOfWeek.class);
        this.dailyStudyDurations = new EnumMap<>(DayOfWeek.class);
        this.startDate = null;
        this.errorMessage = "";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public Map<DayOfWeek, Duration> getDailyStudyDurations() {
        return new EnumMap<>(dailyStudyDurations);
    }

    public void setDailyStudyDurations(Map<DayOfWeek, Duration> dailyStudyDurations) {
        final Map<DayOfWeek, Duration> oldValue = this.dailyStudyDurations;
        this.dailyStudyDurations = new EnumMap<>(dailyStudyDurations);
        support.firePropertyChange("dailyStudyDurations", oldValue, this.dailyStudyDurations);
    }

    public Map<DayOfWeek, Float> getAverageQuizScores() {
        return new EnumMap<>(averageQuizScores);
    }

    public void setAverageQuizScores(Map<DayOfWeek, Float> averageQuizScores) {
        final Map<DayOfWeek, Float> oldValue = this.averageQuizScores;
        this.averageQuizScores = new EnumMap<>(averageQuizScores);
        support.firePropertyChange("averageQuizScores", oldValue, this.averageQuizScores);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        final LocalDateTime oldValue = this.startDate;
        this.startDate = startDate;
        support.firePropertyChange("startDate", oldValue, startDate);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        final String oldValue = this.errorMessage;
        this.errorMessage = errorMessage;
        support.firePropertyChange("errorMessage", oldValue, errorMessage);
    }
}


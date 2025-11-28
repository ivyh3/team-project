package use_case.view_study_metrics;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Output data for the View Study Metrics use case.
 */
public class ViewStudyMetricsOutputData {
    private final Map<DayOfWeek, Duration> dailyStudyDurations;
    private final Map<DayOfWeek, Float> averageQuizScores;
    private final LocalDateTime startDate;
    
    public ViewStudyMetricsOutputData(Map<DayOfWeek, Duration> dailyStudyDurations,
                                      Map<DayOfWeek, Float> averageQuizScores,
                                      LocalDateTime startDate) {
        this.dailyStudyDurations = dailyStudyDurations;
        this.averageQuizScores = averageQuizScores;
        this.startDate = startDate;
    }
    
    public Map<DayOfWeek, Duration> getDailyStudyDurations() {return dailyStudyDurations;}
    
    public Map<DayOfWeek, Float> getAverageQuizScores() {return averageQuizScores;}

    public LocalDateTime getStartDate() {return startDate; }
}


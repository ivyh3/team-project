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
    private final Duration averageWeeklyStudyTime;
//    private final String mostStudiedSubject;
//    private final Map<String, String> subjectStrengths;
    private final LocalDateTime startDate;
    
    public ViewStudyMetricsOutputData(Map<DayOfWeek, Duration> dailyStudyDurations,
                                      Map<DayOfWeek, Float> averageQuizScores,
                                      Duration averageWeeklyStudyTime,
                                      LocalDateTime startDate) {
        this.dailyStudyDurations = dailyStudyDurations;
        this.averageQuizScores = averageQuizScores;
        this.averageWeeklyStudyTime = averageWeeklyStudyTime;
//        this.mostStudiedSubject = mostStudiedSubject;
//        this.subjectStrengths = subjectStrengths;
        this.startDate = startDate;
    }
    
    public Map<DayOfWeek, Duration> getDailyStudyDurations() {
        return dailyStudyDurations;
    }
    
    public Map<DayOfWeek, Float> getAverageQuizScores() {
        return averageQuizScores;
    }
    
    public Duration getAverageWeeklyStudyTime() {return averageWeeklyStudyTime; }

    
//    public String getMostStudiedSubject() {
//        return mostStudiedSubject;
//    }
//
//    public Map<String, String> getSubjectStrengths() {
//        return subjectStrengths;
//    }
//
    public LocalDateTime getStartDate() {return startDate; }
}


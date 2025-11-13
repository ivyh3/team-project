package use_case.view_study_metrics;

import java.time.Duration;
import java.util.Map;

/**
 * Output data for the View Study Metrics use case.
 */
public class ViewStudyMetricsOutputData {
    private final Map<String, Duration> dailyStudyDurations;
    private final Map<String, Float> averageQuizScores;
    private final Duration averageWeeklyStudyTime;
    private final String mostStudiedSubject;
    private final Map<String, String> subjectStrengths;
    
    public ViewStudyMetricsOutputData(Map<String, Duration> dailyStudyDurations,
                                     Map<String, Float> averageQuizScores,
                                     Duration averageWeeklyStudyTime,
                                     String mostStudiedSubject,
                                     Map<String, String> subjectStrengths) {
        this.dailyStudyDurations = dailyStudyDurations;
        this.averageQuizScores = averageQuizScores;
        this.averageWeeklyStudyTime = averageWeeklyStudyTime;
        this.mostStudiedSubject = mostStudiedSubject;
        this.subjectStrengths = subjectStrengths;
    }
    
    public Map<String, Duration> getDailyStudyDurations() {
        return dailyStudyDurations;
    }
    
    public Map<String, Float> getAverageQuizScores() {
        return averageQuizScores;
    }
    
    public Duration getAverageWeeklyStudyTime() {
        return averageWeeklyStudyTime;
    }
    
    public String getMostStudiedSubject() {
        return mostStudiedSubject;
    }
    
    public Map<String, String> getSubjectStrengths() {
        return subjectStrengths;
    }
}


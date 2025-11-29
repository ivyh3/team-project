package use_case.view_study_metrics;

import entity.StudyQuiz;
import entity.StudySession;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.List;

/**
 * Interactor for the View Study Metrics use case.
 */
public class ViewStudyMetricsInteractor implements ViewStudyMetricsInputBoundary {
    private final ViewStudyMetricsDataAccessInterface metricsDataAccessObject;
    private final ViewStudyMetricsOutputBoundary outputBoundary;

    public ViewStudyMetricsInteractor(ViewStudyMetricsDataAccessInterface viewStudyMetricsDataAccessInterface,
                                      ViewStudyMetricsOutputBoundary outputBoundary) {
        this.metricsDataAccessObject = viewStudyMetricsDataAccessInterface;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ViewStudyMetricsInputData inputData) {
        String userId = inputData.getUser();
        LocalDateTime week = inputData.getWeek();
        List<StudySession> sessions = metricsDataAccessObject.getSessionsPerWeek(userId, week);
        List<StudyQuiz> quizzes = metricsDataAccessObject.getQuizzesPerWeek(userId, week);

        // retrieve the daily study durations
        Map<DayOfWeek, Duration> dailyStudyDurations = new EnumMap<>(DayOfWeek.class);
        for (StudySession session : sessions) {
            dailyStudyDurations.merge(DayOfWeek.from(session.getStartTime()), session.getDuration(), Duration::plus);
        }

        // retrieve the average quiz scores
        Map<DayOfWeek, Float> sumMap = new EnumMap<>(DayOfWeek.class);
        Map<DayOfWeek, Integer> countMap = new EnumMap<>(DayOfWeek.class);

        for (StudyQuiz quiz: quizzes) {
            DayOfWeek day = quiz.getStartTime().getDayOfWeek();
            float score = quiz.getScore() * 100;
            sumMap.merge(day, score, Float::sum);
            countMap.merge(day, 1, Integer::sum);
        }
        Map<DayOfWeek, Float> averageQuizScores = new EnumMap<>(DayOfWeek.class);
        for (Map.Entry<DayOfWeek, Float> entry: sumMap.entrySet()) {
            DayOfWeek day = entry.getKey();
            float sum = entry.getValue();
            int count = countMap.get(day);
            averageQuizScores.put(day, sum / count);
        }

        ViewStudyMetricsOutputData outputData = new ViewStudyMetricsOutputData(
                dailyStudyDurations,
                averageQuizScores,
                week
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}


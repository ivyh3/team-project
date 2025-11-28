package use_case.view_study_metrics;

import entity.StudyQuiz;
import entity.StudySession;
import entity.User;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
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
        Map<DayOfWeek, Duration> dailyStudyDurations = new HashMap<>();
        for (StudySession session : sessions) {
            dailyStudyDurations.merge(DayOfWeek.from(session.getStartTime()), session.getDuration(), Duration::plus);
        }
        System.out.println(dailyStudyDurations); // debugging

        // retrieve the average quiz scores
        Map<DayOfWeek, Float> sumMap = new HashMap<>();
        Map<DayOfWeek, Integer> countMap = new HashMap<>();

        for (StudyQuiz quiz : quizzes) {
            DayOfWeek day = quiz.getStartTime().getDayOfWeek();
            float score = quiz.getScore() * 100;

            sumMap.merge(day, score, Float::sum);
            countMap.merge(day, 1, Integer::sum);
        }
        Map<DayOfWeek, Float> averageQuizScores = new HashMap<>();
        for (DayOfWeek day : sumMap.keySet()) {
            averageQuizScores.put(day, sumMap.get(day) / countMap.get(day));
        }
        System.out.println(averageQuizScores); // debugging

        // get the startDate for the graph
        LocalDateTime startDate = week;

        ViewStudyMetricsOutputData outputData = new ViewStudyMetricsOutputData(
                dailyStudyDurations,
                averageQuizScores,
                startDate
        );

        outputBoundary.prepareSuccessView(outputData);

        // TODO: Prepare success or *failure* view
    }
}


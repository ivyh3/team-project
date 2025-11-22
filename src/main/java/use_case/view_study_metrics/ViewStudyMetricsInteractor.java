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
        User user = inputData.getUser();
        String courseId = inputData.getCourseId();
        LocalDateTime week = inputData.getWeek();
        List<StudySession> sessions = metricsDataAccessObject.getSessionsPerWeek(user, week, courseId);
        List<StudyQuiz> quizzes = metricsDataAccessObject.getQuizzesPerWeek(user, week, courseId);

        // retrieve the daily study durations
        Map<DayOfWeek, Duration> dailyStudyDurations = new HashMap<>();
        for (StudySession session : sessions) {
            dailyStudyDurations.merge(DayOfWeek.from(session.getStartTime()), session.getDuration(), Duration::plus);
        }
        // retrieve the average quiz scores
        Map<DayOfWeek, Float> sumMap = new HashMap<>();
        Map<DayOfWeek, Integer> countMap = new HashMap<>();

        for (StudyQuiz quiz : quizzes) {
            DayOfWeek day = quiz.getStartDate().getDayOfWeek();
            float score = quiz.getScore();

            sumMap.merge(day, score, Float::sum);
            countMap.merge(day, 1, Integer::sum);
        }
        Map<DayOfWeek, Float> averageQuizScores = new HashMap<>();
        for (DayOfWeek day : sumMap.keySet()) {
            averageQuizScores.put(day, sumMap.get(day) / countMap.get(day));
        }

        long times = 0;
        for (Duration value: dailyStudyDurations.values()) {
            times += value.toMinutes();
        }
        Duration averageWeeklyStudyTime = Duration.ofMinutes(times / 7);

        LocalDateTime startDate = week;

//        // testing/dummy data
//        Map<String, Duration> dailyStudyDurations = Map.of(
//                "Sun", Duration.ofMinutes(30),
//                "Mon", Duration.ofMinutes(60),
//                "Tue", Duration.ofMinutes(300),
//                "Wed", Duration.ofMinutes(120),
//                "Thu", Duration.ofMinutes(120),
//                "Fri", Duration.ofMinutes(0),
//                "Sat", Duration.ofMinutes(90)
//        );
//
//        // Average quiz scores per day
//        Map<String, Float> averageQuizScores = Map.of(
//                "Sun", 80f,
//                "Mon", 70f,
//                "Tue", 80f,
//                "Wed", 90f,
//                "Thu", 75f,
//                "Fri", 0f,
//                "Sat", 98f
//        );
//
//        String mostStudiedSubject = "Calculus with Proofs";
//
//        Map<String, String> subjectStrengths = Map.of(
//                "Calculus with Proofs", "Strong",
//                "Literature and the Sciences", "Moderate",
//                "Linear Algebra 1", "Weak"
//        );
//
//        LocalDateTime startDate = LocalDateTime.of(
//                2025, 11, 19, 14, 33);
//
        ViewStudyMetricsOutputData outputData = new ViewStudyMetricsOutputData(
                dailyStudyDurations,
                averageQuizScores,
                averageWeeklyStudyTime,
                startDate
//                mostStudiedSubject, TODO: implement if there's time (need to filter by courses)
//                subjectStrengths
        );

        outputBoundary.prepareSuccessView(outputData);

        // TODO: Implement the business logic for viewing study metrics
        // 1. Fetch all sessions and quizzes for the user (filtered by course/time)
        // 2. Calculate metrics (daily durations, average scores, etc.)
        // 3. Determine strongest/weakest subjects
        // 4. Prepare success or failure view
    }
}


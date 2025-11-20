package use_case.view_study_metrics;

import entity.StudyQuiz;
import interface_adapter.repository.StudySessionRepository;
import interface_adapter.repository.StudyQuizRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

/**
 * Interactor for the View Study Metrics use case.
 */
public class ViewStudyMetricsInteractor implements ViewStudyMetricsInputBoundary {
    private final StudySessionRepository sessionRepository;
    private final StudyQuizRepository quizRepository;
    private final ViewStudyMetricsOutputBoundary outputBoundary;

    public ViewStudyMetricsInteractor(StudySessionRepository sessionRepository,
                                     StudyQuizRepository quizRepository,
                                     ViewStudyMetricsOutputBoundary outputBoundary) {
        this.sessionRepository = sessionRepository;
        this.quizRepository = quizRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ViewStudyMetricsInputData inputData) {
        String userId = inputData.getUserId();
        String courseId = inputData.getCourseId();
        String timeFilter = inputData.getTimeFilter(); // TODO: this should be the "startDate" thing, giving the
                                                       // *sunday* for the preferred week

//      List<StudySession> sessions = StudySessionRepository.findByUserAndCourse(userId, courseId);
        // TODO: find a way to retrieve the dates and durations for each session (given the preferred time filter)

//      List<StudyQuiz> quizzes = StudyQuizRepository.findByUserAndCourse(userId, courseId);


        // testing/dummy data
        Map<String, Duration> dailyStudyDurations = Map.of(
                "Sun", Duration.ofMinutes(30),
                "Mon", Duration.ofMinutes(60),
                "Tue", Duration.ofMinutes(300),
                "Wed", Duration.ofMinutes(120),
                "Thu", Duration.ofMinutes(120),
                "Fri", Duration.ofMinutes(0),
                "Sat", Duration.ofMinutes(90)
        );

        // Average quiz scores per day
        Map<String, Float> averageQuizScores = Map.of(
                "Sun", 80f,
                "Mon", 70f,
                "Tue", 80f,
                "Wed", 90f,
                "Thu", 75f,
                "Fri", 0f,
                "Sat", 98f
        );

        // Weekly average study time (dummy)
        long times = 0;
        for (Duration value: dailyStudyDurations.values()) {
            times += value.toMinutes();
        }
        Duration averageWeeklyStudyTime = Duration.ofMinutes(times / 7);

        String mostStudiedSubject = "Calculus with Proofs";

        Map<String, String> subjectStrengths = Map.of(
                "Calculus with Proofs", "Strong",
                "Literature and the Sciences", "Moderate",
                "Linear Algebra 1", "Weak"
        );

        LocalDateTime startDate = LocalDateTime.of(
                2025, 11, 19, 14, 33);

        ViewStudyMetricsOutputData outputData = new ViewStudyMetricsOutputData(
                dailyStudyDurations,
                averageQuizScores,
                averageWeeklyStudyTime,
                mostStudiedSubject,
                subjectStrengths, //TODO: probably remove this metric
                startDate //TODO: remove and replace with timefilter
        );

        outputBoundary.prepareSuccessView(outputData);

        // TODO: Implement the business logic for viewing study metrics
        // 1. Fetch all sessions and quizzes for the user (filtered by course/time)
        // 2. Calculate metrics (daily durations, average scores, etc.)
        // 3. Determine strongest/weakest subjects
        // 4. Prepare success or failure view
    }
}


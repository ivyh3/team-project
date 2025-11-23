package use_case.view_study_metrics;

import entity.StudyQuiz;
import entity.StudySession;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class StudyMetricsInteractorTest {
    private ViewStudyMetricsInteractor interactor;
    private ViewStudyMetricsDataAccessInterface metricsDAO;
    private ViewStudyMetricsOutputBoundary presenter;
    private User user;

    @BeforeEach
    void setUp() {
        metricsDAO = mock(ViewStudyMetricsDataAccessInterface.class);
        presenter = mock(ViewStudyMetricsOutputBoundary.class);
        interactor = new ViewStudyMetricsInteractor(metricsDAO, presenter);
        user = new User("test_user", "name", "password", LocalDateTime.now());
    }

    @Test
    // check durations make sense when there are multiple sessions in a day
    void dailyStudyDurationsMultipleSessionsTest() {
        LocalDateTime week = LocalDateTime.of(2025, 11, 2, 0, 0);
        String courseId = "all";
        List<StudySession> testSessions = new ArrayList<>();

        // sunday
        testSessions.add(createTestSession("s1", week.plusDays(0), 30, courseId));
        testSessions.add(createTestSession("s2", week.plusDays(0), 30, courseId));

        // monday
        testSessions.add(createTestSession("s3", week.plusDays(1), 90, courseId));

        // tuesday
        testSessions.add(createTestSession("s4", week.plusDays(2), 120, courseId));

        when(metricsDAO.getSessionsPerWeek(user, week, courseId))
                .thenReturn(testSessions);
        when(metricsDAO.getQuizzesPerWeek(user, week, courseId))
                .thenReturn(new ArrayList<>());

        ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(user, courseId, week);
        interactor.execute(inputData);

        ArgumentCaptor<ViewStudyMetricsOutputData> captor =
                ArgumentCaptor.forClass(ViewStudyMetricsOutputData.class);
        verify(presenter).prepareSuccessView(captor.capture());

        ViewStudyMetricsOutputData outputData = captor.getValue();
        Map<DayOfWeek, Duration> dailyDurations = outputData.getDailyStudyDurations();

        assertEquals(Duration.ofMinutes(60), dailyDurations.get(DayOfWeek.SUNDAY));
        assertEquals(Duration.ofMinutes(90), dailyDurations.get(DayOfWeek.MONDAY));
        assertEquals(Duration.ofMinutes(120), dailyDurations.get(DayOfWeek.TUESDAY));
        assertNull(dailyDurations.get(DayOfWeek.WEDNESDAY));

    }

    void dailyStudyDurationsEmptyTest() {
        // TODO: write the test, should be zero and empty
    }

    @Test
    // check average scores make sense when there are multiple quizzes in a day
    void averageQuizScoresMultipleQuizzesTest() {
        LocalDateTime week = LocalDateTime.of(2025, 11, 2, 0, 0);
        String courseId = "all";
        List<StudyQuiz> testQuizzes = new ArrayList<>();

        // sunday: average 85%
        testQuizzes.add(createTestQuiz("q1", week.plusDays(0), 80f, courseId));
        testQuizzes.add(createTestQuiz("q2", week.plusDays(0), 90f, courseId));

        // monday: average 70%
        testQuizzes.add(createTestQuiz("q3", week.plusDays(1), 70f, courseId));

        when(metricsDAO.getSessionsPerWeek(user, week, courseId))
                .thenReturn(new ArrayList<>()); // Empty sessions for this test
        when(metricsDAO.getQuizzesPerWeek(user, week, courseId))
                .thenReturn(testQuizzes);

        ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(user, courseId, week);
        interactor.execute(inputData);

        ArgumentCaptor<ViewStudyMetricsOutputData> captor =
                ArgumentCaptor.forClass(ViewStudyMetricsOutputData.class);
        verify(presenter).prepareSuccessView(captor.capture());

        ViewStudyMetricsOutputData outputData = captor.getValue();
        Map<DayOfWeek, Float> averageScores = outputData.getAverageQuizScores();

        assertEquals(85f, averageScores.get(DayOfWeek.SUNDAY), 0.01);
        assertEquals(70f, averageScores.get(DayOfWeek.MONDAY), 0.01);
        assertNull(averageScores.get(DayOfWeek.TUESDAY));
    }

    void averageQuizScoresEmptyTest() {
        // TODO: write the test, should be zero and empty
    }


    // helpers
    private StudySession createTestSession(String id, LocalDateTime startTime,
                                           int durationMinutes, String courseId) {
        StudySession session = new StudySession(id, user.getName(), courseId, startTime);
        session.setDuration(Duration.ofMinutes(durationMinutes));
        return session;
    }

    private StudyQuiz createTestQuiz(String id, LocalDateTime startDate,
                                     float score, String courseId) {
        StudyQuiz quiz = new StudyQuiz(
                id,
                user.getName(),
                "session_" + id,
                new ArrayList<>(),
                courseId,
                "test quiz",
                List.of("material_1")
        );
        quiz.setScore(score);
        quiz.setStartDate(startDate);
        return quiz;
    }

}

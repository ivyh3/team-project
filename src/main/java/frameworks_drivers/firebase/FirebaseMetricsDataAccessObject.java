package frameworks_drivers.firebase;

import entity.User;
import interface_adapter.view_model.DashboardState;
import entity.StudySession;
import entity.StudyQuiz;
import entity.Question;
import use_case.view_study_metrics.ViewStudyMetricsDataAccessInterface;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

//TODO: replace fake data and implement the getSessionsPerWeek and getQuizzesPerWeek methods from the DAI

/**
 * Temporary mock implementation of ViewStudyMetricsDataAccessInterface.
 * Returns dummy data for testing.
 */
public class FirebaseMetricsDataAccessObject implements ViewStudyMetricsDataAccessInterface {

    private FirebaseStudySessionDataAccessObject sessionDAO;
    private FirebaseStudyQuizDataAccessObject quizDAO;

    public FirebaseMetricsDataAccessObject(FirebaseStudySessionDataAccessObject sessionDAO,
                                           FirebaseStudyQuizDataAccessObject quizDAO) {
        this.sessionDAO = sessionDAO;
        this.quizDAO = quizDAO;
    }

    @Override
    public List<StudySession> getSessionsPerWeek(String userId, LocalDateTime currentTime) {
        // Get date range for a week (starting from the given sunday).
        LocalDateTime start = currentTime.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = currentTime.plusDays(7).truncatedTo(ChronoUnit.DAYS).minusNanos(1);
        List<StudySession> sessions = sessionDAO.getStudySessionsInRange(userId, start, end);
        return sessions;
    }

    @Override
    public List<StudyQuiz> getQuizzesPerWeek(String userId, LocalDateTime currentTime) {
        // Get date range for a week (starting from the given sunday).
        LocalDateTime start = currentTime.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = currentTime.plusDays(7).truncatedTo(ChronoUnit.DAYS).minusNanos(1);

        List<StudyQuiz> quizzes = quizDAO.getStudyQuizzesInRange(userId, start, end);

        return quizzes;
    }
}

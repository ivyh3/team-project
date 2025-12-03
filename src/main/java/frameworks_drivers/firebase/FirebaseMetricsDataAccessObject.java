package frameworks_drivers.firebase;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import entity.StudyQuiz;
import entity.StudySession;
import use_case.view_study_metrics.ViewStudyMetricsDataAccessInterface;

/**
 * Temporary mock implementation of ViewStudyMetricsDataAccessInterface.
 * Returns dummy data for testing.
 */
public class FirebaseMetricsDataAccessObject implements ViewStudyMetricsDataAccessInterface {

    public static final int DAYS_PER_WEEK = 7;
    private final FirebaseStudySessionDataAccessObject studySessionDataAccessObject;
    private final FirebaseStudyQuizDataAccessObject studyQuizDataAccessObject;

    public FirebaseMetricsDataAccessObject(FirebaseStudySessionDataAccessObject studySessionDataAccessObject,
                                           FirebaseStudyQuizDataAccessObject studyQuizDataAccessObject) {
        this.studySessionDataAccessObject = studySessionDataAccessObject;
        this.studyQuizDataAccessObject = studyQuizDataAccessObject;
    }

    @Override
    public List<StudySession> getSessionsPerWeek(String userId, LocalDateTime currentTime) {
        // Get date range for a week (starting from the given sunday).
        final LocalDateTime start = currentTime.truncatedTo(ChronoUnit.DAYS);
        final LocalDateTime end = currentTime.plusDays(DAYS_PER_WEEK).truncatedTo(ChronoUnit.DAYS).minusNanos(1);
        return studySessionDataAccessObject.getStudySessionsInRange(userId, start, end);
    }

    @Override
    public List<StudyQuiz> getQuizzesPerWeek(String userId, LocalDateTime currentTime) {
        // Get date range for a week (starting from the given sunday).
        final LocalDateTime start = currentTime.truncatedTo(ChronoUnit.DAYS);
        final LocalDateTime end = currentTime.plusDays(DAYS_PER_WEEK).truncatedTo(ChronoUnit.DAYS).minusNanos(1);

        return studyQuizDataAccessObject.getStudyQuizzesInRange(userId, start, end);
    }
}

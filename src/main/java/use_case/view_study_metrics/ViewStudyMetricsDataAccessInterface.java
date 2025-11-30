package use_case.view_study_metrics;
import entity.StudyQuiz;
import entity.StudySession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO interface for the View Study Metrics Use Case.
 */
public interface ViewStudyMetricsDataAccessInterface {
    /**
     * Gets the sessions for a user.
     *
     * @param userId     the user id to look for
     * @param week     the Sunday of the week desired
     * @return a list of study sessions
     */
    List<StudySession> getSessionsPerWeek(String userId, LocalDateTime week);

    /**
     * Gets the quizzes for a user.
     *
     * @param userId     the user id to look for
     * @param week     the Sunday of the week desired
     * @return a list of study quizzes
     */
    List<StudyQuiz> getQuizzesPerWeek(String userId, LocalDateTime week);
}


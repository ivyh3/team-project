package use_case.view_study_metrics;
import entity.StudyQuiz;
import entity.StudySession;
import entity.User;
import java.time.LocalDateTime;
import java.util.List;
/**
 * DAO interface for the View Study Metrics Use Case.
 */
public interface ViewStudyMetricsDataAccessInterface {
    /**
     * gets the sessions for a user.
     *
     * @param userId     the user to look for
     * @param week     the Sunday of the week desired
     * @return List<StudySession>
     */
    List<StudySession> getSessionsPerWeek(String userId, LocalDateTime week);

    /**
     * gets the quizzes for a user.
     *
     * @param userId     the user to look for
     * @param week     the Sunday of the week desired
     * @return List<StudyQuiz>
     */
    List<StudyQuiz> getQuizzesPerWeek(String userId, LocalDateTime week);
}


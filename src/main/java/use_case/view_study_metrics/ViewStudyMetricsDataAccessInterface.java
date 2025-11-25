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
     * @param user     the user to look for
     * @param week     the Sunday of the week desired
     * @param courseId the course specified, or "all" for all courses
     * @return List<StudySession>
     */
    List<StudySession> getSessionsPerWeek(User user, LocalDateTime week, String courseId);

    /**
     * gets the quizzes for a user.
     *
     * @param user     the user to look for
     * @param week     the Sunday of the week desired
     * @param courseId the course specified, or "all" for all courses
     * @return List<StudyQuiz>
     */
    List<StudyQuiz> getQuizzesPerWeek(User user, LocalDateTime week, String courseId);
}


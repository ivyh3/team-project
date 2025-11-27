package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Factory for creating StudyQuiz instances.
 */
public class StudyQuizFactory {

    /**
     * Create a StudyQuiz with explicit ID and questions.
     */
    public StudyQuiz create(String id, List<Question> questions, float score,
                            LocalDateTime startDate, LocalDateTime endDate) {
        return new StudyQuiz(id, questions, score, startDate, endDate);
    }

    /**
     * Create a StudyQuiz without an ID.
     */
    public StudyQuiz create(List<Question> questions, float score,
                            LocalDateTime startDate, LocalDateTime endDate) {
        return new StudyQuiz(null, questions, score, startDate, endDate);
    }

    /**
     * Create a StudyQuiz with only score and times, questions will be empty.
     */
    public StudyQuiz create(float score, LocalDateTime startDate, LocalDateTime endDate) {
        return new StudyQuiz(null, List.of(), score, startDate, endDate);
    }
}
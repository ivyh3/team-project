package entity;

/**
 * Factory for StudyQuiz entities.
 */
public class StudyQuizFactory {
    /**
     * Create a study quiz with a firebaseID.
     *
     * @param id        The firebase document id for this StudyQuiz
     * @param score     The score/grade of this quiz, as a float between 0 and 1
     * @param startTime The start time
     * @param endTime   The end time
     * @return The StudyQuizEntity
     */
    public StudyQuiz create(String id, float score, java.time.LocalDateTime startTime,
                            java.time.LocalDateTime endTime) {
        return new StudyQuiz(id, score, startTime, endTime);
    }

    /**
     * Create a study quiz without a firebaseID (i.e., not added to database yet).
     *
     * @param score     The score/grade of this quiz, as a float between 0 and 1
     * @param startTime The start time
     * @param endTime   The end time
     * @return The StudyQuiz Entity
     */
    public StudyQuiz create(float score, java.time.LocalDateTime startTime,
                            java.time.LocalDateTime endTime) {
        return new StudyQuiz(null, score, startTime, endTime);
    }
}

package interface_adapter.repository;

import entity.StudyQuiz;
import java.util.List;

/**
 * Repository interface for StudyQuiz entities.
 */
public interface StudyQuizRepository {
	/**
	 * Saves a study quiz entity.
	 * 
	 * @param quiz the quiz to save
	 */
	void save(StudyQuiz quiz);

	/**
	 * Retrieves a study quiz by its ID.
	 * 
	 * @param userId the user ID
	 * @param quizId the quiz ID
	 * @return the study quiz entity
	 */
	StudyQuiz getById(String userId, String quizId);

	/**
	 * Retrieves all quizzes for a user.
	 * 
	 * @param userId the user ID
	 * @return list of study quizzes
	 */
	List<StudyQuiz> findByUser(String userId);

	/**
	 * Retrieves quizzes for a user filtered by course.
	 * 
	 * @param userId   the user ID
	 * @param courseId the course ID
	 * @return list of study quizzes
	 */
	List<StudyQuiz> findByUserAndCourse(String userId, String courseId);

	/**
	 * Updates a study quiz entity.
	 * 
	 * @param quiz the quiz to update
	 */
	void update(StudyQuiz quiz);

	/**
	 * Deletes a study quiz.
	 * 
	 * @param userId the user ID
	 * @param quizId the quiz ID
	 */
	void delete(String userId, String quizId);
}

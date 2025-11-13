package interface_adapter.repository;

import entity.Course;
import java.util.List;

/**
 * Repository interface for Course entities.
 */
public interface CourseRepository {
	/**
	 * Saves a course entity.
	 * 
	 * @param course the course to save
	 */
	void save(Course course);

	/**
	 * Retrieves a course by its ID.
	 * 
	 * @param userId   the user ID
	 * @param courseId the course ID
	 * @return the course entity
	 */
	Course getById(String userId, String courseId);

	/**
	 * Retrieves all courses for a user.
	 * 
	 * @param userId the user ID
	 * @return list of courses
	 */
	List<Course> findByUser(String userId);

	/**
	 * Updates a course entity.
	 * 
	 * @param course the course to update
	 */
	void update(Course course);

	/**
	 * Deletes a course.
	 * 
	 * @param userId   the user ID
	 * @param courseId the course ID
	 */
	void delete(String userId, String courseId);
}

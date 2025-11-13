package interface_adapter.repository;

import entity.StudySession;
import java.util.List;

/**
 * Repository interface for StudySession entities.
 */
public interface StudySessionRepository {
    /**
     * Saves a study session entity.
     * @param session the session to save
     */
    void save(StudySession session);
    
    /**
     * Retrieves a study session by its ID.
     * @param userId the user ID
     * @param sessionId the session ID
     * @return the study session entity
     */
    StudySession getById(String userId, String sessionId);
    
    /**
     * Updates a study session entity.
     * @param session the session to update
     */
    void update(StudySession session);
    
    /**
     * Retrieves all study sessions for a user.
     * @param userId the user ID
     * @return list of study sessions
     */
    List<StudySession> findByUser(String userId);
    
    /**
     * Retrieves study sessions for a user filtered by course.
     * @param userId the user ID
     * @param courseId the course ID
     * @return list of study sessions
     */
    List<StudySession> findByUserAndCourse(String userId, String courseId);
    
    /**
     * Deletes a study session.
     * @param userId the user ID
     * @param sessionId the session ID
     */
    void delete(String userId, String sessionId);
}


package interface_adapter.repository;

import entity.User;

/**
 * Repository interface for User entities.
 */
public interface UserRepository {
    /**
     * Retrieves a user by their ID.
     * @param userId the user ID
     * @return the user entity
     */
    User getById(String userId);
    
    /**
     * Saves a user entity.
     * @param user the user to save
     */
    void save(User user);
    
    /**
     * Updates a user entity.
     * @param user the user to update
     */
    void update(User user);
    
    /**
     * Deletes a user by their ID.
     * @param userId the user ID
     */
    void delete(String userId);
    
    /**
     * Checks if a user exists by email.
     * @param email the email to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByEmail(String email);
}


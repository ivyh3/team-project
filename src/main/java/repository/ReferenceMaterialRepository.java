package repository;

/**
 * Minimal repository interface used by the interactor.
 * Implementations should return a domain object or null from getById
 * and perform deletion in delete.
 */
public interface ReferenceMaterialRepository {
    /**
     * Return the material object for given userId and materialId, or null if not found.
     */
    Object getById(String userId, String materialId);

    /**
     * Delete the material metadata for given userId and materialId.
     */
    void delete(String userId, String materialId);
}

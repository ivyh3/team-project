package repository;

import entity.ReferenceMaterial;

/**
 * Repository interface for reference materials.
 * Handles retrieval and deletion of reference materials.
 */
public interface ReferenceMaterialRepository {

    /**
     * Returns the reference material for the given userId and materialId,
     * or null if not found.
     */
    ReferenceMaterial getById(String userId, String materialId);

    /**
     * Deletes the reference material for the given userId and materialId.
     */
    void delete(String userId, String materialId);
}

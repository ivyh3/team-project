package use_case.upload_reference_material;

import java.util.Optional;

/**
 * Repository interface for ReferenceMaterial entities.
 * Provides abstract persistence operations for use cases.
 */
public interface ReferenceMaterialRepository {

    /**
     * Finds a reference material by its fingerprint.
     *
     * @param fingerprint the unique fingerprint of the material
     * @return an Optional containing the material if found, or empty if not found
     */
    Optional<ReferenceMaterial> findByFingerprint(String fingerprint);

    /**
     * Saves a reference material to the repository.
     *
     * @param material the material to save
     */
    void save(ReferenceMaterial material);

    /**
     * Finds a reference material by user ID and material ID.
     * Needed for deletion and user-specific queries.
     *
     * @param userId     the ID of the user
     * @param materialId the ID of the material
     * @return an Optional containing the material if found
     */
    Optional<ReferenceMaterial> findByUserIdAndMaterialId(String userId, String materialId);

    /**
     * Deletes a reference material for a given user.
     *
     * @param userId     the ID of the user
     * @param materialId the ID of the material
     */
    void delete(String userId, String materialId);
}

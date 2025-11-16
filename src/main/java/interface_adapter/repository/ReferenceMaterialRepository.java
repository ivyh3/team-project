package interface_adapter.repository;

import entity.ReferenceMaterial;
import java.io.InputStream;
import java.util.List;

/**
 * Repository interface for ReferenceMaterial entities.
 */
public interface ReferenceMaterialRepository {
	/**
	 * Saves a reference material entity.
	 * 
	 * @param material the material to save
	 */
	void save(ReferenceMaterial material);

	/**
	 * Retrieves a reference material by its ID.
	 * 
	 * @param userId     the user ID
	 * @param materialId the material ID
	 * @return the reference material entity
	 */
	ReferenceMaterial getById(String userId, String materialId);

	/**
	 * Retrieves all reference materials for a user.
	 * 
	 * @param userId the user ID
	 * @return list of reference materials
	 */
	List<ReferenceMaterial> findByUser(String userId);

	/**
	 * Retrieves the file stream for a reference material.
	 * 
	 * @param userId     the user ID
	 * @param materialId the material ID
	 * @return input stream of the file
	 */
	InputStream getFileStream(String userId, String materialId);

	/**
	 * Deletes a reference material.
	 * 
	 * @param userId     the user ID
	 * @param materialId the material ID
	 */
	void delete(String userId, String materialId);

	/**
	 * Checks if a material exists by fingerprint.
	 * 
	 * @param userId      the user ID
	 * @param fingerprint the file fingerprint
	 * @return true if the material exists, false otherwise
	 */
	boolean existsByFingerprint(String userId, String fingerprint);

    Object findById(String materialId);

    void deleteById(String materialId);
}

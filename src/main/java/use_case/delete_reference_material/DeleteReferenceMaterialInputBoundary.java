package use_case.delete_reference_material;

import java.util.List;

/**
 * Input boundary for the Delete Reference Material use case.
 * Supports deletion of multiple reference material files at once.
 */
public interface DeleteReferenceMaterialInputBoundary {

    /**
     * Deletes a batch of reference material files for the given user.
     *
     * @param userId    The ID of the user who owns the files.
     * @param fileNames A list of filenames to delete.
     */
    void delete(String userId, List<String> fileNames);
}
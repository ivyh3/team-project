package use_case.delete_reference_material;

import java.util.List;

/**
 * Output boundary for the Delete Reference Material use case.
 */
public interface DeleteReferenceMaterialOutputBoundary {

    /**
     * Called when deletion succeeds.
     *
     * @param deletedFiles list of deleted material file names or paths
     */
    void presentDeletionSuccess(List<String> deletedFiles);

    /**
     * Called when deletion fails.
     *
     * @param errorMessage message to display to the user
     */
    void presentDeletionFailure(String errorMessage);
}
package use_case.delete_reference_material;

/**
 * Output boundary for the Delete Reference Material use case.
 */
public interface DeleteReferenceMaterialOutputBoundary {
    /**
     * Prepares the success view.
     * 
     * @param outputData the output data
     */
    void prepareSuccessView(DeleteReferenceMaterialOutputData outputData);

    /**
     * Prepares the failure view.
     * 
     * @param error the error message
     */
    void prepareFailView(String error);
}

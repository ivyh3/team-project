package use_case.upload_reference_material;

/**
 * Output boundary for the Upload Reference Material use case.
 */
public interface UploadReferenceMaterialOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData the output data
     */
    void prepareSuccessView(UploadReferenceMaterialOutputData outputData);
    
    /**
     * Prepares the failure view.
     * @param error the error message
     */
    void prepareFailView(String error);
}


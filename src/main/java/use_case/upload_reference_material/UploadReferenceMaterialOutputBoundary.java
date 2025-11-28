package use_case.upload_reference_material;

/**
 * Output boundary for the Upload Reference Material use case.
 */
public interface UploadReferenceMaterialOutputBoundary {

    void prepareSuccessView(UploadReferenceMaterialOutputData outputData);

    void prepareFailView(String errorMessage);
}


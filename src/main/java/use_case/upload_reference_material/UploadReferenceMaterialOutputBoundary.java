package use_case.upload_reference_material;

public interface UploadReferenceMaterialOutputBoundary {

    void prepareSuccessView(UploadReferenceMaterialOutputData outputData);

    void prepareFailView(String errorMessage);

    void presentDeletion(String storagePath); // <-- add this
}

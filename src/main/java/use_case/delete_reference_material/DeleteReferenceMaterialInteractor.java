package use_case.delete_reference_material;

import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;
import use_case.upload_reference_material.UploadReferenceMaterialOutputBoundary;

public class DeleteReferenceMaterialInteractor {

    private final UploadReferenceMaterialDataAccessInterface repository;
    private final UploadReferenceMaterialOutputBoundary presenter;

    public DeleteReferenceMaterialInteractor(UploadReferenceMaterialDataAccessInterface repository,
                                             UploadReferenceMaterialOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    public void delete(String userId, String fileName) {
        try {
            repository.deleteFile(userId, fileName);
            presenter.presentDeletion(fileName);// ✅ works now
        } catch (Exception e) {
            presenter.prepareFailView(e.getMessage());
        }
    }
}

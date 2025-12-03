package use_case.delete_reference_material;

import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Interactor for deleting reference materials.
 */
public class DeleteReferenceMaterialInteractor implements DeleteReferenceMaterialInputBoundary {

    private final UploadReferenceMaterialDataAccessInterface fileDAO;
    private final DeleteReferenceMaterialOutputBoundary presenter;

    public DeleteReferenceMaterialInteractor(
            UploadReferenceMaterialDataAccessInterface fileDAO,
            DeleteReferenceMaterialOutputBoundary presenter) {
        this.fileDAO = fileDAO;
        this.presenter = presenter;
    }

    public void delete(String userId, List<String> fileNames) {
        List<String> deletedFiles = new ArrayList<>();
        try {
            for (String fileName : fileNames) {
                fileDAO.deleteFile(userId, fileName);
                deletedFiles.add(fileName);
            }
            presenter.presentDeletionSuccess(deletedFiles);
        } catch (Exception e) {
            presenter.presentDeletionFailure(
                    "Failed to delete files: " + e.getMessage()
            );
        }
    }
}
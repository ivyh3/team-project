package use_case.upload_reference_material;

import frameworks_drivers.firebase.FirebaseFileDataAccessObject;

import java.io.File;

public class UploadReferenceMaterialInteractor implements UploadReferenceMaterialInputBoundary {

    private final FirebaseFileDataAccessObject fileDAO;
    private final UploadReferenceMaterialOutputBoundary presenter;

    public UploadReferenceMaterialInteractor(FirebaseFileDataAccessObject fileDAO,
                                             UploadReferenceMaterialOutputBoundary presenter) {
        this.fileDAO = fileDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(UploadReferenceMaterialInputData inputData) {
        try {
            // Upload the file to Firebase
            String storagePath = fileDAO.uploadFile(inputData.getUserId(), inputData.getFile());

            // Save metadata in Firestore (optional)
            fileDAO.saveMetadata(inputData.getUserId(), storagePath, inputData.getPrompt());

            // Prepare output data for the presenter
            UploadReferenceMaterialOutputData out = new UploadReferenceMaterialOutputData(
                    inputData.getFile().getName(),
                    storagePath,
                    inputData.getPrompt()
            );

            presenter.prepareSuccessView(out);
        } catch (Exception e) {
            presenter.prepareFailView(e.getMessage());
        }
    }

    // Optional delete method (can be removed if handled elsewhere)
    public void delete(String userId, String fileName) {
        try {
            fileDAO.deleteFileWithMetadata(userId, fileName);
            presenter.presentDeletion(String.format("users/%s/%s", userId, fileName));
        } catch (Exception e) {
            presenter.prepareFailView("Deletion failed: " + e.getMessage());
        }
    }
}
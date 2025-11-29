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
            // Upload the file
            String storagePath = fileDAO.uploadFile(inputData.getUserId(), inputData.getFile());

            // Create output data including the prompt
            UploadReferenceMaterialOutputData out =
                    new UploadReferenceMaterialOutputData(
                            inputData.getFile().getName(),
                            storagePath,
                            inputData.getPrompt() // now works
                    );

            // Notify presenter
            presenter.prepareSuccessView(out);
        } catch (Exception e) {
            presenter.prepareFailView(e.getMessage());
        }
    }

    public void delete(String userId, String fileName) {
        try {
            // Compute storage path manually
            String storagePath = String.format("users/%s/%s", userId, fileName);

            // Delete using DAO
            fileDAO.deleteFile(userId, fileName);

            presenter.presentDeletion(storagePath);
        } catch (Exception e) {
            presenter.prepareFailView("Deletion failed: " + e.getMessage());
        }
    }
}


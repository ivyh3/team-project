package use_case.upload_reference_material;

import java.io.File;
import java.util.Objects;

/**
 * Interactor for the Upload Reference Material use case.
 */
public class UploadReferenceMaterialInteractor implements UploadReferenceMaterialInputBoundary {

    private final UploadReferenceMaterialDataAccessInterface fileDAO;
    private final UploadReferenceMaterialOutputBoundary presenter;

    public UploadReferenceMaterialInteractor(
            UploadReferenceMaterialDataAccessInterface fileDAO,
            UploadReferenceMaterialOutputBoundary presenter) {
        this.fileDAO = Objects.requireNonNull(fileDAO, "fileDAO cannot be null");
        this.presenter = Objects.requireNonNull(presenter, "presenter cannot be null");
    }

    @Override
    public void execute(UploadReferenceMaterialInputData inputData) {
        if (inputData == null) {
            presenter.prepareFailView("Upload failed: inputData cannot be null.");
            return;
        }

        try {
            // Validate the file
            File file = inputData.getFile();
            if (file == null || !file.exists() || !file.isFile()) {
                presenter.prepareFailView("Upload failed: file must exist and be a valid file.");
                return;
            }

            // Validate courseCode and title
            String courseCode = inputData.getCourseCode();
            String title = inputData.getTitle();
            if (courseCode == null || courseCode.isEmpty()) {
                presenter.prepareFailView("Upload failed: courseCode cannot be null or empty.");
                return;
            }
            if (title == null || title.isEmpty()) {
                presenter.prepareFailView("Upload failed: title cannot be null or empty.");
                return;
            }

            // Upload the file using DAO
            String storagePath = fileDAO.uploadFile(courseCode, file);
            if (storagePath == null || storagePath.isEmpty()) {
                presenter.prepareFailView("Upload failed: storage path returned by DAO is invalid.");
                return;
            }

            // Save metadata (use title as description)
            fileDAO.saveMetadata(courseCode, storagePath, title);

            // Prepare output for presenter
            UploadReferenceMaterialOutputData outputData = new UploadReferenceMaterialOutputData(
                    file.getName(),
                    storagePath,
                    title
            );
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Upload failed: " + e.getMessage());
        }
    }
}
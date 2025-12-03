package interface_adapter.controller;

import interface_adapter.view_model.ViewManagerModel;
import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;
import use_case.upload_reference_material.UploadReferenceMaterialInputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialInputData;
import view.ViewManager;

import java.io.File;
import java.util.List;

/**
 * Controller responsible for handling reference material uploads.
 */
public class UploadReferenceMaterialController {

    private final UploadReferenceMaterialInputBoundary interactor;
    private final UploadReferenceMaterialDataAccessInterface dataAccess;
    private final ViewManager viewManager;

    public UploadReferenceMaterialController(
            UploadReferenceMaterialInputBoundary interactor,
            UploadReferenceMaterialDataAccessInterface dataAccess,
            ViewManager viewManager) {
        if (interactor == null || dataAccess == null || viewManager == null) {
            throw new IllegalArgumentException("Interactor, DataAccess, and ViewManager cannot be null.");
        }

        this.interactor = interactor;
        this.dataAccess = dataAccess;
        this.viewManager = viewManager;
    }

    /**
     * Uploads a reference material file for a given user.
     *
     * @param userId ID of the user uploading the file
     * @param file   File to upload
     * @param prompt Optional description or prompt associated with the file
     */
    public void uploadReferenceMaterial(String userId, File file, String prompt) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank.");
        }
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("Title/Prompt cannot be null or blank.");
        }
        validateFile(file);

        // Use the correct constructor: (courseCode/userId, title/prompt, file)
        UploadReferenceMaterialInputData inputData =
                new UploadReferenceMaterialInputData(userId, prompt, file);

        interactor.execute(inputData);
    }

    /**
     * Retrieves all reference materials uploaded by the user.
     *
     * @param userId ID of the user
     * @return Array of Files uploaded by the user
     */
    public File[] getUploadedMaterials(String userId) {
        if (userId == null || userId.isBlank()) {
            return new File[0];
        }

        try {
            List<String> filePaths = dataAccess.getAllFiles(userId);
            if (filePaths == null) {
                return new File[0];
            }
            return filePaths.stream().map(File::new).toArray(File[]::new);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve uploaded materials.", e);
        }
    }

    /**
     * Validates that the given file exists and is a proper file.
     *
     * @param file File to validate
     */
    private void validateFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File does not exist or is not valid.");
        }
    }

    /**
     * Navigates to the Upload Materials view.
     */
    public void showManageFilesView() {
        viewManager.showView("uploadMaterialsView"); // ensure this matches your actual view name
    }
}
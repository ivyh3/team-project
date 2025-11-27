package interface_adapter.controller;

import use_case.upload_reference_material.UploadReferenceMaterialInputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialInputData;

import java.io.File;
import java.util.Objects;

/**
 * Controller for the Upload Reference Material use case.
 * Collects user input and delegates the request to the interactor.
 */
public class UploadReferenceMaterialController {

    private final UploadReferenceMaterialInputBoundary interactor;

    /**
     * Constructs the controller with the given interactor.
     *
     * @param interactor the input boundary interactor
     */
    public UploadReferenceMaterialController(UploadReferenceMaterialInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor, "Interactor cannot be null");
    }

    /**
     * Handles a request to upload a reference material file.
     *
     * @param userId the user ID (cannot be null or empty)
     * @param file   the file to upload (cannot be null and must exist)
     * @param prompt a description or prompt for the material (cannot be null or empty)
     */
    public void uploadReferenceMaterial(String userId, File file, String prompt) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File cannot be null and must exist");
        }
        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty");
        }

        UploadReferenceMaterialInputData inputData = new UploadReferenceMaterialInputData(
                userId, file, prompt);
        interactor.execute(inputData);
    }
}

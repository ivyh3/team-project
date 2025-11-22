package interface_adapter.controller;

import use_case.upload_reference_material.UploadReferenceMaterialInputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialInputData;

import java.io.File;

/**
 * Controller for the Upload Reference Material use case.
 */
public class UploadReferenceMaterialController {
    private final UploadReferenceMaterialInputBoundary interactor;

    public UploadReferenceMaterialController(UploadReferenceMaterialInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the upload reference material use case.
     * 
     * @param userId the user ID
     * @param file   the file to upload
     * @param prompt the prompt describing the material
     */
    public void execute(String userId, File file, String prompt) {
        UploadReferenceMaterialInputData inputData = new UploadReferenceMaterialInputData(
                userId, file, prompt);
        interactor.execute(inputData);
    }
}

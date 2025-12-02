package interface_adapter.controller;

import use_case.upload_reference_material.UploadReferenceMaterialInputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialInputData;

import java.io.File;

public class UploadReferenceMaterialController {

    private final UploadReferenceMaterialInputBoundary interactor;

    public UploadReferenceMaterialController(UploadReferenceMaterialInputBoundary interactor) {
        this.interactor = interactor;
    }

    // This is the method your view should call
    public void uploadReferenceMaterial(String userId, File file, String prompt) {
        if (userId == null || userId.isEmpty()) return;
        if (file == null || !file.exists()) return;

        UploadReferenceMaterialInputData inputData = new UploadReferenceMaterialInputData(userId, file, prompt);
        interactor.execute(inputData);
    }
}
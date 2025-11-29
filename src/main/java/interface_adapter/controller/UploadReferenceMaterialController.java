package interface_adapter.controller;

import use_case.upload_reference_material.UploadReferenceMaterialInputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialInputData;

import java.io.File;
import java.io.InputStream;

public class UploadReferenceMaterialController {

    private final UploadReferenceMaterialInputBoundary interactor;

    public UploadReferenceMaterialController(UploadReferenceMaterialInputBoundary interactor) {
        this.interactor = interactor;
    }

    // Change method signature
    public void uploadReferenceMaterial(String userId, File file) {
        if (userId == null || file == null) return;
        interactor.execute(new UploadReferenceMaterialInputData(userId, file, null));
    }

    // NEW: delete method
    public void deleteReferenceMaterial(String userId, String fileName) {
        try {
            interactor.delete(userId, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

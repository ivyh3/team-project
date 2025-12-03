package use_case.list_reference_materials;

import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;

public class ListReferenceMaterialsInteractor implements ListReferenceMaterialsInputBoundary {

    private final UploadReferenceMaterialDataAccessInterface fileDAO;
    private final ListReferenceMaterialsOutputBoundary presenter;

    public ListReferenceMaterialsInteractor(
            UploadReferenceMaterialDataAccessInterface fileDAO,
            ListReferenceMaterialsOutputBoundary presenter) {
        this.fileDAO = fileDAO;
        this.presenter = presenter;
    }

    @Override
    public void listFiles(String userId) {
        try {
            presenter.presentFiles(fileDAO.getAllFiles(userId));
        } catch (Exception e) {
            presenter.presentError("Failed to load reference materials: " + e.getMessage());
        }
    }
}
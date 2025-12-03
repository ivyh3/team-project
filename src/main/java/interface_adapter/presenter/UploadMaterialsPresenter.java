package interface_adapter.presenter;

import interface_adapter.view_model.UploadMaterialsViewModel;
import use_case.upload_reference_material.UploadReferenceMaterialOutputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialOutputData;

/**
 * Presenter for the Upload Reference Material use case.
 */
public class UploadMaterialsPresenter implements UploadReferenceMaterialOutputBoundary {

    private final UploadMaterialsViewModel viewModel;

    public UploadMaterialsPresenter(UploadMaterialsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(UploadReferenceMaterialOutputData outputData) {
        // Add the uploaded file to the ViewModel
        viewModel.addMaterial(outputData.getFileName());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Optionally, handle error (could set an error state in the ViewModel)
        System.err.println("Upload failed: " + errorMessage);
    }

    @Override
    public void presentDeletion(String fileName) {
        // Remove the deleted file from the ViewModel
        viewModel.removeMaterial(fileName);
    }
}
package interface_adapter.presenter;

import interface_adapter.view_model.UploadMaterialsViewModel;
import use_case.delete_reference_material.DeleteReferenceMaterialOutputBoundary;

import java.util.List;

public class DeleteReferenceMaterialPresenter implements DeleteReferenceMaterialOutputBoundary {

    private final UploadMaterialsViewModel viewModel;

    public DeleteReferenceMaterialPresenter(UploadMaterialsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentDeletionSuccess(List<String> deletedFiles) {
        deletedFiles.forEach(viewModel::removeMaterial);
        System.out.println("Deleted files: " + deletedFiles);
    }

    @Override
    public void presentDeletionFailure(String errorMessage) {
        System.err.println("Failed to delete: " + errorMessage);
    }
}
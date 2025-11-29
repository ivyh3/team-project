package interface_adapter.presenter;

import interface_adapter.view_model.DashboardState;
import use_case.upload_reference_material.UploadReferenceMaterialOutputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialOutputData;
import interface_adapter.view_model.UploadMaterialsViewModel;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.ViewManagerModel;

public class UploadMaterialsPresenter implements UploadReferenceMaterialOutputBoundary {

    private final UploadMaterialsViewModel uploadViewModel;
    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public UploadMaterialsPresenter(UploadMaterialsViewModel uploadViewModel,
                                    DashboardViewModel dashboardViewModel,
                                    ViewManagerModel viewManagerModel) {
        this.uploadViewModel = uploadViewModel;
        this.dashboardViewModel = dashboardViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(UploadReferenceMaterialOutputData outputData) {
        String fileName = outputData.getFileName();

        // 2️⃣ Update Dashboard view state
        DashboardState state = dashboardViewModel.getState();
        state.setUploadedFiles(uploadViewModel.getUploadedMaterials()); // ensure this method exists
        dashboardViewModel.setState(state); // triggers property change listeners
    }

    @Override
    public void prepareFailView(String errorMessage) {
        uploadViewModel.showError(errorMessage);
    }

    @Override
    public void presentDeletion(String storagePath) {
        // Extract fileName from storagePath
        String fileName = storagePath.substring(storagePath.lastIndexOf("/") + 1);

        // Remove from UploadMaterials view
        uploadViewModel.removeMaterial(fileName);

        // Update dashboard state as well
        DashboardState state = dashboardViewModel.getState();
        state.setUploadedFiles(uploadViewModel.getUploadedMaterials()); // ensure this method exists
        dashboardViewModel.setState(state);
    }
}

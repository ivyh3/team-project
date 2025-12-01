package interface_adapter.presenter;

import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.UploadMaterialsViewModel;
import interface_adapter.view_model.ViewManagerModel;
import use_case.upload_reference_material.UploadReferenceMaterialOutputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialOutputData;

import java.util.List;

/**
 * Presenter to update UploadMaterialsView and dashboard state.
 */
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
        uploadViewModel.addMaterial(fileName);

        DashboardState state = dashboardViewModel.getState();
        List<String> files = uploadViewModel.getUploadedMaterials();
        state.setUploadedFiles(files);
        dashboardViewModel.setState(state);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        uploadViewModel.showError(errorMessage);
    }

    @Override
    public void presentDeletion(String storagePath) {
        String fileName = storagePath.substring(storagePath.lastIndexOf("/") + 1);
        uploadViewModel.removeMaterial(fileName);

        DashboardState state = dashboardViewModel.getState();
        List<String> files = uploadViewModel.getUploadedMaterials();
        state.setUploadedFiles(files);
        dashboardViewModel.setState(state);
    }
}
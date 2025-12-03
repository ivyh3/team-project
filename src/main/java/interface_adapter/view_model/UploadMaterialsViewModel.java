package interface_adapter.view_model;

import java.util.ArrayList;
import java.util.List;

public class UploadMaterialsViewModel {

    private final String currentUserId;
    private final ViewModel<UploadMaterialsState> viewModel;

    public UploadMaterialsViewModel(String currentUserId) {
        this.currentUserId = currentUserId;
        this.viewModel = new ViewModel<>("uploadMaterials");
        this.viewModel.setState(new UploadMaterialsState());
    }

    public ViewModel<UploadMaterialsState> getViewModel() {
        return viewModel;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    // Adds a material and notifies the view
    public void addMaterial(String fileName) {
        UploadMaterialsState state = viewModel.getState();
        if (state.getUploadedMaterials() == null) {
            state.setUploadedMaterials(new ArrayList<>());
        }
        state.getUploadedMaterials().add(fileName);
        viewModel.firePropertyChange(); // Notify observers
    }

    // Removes a material and notifies the view
    public void removeMaterial(String fileName) {
        UploadMaterialsState state = viewModel.getState();
        if (state.getUploadedMaterials() != null) {
            state.getUploadedMaterials().remove(fileName);
            viewModel.firePropertyChange(); // Notify observers
        }
    }
}
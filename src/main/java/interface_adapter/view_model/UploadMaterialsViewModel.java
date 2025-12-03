package interface_adapter.view_model;

import java.util.ArrayList;

public class UploadMaterialsViewModel {

    private final String currentUserId;
    private final ViewModel<UploadMaterialsState> viewModel;

    public UploadMaterialsViewModel(String userId) {
        this.currentUserId = userId;

        // Initialize the ViewModel with the view name
        this.viewModel = new ViewModel<>("uploadMaterials");

        // Set the initial state
        this.viewModel.setState(new UploadMaterialsState(userId));
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void addMaterial(String fileName) {
        UploadMaterialsState state = viewModel.getState();
        if (state.getUploadedMaterials() == null) {
            state.setUploadedMaterials(new ArrayList<>());
        }
        state.getUploadedMaterials().add(fileName);
        viewModel.firePropertyChange();
    }

    public void removeMaterial(String fileName) {
        UploadMaterialsState state = viewModel.getState();
        if (state.getUploadedMaterials() != null) {
            state.getUploadedMaterials().remove(fileName);
            viewModel.firePropertyChange();
        }
    }

    // Return the internal ViewModel (non-null now)
    public ViewModel<UploadMaterialsState> getViewModel() {
        return viewModel;
    }
}
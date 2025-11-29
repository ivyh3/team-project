package interface_adapter.view_model;

import interface_adapter.view_model.UploadMaterialsState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * ViewModel for UploadMaterialsView.
 * Manages the state and notifies the view when changes occur.
 */
public class UploadMaterialsViewModel {

    private final UploadMaterialsState state;
    private final List<Consumer<UploadMaterialsState>> observers;

    public UploadMaterialsViewModel() {
        this.state = new UploadMaterialsState();
        this.observers = new ArrayList<>();
    }

    // ---------- State Accessors ----------
    public UploadMaterialsState getState() {
        return state;
    }

    // ---------- Observer Methods ----------
    public void addObserver(Consumer<UploadMaterialsState> observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Consumer<UploadMaterialsState> observer : observers) {
            observer.accept(state);
        }
    }

    // ---------- Public API for the View ----------
    public void addMaterial(String materialName) {
        state.addMaterial(materialName);
        notifyObservers();
    }

    public void removeMaterial(String materialName) {
        state.removeMaterial(materialName);
        notifyObservers();
    }

    public void clearMaterials() {
        state.clearMaterials();
        notifyObservers();
    }

    public boolean hasMaterial(String materialName) {
        return state.hasMaterial(materialName);
    }

    public List<String> getUploadedMaterials() {
        return state.getUploadedMaterials();
    }

    public void showError(String errorMessage) {
        // Implement error handling logic if needed
        System.err.println("Error: " + errorMessage);
    }
}

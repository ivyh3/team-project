package interface_adapter.view_model;

import view.StatefulView;
import view.UploadSessionMaterialsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * ViewModel for UploadMaterialsView.
 * Holds uploaded materials and notifies observers on changes.
 */
public class UploadMaterialsViewModel extends ViewModel<UploadMaterialsState> {

    private final List<String> uploadedMaterials = new ArrayList<>();
    private final List<Consumer<List<String>>> observers = new ArrayList<>();

    public UploadMaterialsViewModel() {
        super("uploadMaterials");
    }

    // Register observer
    public void addObserver(Consumer<List<String>> observer) {
        if (observer != null) observers.add(observer);
    }

    // Notify observers
    private void notifyObservers() {
        for (Consumer<List<String>> observer : observers) {
            observer.accept(Collections.unmodifiableList(uploadedMaterials));
        }
    }

    // Add a material
    public void addMaterial(String materialName) {
        if (!uploadedMaterials.contains(materialName)) {
            uploadedMaterials.add(materialName);
            notifyObservers();
        }
    }

    // Remove a material
    public void removeMaterial(String materialName) {
        if (uploadedMaterials.remove(materialName)) {
            notifyObservers();
        }
    }

    // Get list of uploaded materials
    public List<String> getUploadedMaterials() {
        return Collections.unmodifiableList(uploadedMaterials);
    }

    // Show error
    public void showError(String message) {
        System.err.println("Error: " + message);
    }
}
package interface_adapter.view_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the state for the Upload Materials view.
 * Tracks the list of uploaded materials.
 */
public class UploadMaterialsState {

    private final List<String> uploadedMaterials;

    public UploadMaterialsState() {
        this.uploadedMaterials = new ArrayList<>();
    }

    /**
     * Returns an unmodifiable copy of the uploaded materials.
     */
    public List<String> getUploadedMaterials() {
        return Collections.unmodifiableList(uploadedMaterials);
    }

    /**
     * Adds a material to the list.
     */
    public void addMaterial(String materialName) {
        if (materialName != null && !materialName.isEmpty()) {
            uploadedMaterials.add(materialName);
        }
    }

    /**
     * Removes a material from the list.
     */
    public void removeMaterial(String materialName) {
        uploadedMaterials.remove(materialName);
    }

    /**
     * Clears all uploaded materials.
     */
    public void clearMaterials() {
        uploadedMaterials.clear();
    }

    /**
     * Checks if a material exists in the state.
     */
    public boolean hasMaterial(String materialName) {
        return uploadedMaterials.contains(materialName);
    }
}

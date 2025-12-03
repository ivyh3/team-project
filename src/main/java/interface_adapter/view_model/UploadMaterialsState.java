package interface_adapter.view_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * State class for managing uploaded reference materials.
 */
public class UploadMaterialsState {

    private List<String> uploadedMaterials;

    public UploadMaterialsState() {
        this.uploadedMaterials = new ArrayList<>();
    }

    /**
     * Get the list of uploaded materials.
     *
     * @return list of file names
     */
    public List<String> getUploadedMaterials() {
        return uploadedMaterials;
    }

    /**
     * Set the list of uploaded materials.
     *
     * @param uploadedMaterials list of file names
     */
    public void setUploadedMaterials(List<String> uploadedMaterials) {
        this.uploadedMaterials = Objects.requireNonNullElse(uploadedMaterials, new ArrayList<>());
    }

    /**
     * Add a material to the uploaded materials list.
     *
     * @param fileName name of the file to add
     */
    public void addMaterial(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            uploadedMaterials.add(fileName);
        }
    }

    /**
     * Remove a material from the uploaded materials list.
     *
     * @param fileName name of the file to remove
     */
    public void removeMaterial(String fileName) {
        uploadedMaterials.remove(fileName);
    }
}
package interface_adapter.view_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UploadMaterialsState {
    private final String userId;
    private List<String> uploadedMaterials;

    public UploadMaterialsState(String userId) {
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.uploadedMaterials = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getUploadedMaterials() {
        return uploadedMaterials;
    }

    public void setUploadedMaterials(List<String> uploadedMaterials) {
        this.uploadedMaterials = Objects.requireNonNull(uploadedMaterials, "uploadedMaterials cannot be null");
    }

    public void addMaterial(String name) {
        uploadedMaterials.add(name);
    }

    public void removeMaterial(String name) {
        uploadedMaterials.remove(name);
    }
}
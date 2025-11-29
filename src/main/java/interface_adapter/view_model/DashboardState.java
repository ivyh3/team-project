package interface_adapter.view_model;

import java.util.ArrayList;
import java.util.List;

public class DashboardState {
    public static String userId = "";
    private String email = "";
    private List<String> uploadedFiles = new ArrayList<>();

    public DashboardState(DashboardState copy) {
        userId = copy.userId;
        email = copy.email;
    }

    // Because of the previous copy constructor, the default constructor must be
    // explicit.
    public DashboardState() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<String> uploadedFiles) {
        this.uploadedFiles = uploadedFiles != null ? uploadedFiles : new ArrayList<>();
    }
}

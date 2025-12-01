package interface_adapter.view_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The State information representing the logged-in user and dashboard data.
 */
public class DashboardState {
    private String userId = "";
    private String email = "";

    public DashboardState(DashboardState copy) {
        userId = copy.userId;
        email = copy.email;
    }

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

    private List<String> uploadedFiles = new ArrayList<>();

    public DashboardState(List<String> uploadedFiles) {
        this.uploadedFiles = new ArrayList<>(uploadedFiles != null ? uploadedFiles : Collections.emptyList());
    }

    public List<String> getUploadedFiles() {
        return new ArrayList<>(uploadedFiles);
    }

    public void setUploadedFiles(List<String> uploadedFiles) {
        this.uploadedFiles = new ArrayList<>(uploadedFiles != null ? uploadedFiles : Collections.emptyList());
    }

}

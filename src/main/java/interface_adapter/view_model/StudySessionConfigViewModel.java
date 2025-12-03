package interface_adapter.view_model;

import java.util.List;

public class StudySessionConfigViewModel extends ViewModel<StudySessionConfigState> {

    public StudySessionConfigViewModel() {
        super("studySessionConfig");
        this.setState(new StudySessionConfigState());
    }

    // Add this method
    public void setAvailableReferenceFiles(List<String> fileNames) {
        if (fileNames != null && !fileNames.isEmpty()) {
            // Set the first file in the list as the reference file
            this.getState().setReferenceFile(fileNames.get(0));
        }
    }

    public void firePropertyChanged() {
        // If using observer pattern, notify observers here
        // Example: notify all listeners that state changed
    }
}
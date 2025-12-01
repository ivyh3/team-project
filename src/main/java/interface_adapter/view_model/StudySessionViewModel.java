package interface_adapter.view_model;

/**
 * ViewModel for the Study Session view.
 * Stores the state and data that the study session view needs to display.
 */
public class StudySessionViewModel extends ViewModel<StudySessionState> {
    public StudySessionViewModel() {
        super("studySession");
    }
}
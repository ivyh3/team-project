package interface_adapter.view_model;


/**
 * ViewModel to store all required data in starting a study session.
 */
public class StudySessionConfigViewModel extends ViewModel<StudySessionConfigState> {
    public StudySessionConfigViewModel() {
        super("studySessionConfig");
        this.setState(new StudySessionConfigState());
    }
}


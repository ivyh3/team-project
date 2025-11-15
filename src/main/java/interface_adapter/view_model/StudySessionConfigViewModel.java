package interface_adapter.view_model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel to store all required data in starting a study session.
 */
public class StudySessionConfigViewModel extends ViewModel<StudySessionConfigState> {
	
	public StudySessionConfigViewModel() {
        super("studySessionConfig");
        this.setState(new StudySessionConfigState());
	}
}


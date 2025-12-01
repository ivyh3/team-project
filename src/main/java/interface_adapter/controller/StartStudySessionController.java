package interface_adapter.controller;

import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionState;
import use_case.start_study_session.StartStudySessionInputBoundary;
import use_case.start_study_session.StartStudySessionInputData;

import java.io.File;
import java.time.LocalDateTime;

public class StartStudySessionController {
    private final StartStudySessionInputBoundary interactor;

    public StartStudySessionController(StartStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(StudySessionConfigState state) {
        // Create a File object from the selected reference file
        File uploadedFile = state.getReferenceFile() != null
                ? new File(state.getReferenceFile())
                : null;

        // Create StudySessionState
        StudySessionState session = new StudySessionState(
                state.getSessionType(),
                LocalDateTime.now(),
                state.getTotalTargetDurationMinutes(),
                true,
                state.getPrompt(),
                uploadedFile
        );

        // Wrap in input data for the interactor
        StartStudySessionInputData inputData = new StartStudySessionInputData(session);
        interactor.execute(inputData);
    }

    public void abortStudySessionConfig() {
        interactor.abortStudySessionConfig();
    }

    public void refreshFileOptions() {
        interactor.refreshFileOptions();
    }
}
package use_case.start_study_session;

import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;
import interface_adapter.view_model.StudySessionState;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interactor for the start study session use case.
 */
public class StartStudySessionInteractor implements StartStudySessionInputBoundary {
    private final StartStudySessionOutputBoundary presenter;
    private final StartStudySessionDataAccessInterface fileDataAccessObject;

    public StartStudySessionInteractor(StartStudySessionOutputBoundary presenter,
                                       StartStudySessionDataAccessInterface fileRepository) {
        this.presenter = presenter;
        this.fileDataAccessObject = fileRepository;
    }

    @Override
    public void execute(StartStudySessionInputData inputData) {
        final StudySessionState sessionState = inputData.getSessionState();

        // Validation
        if (sessionState.getSessionType() == null) {
            presenter.prepareErrorView("You need a session type selected.");
        } else if (sessionState.getSessionType() == SessionType.FIXED
                && (sessionState.getTargetDurationMinutes() == null || sessionState.getTargetDurationMinutes() <= 0)) {
            presenter.prepareErrorView("Please study a bit more seriously (time can't be zero)!");
        } else if (sessionState.getReferenceFile() == null) {
            presenter.prepareErrorView("Please select a reference file.");
        } else if (sessionState.getPrompt() == null || sessionState.getPrompt().isEmpty()) {
            presenter.prepareErrorView("Please provide a prompt describing what to study.");
        } else {
            final StartStudySessionOutputData outputData = new StartStudySessionOutputData(
                    sessionState,
                    LocalDateTime.now()
            );
            presenter.startStudySession(outputData);
        }
    }

    @Override
    public void abortStudySessionConfig() {
        presenter.abortStudySessionConfig();
    }

    @Override
    public void refreshFileOptions() {
        List<String> fileOptions = fileDataAccessObject.getAllUserFiles(DashboardState.userId);
        if (fileOptions == null || fileOptions.isEmpty()) {
            presenter.prepareErrorView("No textbook files. Go to the settings and add some first.");
            presenter.abortStudySessionConfig();
        } else {
            presenter.refreshFileOptions(fileOptions);
        }
    }

    private boolean checkIfFileExists(String file) {
        return fileDataAccessObject.fileExistsByName(DashboardState.userId, file);
    }
}
package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;
import java.time.LocalDateTime;

/**
 * The interactor for the start study session use case.
 */
public class StartStudySessionInteractor implements StartStudySessionInputBoundary {
    private final StartStudySessionOutputBoundary presenter;
    // private ReferenceFilesDataAccessInterface referenceFilesDataAccessObject

    public StartStudySessionInteractor(StartStudySessionOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(StartStudySessionInputData inputData) {
        StudySessionConfigState config = inputData.getConfig();

        // Validate.
        if (config.getSessionType() == null) {
            presenter.prepareErrorView("You need a session type selected.");
            return;
        } else if (config.getSessionType() == SessionType.FIXED && (config.getTotalTargetDurationMinutes() == null || config.getTotalTargetDurationMinutes() <= 0)) {
            presenter.prepareErrorView("Please study a bit more seriously (time can't be zero)!");
            return;
        } else if (config.getReferenceFile() == null || config.getReferenceFile().isEmpty()) {
            presenter.prepareErrorView("Please select a reference file.");
            return;
        } else if (!checkIfFileExists(config.getReferenceFile())) {
            presenter.prepareErrorView("Reference file does not exist in storage..? Use another one.");
            return;
        } else if (config.getPrompt() == null || config.getPrompt().isEmpty()) {
            presenter.prepareErrorView("Please provide a prompt describing what to study.");
            return;
        }

        // Passed all validation checks. Start the study session.
        StartStudySessionOutputData outputData = new StartStudySessionOutputData(
                config,
                LocalDateTime.now()
        );

        presenter.startStudySession(outputData);
    }

    @Override
    public void abortStudySessionConfig() {
        presenter.abortStudySessionConfig();
    }

    @Override
    public void setSessionType(SessionType sessionType) {
        presenter.setSessionType(sessionType);
    }

    /**
     * Check if the given file resource exists in storage.
     * @param file The file to check.
     * @return whether the file exists.
     */
    private boolean checkIfFileExists(String file) {
        return true; // Temporary.
    }

}

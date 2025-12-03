package use_case.start_study_session;

import java.time.LocalDateTime;
import java.util.List;

import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;

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

    /**
     * Executes start study session use case.
     *
     * @param inputData The input data
     */
    @Override
    public void execute(StartStudySessionInputData inputData) {
        final StudySessionConfigState config = inputData.getConfig();
        final String userId = inputData.getUserId();
        // Validate.
        if (config.getSessionType() == null) {
            presenter.prepareErrorView("You need a session type selected.");
        }
        else if (config.getSessionType() == SessionType.FIXED
                && (config.getTotalTargetDurationMinutes() == null || config.getTotalTargetDurationMinutes() <= 0)) {
            presenter.prepareErrorView("Please study a bit more seriously (time can't be zero)!");
        }
        else if (config.getReferenceFile() == null || config.getReferenceFile().isEmpty()) {
            presenter.prepareErrorView("Please select a reference file.");
        }
        else if (!checkIfFileExists(userId, config.getReferenceFile())) {
            presenter.prepareErrorView("Reference file does not exist in storage..? Use another one.");
        }
        else if (config.getPrompt() == null || config.getPrompt().isEmpty()) {
            presenter.prepareErrorView("Please provide a prompt describing what to study.");
        }
        else {
            // Passed all validation checks. Start the study session.
            final StartStudySessionOutputData outputData = new StartStudySessionOutputData(
                    config,
                    LocalDateTime.now());

            presenter.startStudySession(outputData);
        }
    }

    @Override
    public void abortStudySessionConfig() {
        presenter.abortStudySessionConfig();
    }

    /**
     * Check if the given file resource exists in storage.
     *
     * @param userId The id of the current user
     * @param file   The file to check.
     * @return whether the file exists.
     */
    private boolean checkIfFileExists(String userId, String file) {
        return fileDataAccessObject.fileExistsByName(userId, file);
    }

    @Override
    public void refreshFileOptions(String userId) {
        final List<String> fileOptions = fileDataAccessObject.getAllUserFiles(userId);
        if (fileOptions == null || fileOptions.isEmpty()) {
            presenter.prepareErrorView("No textbook files. Go to the settings and add some first.");
            presenter.abortStudySessionConfig();
        }
        else {
            presenter.refreshFileOptions(fileOptions);
        }
    }

}
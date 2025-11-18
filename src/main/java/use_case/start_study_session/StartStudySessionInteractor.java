package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;
import interface_adapter.view_model.StudySessionState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartStudySessionInteractor implements StartStudySessionInputBoundary {
    private final StartStudySessionOutputBoundary presenter;
    // private ReferenceFilesDataAccessInterface referenceFilesDataAccessObject
    private final List<String> files = Arrays.asList("mat223.pdf", "longer_textbook_name_adfasdf.pdf", "csc222.pdf", "pdf.pdf");

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

        // TODO: Have two seperate views for study sessions, and I guess would need two view model state classes then
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

    private boolean checkIfFileExists(String file) {
        return true; // Temporary.
    }

}

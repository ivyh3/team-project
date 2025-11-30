package use_case.start_study_session;

import java.util.List;

/**
 * Output boundary for the start study session use case.
 */
public interface StartStudySessionOutputBoundary {
    /**
     * Start the study session.
     *
     * @param outputData The output data.
     */
    void startStudySession(StartStudySessionOutputData outputData);

    /**
     * Prepare a failure view or prompt.
     *
     * @param errorMessage The explanation for the error.
     */
    void prepareErrorView(String errorMessage);

    /**
     * Cancel the study session configuration, and return home.
     */
    void abortStudySessionConfig();

    /**
     * Sets the possible file selections for the file selector.
     *
     * @param fileOptions  The file options that are selectable
     */
    void refreshFileOptions(List<String> fileOptions);
}

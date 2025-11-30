package use_case.start_study_session;

import frameworks_drivers.database.InMemoryDatabase;
import interface_adapter.view_model.StudySessionConfigState;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StartStudySessionTest {

    @Test
    void startFixedStudySessionTest() {
        StartStudySessionDataAccessInterface database = new InMemoryDatabase(
                Map.of("csc236.pdf", "csc236.pdf"));
        StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.FIXED);
        config.setTargetDurationHours(2);
        config.setTargetDurationMinutes(30);
        config.setReferenceFile("csc236.pdf");
        config.setPrompt("Recursive correctness");

        StartStudySessionInputData inputData = new StartStudySessionInputData(
                config
        );

        StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {

            @Override
            public void startStudySession(StartStudySessionOutputData outputData) {
                StudySessionConfigState outputState = outputData.getConfig();

                // Check if final output config state correct
                assertEquals(StudySessionConfigState.SessionType.FIXED, outputState.getSessionType());
                assertEquals(2 * 60 + 30, outputState.getTotalTargetDurationMinutes());
                assertEquals("Recursive correctness", outputState.getPrompt());
                assertEquals("csc236.pdf", outputState.getReferenceFile());

                assertNotNull(outputData.getStartTime()); // Make sure a start time was provided
            }

            @Override
            public void prepareErrorView(String errorMessage) {
                fail("An error is unexpected:" + errorMessage);
            }

            @Override
            public void abortStudySessionConfig() {
                fail("Aborting the config is unexpected.");
            }

            @Override
            public void refreshFileOptions(List<String> fileOptions) {
                fail("Refreshing file options is unexpected.");
            }
        };

        StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter, database);
        interactor.execute(inputData);

    }

    @Test
    void startVariableStudySessionTest() {
        StartStudySessionDataAccessInterface database = new InMemoryDatabase(
                Map.of("csc236.pdf", "csc236.pdf"));
        StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
        config.setReferenceFile("csc236.pdf");
        config.setPrompt("Recursive correctness");

        StartStudySessionInputData inputData = new StartStudySessionInputData(
                config
        );

        StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {

            @Override
            public void startStudySession(StartStudySessionOutputData outputData) {
                StudySessionConfigState outputState = outputData.getConfig();

                // Check if final output config state correct
                assertEquals(StudySessionConfigState.SessionType.VARIABLE, outputState.getSessionType());
                assertEquals("Recursive correctness", outputState.getPrompt());
                assertEquals("csc236.pdf", outputState.getReferenceFile());

                assertNotNull(outputData.getStartTime()); // Make sure a start time was provided
            }

            @Override
            public void prepareErrorView(String errorMessage) {
                fail("An error is unexpected:" + errorMessage);
            }

            @Override
            public void abortStudySessionConfig() {
                fail("Aborting the config is unexpected.");
            }

            @Override
            public void refreshFileOptions(List<String> fileOptions) {
                fail("Refreshing file options is unexpected.");
            }
        };

        StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter, database);
        interactor.execute(inputData);

    }

    @Test
    void abortConfigTest() {
        StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {

            @Override
            public void startStudySession(StartStudySessionOutputData outputData) {
                fail("Starting the session is unexpected.");
            }

            @Override
            public void prepareErrorView(String errorMessage) {
                fail("An error is unexpected:" + errorMessage);
            }

            @Override
            public void abortStudySessionConfig() {
                // Nothing to test here as no output data.
            }

            @Override
            public void refreshFileOptions(List<String> fileOptions) {
                fail("Refreshing file options is unexpected.");
            }
        };

        StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter,
                new InMemoryDatabase());
        interactor.abortStudySessionConfig();
    }

    @Test
    void failurePromptNotAddedTest() {
        StartStudySessionDataAccessInterface database = new InMemoryDatabase(
                Map.of("csc236.pdf", "csc236.pdf"));
        StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
        config.setReferenceFile("csc236.pdf");
        // Prompt was not set, any other information is valid.

        StartStudySessionInputData inputData = new StartStudySessionInputData(
                config
        );

        StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {

            @Override
            public void startStudySession(StartStudySessionOutputData outputData) {
                fail("Starting the session is unexpected.");
            }

            @Override
            public void prepareErrorView(String errorMessage) {
                assertEquals("Please provide a prompt describing what to study.", errorMessage);
            }

            @Override
            public void abortStudySessionConfig() {
                fail("Aborting the config is unexpected.");
            }

            @Override
            public void refreshFileOptions(List<String> fileOptions) {
                fail("Refreshing file options is unexpected.");
            }
        };

        StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter,
                database);
        interactor.execute(inputData);
    }

    @Test
    void failureNoDurationSpecifiedFixedSessionTest() {
        StartStudySessionDataAccessInterface database = new InMemoryDatabase(
                Map.of("csc236.pdf", "csc236.pdf"));
        StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.FIXED);
        config.setReferenceFile("csc236.pdf");
        // Total study target set to 0.
        config.setTargetDurationHours(0);
        config.setTargetDurationMinutes(0);
        config.setPrompt("Recursive correctness");

        StartStudySessionInputData inputData = new StartStudySessionInputData(
                config
        );

        StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {

            @Override
            public void startStudySession(StartStudySessionOutputData outputData) {
                fail("Starting the session is unexpected.");
            }

            @Override
            public void prepareErrorView(String errorMessage) {
                assertEquals("Please study a bit more seriously (time can't be zero)!", errorMessage);
            }

            @Override
            public void abortStudySessionConfig() {
                fail("Aborting the config is unexpected.");
            }

            @Override
            public void refreshFileOptions(List<String> fileOptions) {
                fail("Refreshing file options is unexpected.");
            }
        };

        StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter,
                database);
        interactor.execute(inputData);
    }

    // TODO: Add a test for refreshing file options.
}

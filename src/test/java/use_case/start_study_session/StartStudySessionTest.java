package use_case.start_study_session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import frameworks_drivers.database.InMemoryDatabase;
import interface_adapter.view_model.StudySessionConfigState;

class StartStudySessionTest {

    @Test
    void startFixedStudySessionTest() {
        final int durationHours = 2;
        final int durationMins = 30;
        final int minPerHour = 60;

        final StartStudySessionDataAccessInterface database = new InMemoryDatabase(
            Map.of("csc236.pdf", "csc236.pdf"));
        final StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.FIXED);
        config.setTargetDurationHours(durationHours);
        config.setTargetDurationMinutes(durationMins);
        config.setReferenceFile("csc236.pdf");
        config.setPrompt("Recursive correctness");

        final StartStudySessionInputData inputData = new StartStudySessionInputData(
            "testUser",
            config
        );

        final StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {
            @Override
            public void startStudySession(StartStudySessionOutputData outputData) {
                final StudySessionConfigState outputState = outputData.getConfig();

                // Check if final output config state correct
                assertEquals(StudySessionConfigState.SessionType.FIXED, outputState.getSessionType());
                assertEquals(durationHours * minPerHour + durationMins, outputState.getTotalTargetDurationMinutes());
                assertEquals("Recursive correctness", outputState.getPrompt());
                assertEquals("csc236.pdf", outputState.getReferenceFile());

                // Make sure a start time was provided
                assertNotNull(outputData.getStartTime());
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

        final StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter, database);
        interactor.execute(inputData);

    }

    @Test
    void startVariableStudySessionTest() {
        final StartStudySessionDataAccessInterface database = new InMemoryDatabase(
            Map.of("csc236.pdf", "csc236.pdf"));
        final StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
        config.setReferenceFile("csc236.pdf");
        config.setPrompt("Recursive correctness");

        final StartStudySessionInputData inputData = new StartStudySessionInputData(
            "testUser",
            config
        );

        final StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {
            @Override
            public void startStudySession(StartStudySessionOutputData outputData) {
                final StudySessionConfigState outputState = outputData.getConfig();

                // Check if final output config state correct
                assertEquals(StudySessionConfigState.SessionType.VARIABLE, outputState.getSessionType());
                assertEquals("Recursive correctness", outputState.getPrompt());
                assertEquals("csc236.pdf", outputState.getReferenceFile());

                // Make sure a start time was provided
                assertNotNull(outputData.getStartTime());
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

        final StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter, database);
        interactor.execute(inputData);

    }

    @Test
    void abortConfigTest() {
        final StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {

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

        final StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter,
            new InMemoryDatabase());
        interactor.abortStudySessionConfig();
    }

    @Test
    void failurePromptNotAddedTest() {
        final StartStudySessionDataAccessInterface database = new InMemoryDatabase(
            Map.of("csc236.pdf", "csc236.pdf"));
        final StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
        config.setReferenceFile("csc236.pdf");
        // Prompt was not set, any other information is valid.

        final StartStudySessionInputData inputData = new StartStudySessionInputData(
            "testUser",
            config
        );

        final StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {

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

        final StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter,
            database);
        interactor.execute(inputData);
    }

    @Test
    void failureNoDurationSpecifiedFixedSessionTest() {
        final StartStudySessionDataAccessInterface database = new InMemoryDatabase(
            Map.of("csc236.pdf", "csc236.pdf"));
        final StudySessionConfigState config = new StudySessionConfigState();
        config.setSessionType(StudySessionConfigState.SessionType.FIXED);
        config.setReferenceFile("csc236.pdf");
        // Total study target set to 0.
        config.setTargetDurationHours(0);
        config.setTargetDurationMinutes(0);
        config.setPrompt("Recursive correctness");

        final StartStudySessionInputData inputData = new StartStudySessionInputData(
            "testUser",
            config
        );

        final StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {
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

        final StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter,
            database);
        interactor.execute(inputData);
    }

    @Test
    void testFileRefresh() {
        final StartStudySessionDataAccessInterface database = new InMemoryDatabase(
            Map.of("csc236.pdf", "csc263.pdf",
                "mat223.pdf", "mat223.pdf"));

        final StartStudySessionOutputBoundary successPresenter = new StartStudySessionOutputBoundary() {
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
                assertEquals("csc236.pdf", fileOptions.get(0));
                assertEquals("mat223.pdf", fileOptions.get(1));
            }
        };

        final StartStudySessionInputBoundary interactor = new StartStudySessionInteractor(successPresenter,
            database);

        interactor.refreshFileOptions("testUser");

    }
}

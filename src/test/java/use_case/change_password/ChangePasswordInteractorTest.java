package use_case.change_password;

import entity.UserFactory;
import frameworks_drivers.database.InMemoryUserDataAccessObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class ChangePasswordInteractorTest {

    @Test
    void successTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for successTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "oldpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "oldpassword", "newpassword",
                "newpassword");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                // Password was changed successfully
                assert userDataAccessObject.verifyPassword("test@example.com", "newpassword");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyOldPasswordTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for failureEmptyOldPasswordTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "oldpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "", "newpassword123",
                "newpassword123");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Current password cannot be empty", errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyNewPasswordTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for failureEmptyNewPasswordTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "oldpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "oldpassword", "", "");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("New password cannot be empty", errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyConfirmPasswordTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for failureEmptyConfirmPasswordTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "oldpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "oldpassword", "newpassword123",
                "");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Confirm password cannot be empty", errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failurePasswordsDoNotMatchTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for failurePasswordsDoNotMatchTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "oldpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "oldpassword", "newpassword123",
                "differentpassword");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("New passwords do not match", errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureNewPasswordTooShortTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for failureNewPasswordTooShortTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "oldpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "oldpassword", "12345", "12345");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("New password must be at least 6 characters", errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureNewPasswordSameAsOldTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for failureNewPasswordSameAsOldTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "oldpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "oldpassword", "oldpassword",
                "oldpassword");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("New password must be different from current password", errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureIncorrectOldPasswordTest() {
        final ChangePasswordUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(
                new UserFactory());

        // User set up for failureIncorrectOldPasswordTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "correctpassword");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com")
                .getUserId();

        final ChangePasswordInputData inputData = new ChangePasswordInputData(userId, "wrongpassword", "newpassword123",
                "newpassword123");

        final ChangePasswordOutputBoundary userPresenter = new ChangePasswordOutputBoundary() {
            @Override
            public void prepareSuccessView(ChangePasswordOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Current password is incorrect", errorMessage);
            }
        };

        final ChangePasswordInteractor interactor = new ChangePasswordInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }
}

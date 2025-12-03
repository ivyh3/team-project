package use_case.signup;

import entity.UserFactory;
import frameworks_drivers.database.InMemoryUserDataAccessObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class SignupInteractorTest {

    @Test
    void successTest() {
        final SignupUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final SignupInputData inputData = new SignupInputData("newuser@example.com", "password123", "password123");


        final SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("newuser@example.com").getUserId();
                assertEquals(userId, outputData.getUserId());
                assertEquals("newuser@example.com", outputData.getEmail());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        final SignupInteractor interactor = new SignupInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyEmailTest() {
        final SignupUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final SignupInputData inputData = new SignupInputData("", "password123", "password123");

        final SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Email cannot be empty", errorMessage);
            }
        };

        final SignupInteractor interactor = new SignupInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyPasswordTest() {
        final SignupUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final SignupInputData inputData = new SignupInputData("test@example.com", "", "");

        final SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Password cannot be empty", errorMessage);
            }
        };

        final SignupInteractor interactor = new SignupInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failurePasswordTooShortTest() {
        final SignupUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final SignupInputData inputData = new SignupInputData("test@example.com", "12345", "12345");

        final SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Password must be at least 6 characters", errorMessage);
            }
        };

        final SignupInteractor interactor = new SignupInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUserAlreadyExistsTest() {
        final SignupUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        // User set up for failureUserAlreadyExistsTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("existing@example.com", "password123");

        final SignupInputData inputData = new SignupInputData("existing@example.com", "password123", "password123");

        final SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User already exists", errorMessage);
            }
        };

        final SignupInteractor interactor = new SignupInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failurePasswordsDoNotMatchTest() {
        final SignupUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final SignupInputData inputData = new SignupInputData("test@example.com", "password123", "differentpassword");

        final SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Passwords don't match", errorMessage);
            }
        };

        final SignupInteractor interactor = new SignupInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }
}

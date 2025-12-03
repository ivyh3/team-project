package use_case.login;

import entity.UserFactory;
import frameworks_drivers.database.InMemoryUserDataAccessObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class LoginInteractorTest {

    @Test
    void successTest() {
        final LoginUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        // User set up for successTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "password123");
        final String userId = ((InMemoryUserDataAccessObject) userDataAccessObject).getUserByEmail("test@example.com").getUserId();

        final LoginInputData inputData = new LoginInputData("test@example.com", "password123");

        final LoginOutputBoundary userPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                assertEquals(userId, outputData.getUserId());
                assertEquals("test@example.com", outputData.getEmail());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        final LoginInteractor interactor = new LoginInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyEmailTest() {
        final LoginUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final LoginInputData inputData = new LoginInputData("", "password123");

        final LoginOutputBoundary userPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Email cannot be empty", errorMessage);
            }
        };

        final LoginInteractor interactor = new LoginInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyPasswordTest() {
        final LoginUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final LoginInputData inputData = new LoginInputData("test@example.com", "");

        final LoginOutputBoundary userPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Password cannot be empty", errorMessage);
            }
        };

        final LoginInteractor interactor = new LoginInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUserDoesNotExistTest() {
        final LoginUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        final LoginInputData inputData = new LoginInputData("nonexistent@example.com", "password123");

        final LoginOutputBoundary userPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("nonexistent@example.com: Account does not exist.", errorMessage);
            }
        };

        final LoginInteractor interactor = new LoginInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureIncorrectPasswordTest() {
        final LoginUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(new UserFactory());

        // User set up for failureIncorrectPasswordTest
        ((InMemoryUserDataAccessObject) userDataAccessObject).createUser("test@example.com", "password123");

        final LoginInputData inputData = new LoginInputData("test@example.com", "wrongpassword");

        final LoginOutputBoundary userPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Incorrect password for \"test@example.com\".", errorMessage);
            }
        };

        final LoginInteractor interactor = new LoginInteractor(userDataAccessObject, userPresenter);
        interactor.execute(inputData);
    }
}

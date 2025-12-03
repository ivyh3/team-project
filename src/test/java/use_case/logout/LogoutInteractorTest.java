package use_case.logout;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class LogoutInteractorTest {

    @Test
    void successTest() {
        final LogoutOutputBoundary userPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData outputData) {
                // Logout should always succeed
                assertNotNull(outputData);
            }
        };

        final LogoutInteractor interactor = new LogoutInteractor(userPresenter);
        interactor.execute();
    }
}


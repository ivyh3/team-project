package app;


// TODO: PUT EVERYTHING IN THE PROPER PLACE

import entity.UserFactory;
import frameworks_drivers.TEMP.FileUserDataAccessObject;
import interface_adapter.controller.EndStudySessionController;
import interface_adapter.controller.StartStudySessionController;
import interface_adapter.controller.ViewStudyMetricsController;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.InitialViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.presenter.EndStudySessionPresenter;
import interface_adapter.presenter.StartStudySessionPresenter;
import interface_adapter.presenter.ViewStudyMetricsPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.view_model.*;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.end_study_session.EndStudySessionInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.start_study_session.StartStudySessionInteractor;
import use_case.view_study_metrics.ViewStudyMetricsInteractor;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    public static final ViewManagerModel viewManagerModel = new ViewManagerModel();
    // TODO: Sort things out.
    final UserFactory userFactory = new UserFactory();
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    private final String appTitle = "My title";
    private final JPanel cardPanel = new JPanel();

    //    final TestDataAccessObject testDataAccessObject = new TestDataAccessObject();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private DashboardView dashboardView;
    private StudySessionConfigView studySessionConfigView;
    private StudySessionView studySessionView;
    private StudySessionEndView studySessionEndView;
    private StudySessionConfigViewModel studySessionConfigViewModel;
    private StudySessionViewModel studySessionViewModel;
    private StudySessionEndViewModel studySessionEndViewModel;
    private InitialView initialView;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private InitialViewModel initialViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addInitialView() {
        // Create the InitialViewModel and InitialView
        initialViewModel = new InitialViewModel();
        initialView = new InitialView(initialViewModel);

        // Add the InitialView to the CardLayout
        cardPanel.add(initialView, initialView.getViewName());

        // Attach ActionListeners for the buttons
        initialView.addLoginButtonListener(e -> {
            // Switch to LoginView
            if (loginView != null) {
                viewManagerModel.setState(loginView.getViewName());
                viewManagerModel.firePropertyChange();
            }
        });

        initialView.addSignupButtonListener(e -> {
            // Switch to SignupView
            if (signupView != null) {
                viewManagerModel.setState(signupView.getViewName());
                viewManagerModel.firePropertyChange();
            }
        });

        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addDashboardView() {
        dashboardView = new DashboardView();

        cardPanel.add(dashboardView, dashboardView.getViewName());

        return this;
    }

    public AppBuilder addSettingsView() {
        SettingsViewModel settingsViewModel = new SettingsViewModel();
        SettingsView settingsView = new SettingsView(settingsViewModel);

        cardPanel.add(settingsView, settingsView.getViewName());

        return this;
    }

    public AppBuilder addStudySessionView() {
        studySessionViewModel = new StudySessionViewModel();
        studySessionView = new StudySessionView(studySessionViewModel);

        cardPanel.add(studySessionView, studySessionView.getViewName());
        return this;
    }

    public AppBuilder addStudySessionConfigView() {
        studySessionConfigViewModel = new StudySessionConfigViewModel();
        studySessionConfigView = new StudySessionConfigView(studySessionConfigViewModel);
        cardPanel.add(studySessionConfigView, studySessionConfigView.getViewName());
        return this;
    }

    public AppBuilder addConfigStudySessionUseCase() {
        StartStudySessionPresenter startStudySessionPresenter = new StartStudySessionPresenter(
                studySessionConfigViewModel,
                studySessionViewModel,
                viewManagerModel,
                dashboardView.getViewName()
        );
        StartStudySessionInteractor configStudySessionInteractor = new StartStudySessionInteractor(startStudySessionPresenter);
        StartStudySessionController studySessionConfigController = new StartStudySessionController(configStudySessionInteractor);
        studySessionConfigView.setStartStudySessionController(studySessionConfigController);
        return this;
    }

    public AppBuilder addUploadSessionMaterialsView() {
        UploadSessionMaterialsView uploadSessionMaterialsView = new UploadSessionMaterialsView();
        cardPanel.add(uploadSessionMaterialsView, uploadSessionMaterialsView.getViewName());

        return this;
    }

    public AppBuilder addVariableSessionView() {
        VariableSessionView variableSessionView = new VariableSessionView();
        cardPanel.add(variableSessionView, variableSessionView.getViewName());
        return this;
    }

    public AppBuilder addStudySessionEndView() {
        studySessionEndViewModel = new StudySessionEndViewModel();
        studySessionEndView = new StudySessionEndView(studySessionEndViewModel);
        cardPanel.add(studySessionEndView, studySessionEndView.getViewName());
        return this;
    }

    public AppBuilder addEndStudySessionUseCase() {
        EndStudySessionPresenter endStudySessionPresenter = new EndStudySessionPresenter(
                studySessionViewModel,
                studySessionEndViewModel,
                viewManagerModel
        );
        EndStudySessionInteractor endStudySessionInteractor = new EndStudySessionInteractor(endStudySessionPresenter);
        EndStudySessionController endStudySessionController = new EndStudySessionController(endStudySessionInteractor);
        studySessionView.addEndStudySessionController(endStudySessionController);
        return this;
    }

    public AppBuilder addStudyQuizView() {
        StudyQuizView studyQuizView = new StudyQuizView();
        cardPanel.add(studyQuizView, studyQuizView.getViewName());
        return this;
    }

    public AppBuilder addFileManagerView() {
        FileManagerView fileManagerView = new FileManagerView();
        cardPanel.add(fileManagerView, fileManagerView.getViewName());
        return this;
    }

    public AppBuilder addQuizHistoryView() {
        QuizHistoryView quizHistoryView = new QuizHistoryView();
        cardPanel.add(quizHistoryView, quizHistoryView.getViewName());
        return this;
    }

    public AppBuilder addScheduleSessionView() {
        ScheduleSessionView scheduleSessionView = new ScheduleSessionView();
        cardPanel.add(scheduleSessionView, scheduleSessionView.getViewName());
        return this;
    }

    public AppBuilder addStudyMetricsView() {
        MetricsViewModel metricsViewModel = new MetricsViewModel();
        ViewStudyMetricsPresenter presenter = new ViewStudyMetricsPresenter(metricsViewModel);

        // TODO: Replace with actual DAO implementations
//        StudySessionRepository sessionRepository = new StudySessionRepository();
//        StudyQuizRepository quizRepository = new StudyQuizRepository();

        // Create Interactor
        ViewStudyMetricsInteractor interactor = new ViewStudyMetricsInteractor(
                presenter
        );

        // Create Controller
        ViewStudyMetricsController controller = new ViewStudyMetricsController(interactor);

        // Create View with ViewModel and Controller
        StudyMetricsView studyMetricsView = new StudyMetricsView(metricsViewModel, controller);

        cardPanel.add(studyMetricsView, studyMetricsView.getViewName());
        return this;
    }

    public AppBuilder addChooseStudySessionView() {
        ChooseStudySessionView chooseStudySessionView = new ChooseStudySessionView();
        cardPanel.add(chooseStudySessionView, chooseStudySessionView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame app = new JFrame(appTitle);
        app.setSize(800, 600);
        app.setLayout(new BorderLayout());

        cardPanel.setPreferredSize(new Dimension(800, 600));

        app.add(cardPanel, BorderLayout.CENTER);

        // Default view to dashboard view
        viewManagerModel.setView(dashboardView.getViewName());

        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setVisible(true);

        return app;
    }
}

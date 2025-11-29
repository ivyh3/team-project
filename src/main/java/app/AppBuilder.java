package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import entity.StudyQuizFactory;
import entity.StudySessionFactory;
import entity.UserFactory;
import frameworks_drivers.firebase.FirebaseFileDataAccessObject;
import frameworks_drivers.firebase.FirebaseMetricsDataAccessObject;
import frameworks_drivers.firebase.FirebaseStudyQuizDataAccessObject;
import frameworks_drivers.firebase.FirebaseStudySessionDataAccessObject;
import frameworks_drivers.firebase.FirebaseUserDataAccessObject;
import interface_adapter.controller.EndStudySessionController;
import interface_adapter.controller.LoginController;
import interface_adapter.controller.SignupController;
import interface_adapter.controller.StartStudySessionController;
import interface_adapter.controller.ViewStudyMetricsController;
import interface_adapter.presenter.EndStudySessionPresenter;
import interface_adapter.presenter.LoginPresenter;
import interface_adapter.presenter.SignupPresenter;
import interface_adapter.presenter.StartStudySessionPresenter;
import interface_adapter.presenter.ViewStudyMetricsPresenter;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.LoginViewModel;
import interface_adapter.view_model.MetricsViewModel;
import interface_adapter.view_model.SettingsViewModel;
import interface_adapter.view_model.SignupViewModel;
import interface_adapter.view_model.StudySessionConfigViewModel;
import interface_adapter.view_model.StudySessionEndViewModel;
import interface_adapter.view_model.StudySessionViewModel;
import interface_adapter.view_model.ViewManagerModel;
import use_case.end_study_session.EndStudySessionInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.start_study_session.StartStudySessionInteractor;
import use_case.view_study_metrics.ViewStudyMetricsDataAccessInterface;
import use_case.view_study_metrics.ViewStudyMetricsInteractor;
import view.DashboardView;
import view.FileManagerView;
import view.InitialView;
import view.LoginView;
import view.QuizHistoryView;
import view.ScheduleSessionView;
import view.SettingsView;
import view.SignupView;
import view.StudyMetricsView;
import view.StudyQuizView;
import view.StudySessionConfigView;
import view.StudySessionEndView;
import view.StudySessionView;
import view.UploadMaterialsView;
import view.UploadSessionMaterialsView;
import view.ViewManager;

public class AppBuilder {
    public static final ViewManagerModel viewManagerModel = new ViewManagerModel();
    // TODO: Sort things out.
    final UserFactory userFactory = new UserFactory();
    final StudySessionFactory studySessionFactory = new StudySessionFactory();
    final StudyQuizFactory studyQuizFactory = new StudyQuizFactory();
    final FirebaseStudySessionDataAccessObject studySessionDataAccessObject = new FirebaseStudySessionDataAccessObject(
        studySessionFactory);
    final FirebaseStudyQuizDataAccessObject quizDataAccessObject = new FirebaseStudyQuizDataAccessObject(
        studyQuizFactory);
    final FirebaseUserDataAccessObject userDataAccessObject = new FirebaseUserDataAccessObject(
        userFactory);
    final FirebaseFileDataAccessObject fileDataAccessObject = new FirebaseFileDataAccessObject();
    private final String APP_TITLE = "AI Study Companion";
    private final JPanel cardPanel = new JPanel();
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
    private DashboardViewModel dashboardViewModel;
    private LoginView loginView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addInitialView() {
        // Create the InitialView (no ViewModel needed - it has no state)
        initialView = new InitialView();

        // Add the InitialView to the CardLayout
        cardPanel.add(initialView, initialView.getViewName());

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

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
            signupViewModel, dashboardViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
            userDataAccessObject, signupOutputBoundary);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
            dashboardViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
            userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    // public AppBuilder addChangePasswordUseCase() {
    // final ChangePasswordOutputBoundary changePasswordOutputBoundary = new
    // ChangePasswordPresenter(viewManagerModel,
    // dashboardViewModel);

    // final ChangePasswordInputBoundary changePasswordInteractor = new
    // ChangePasswordInteractor(userDataAccessObject,
    // changePasswordOutputBoundary, userFactory);

    // ChangePasswordController changePasswordController = new
    // ChangePasswordController(changePasswordInteractor);
    // dashboardView.setChangePasswordController(changePasswordController);
    // return this;
    // }

    /**
     * Adds the Logout Use Case to the application.
     *
     * @return this builder
     */
    // public AppBuilder addLogoutUseCase() {
    // final LogoutOutputBoundary logoutOutputBoundary = new
    // LogoutPresenter(viewManagerModel,
    // dashboardViewModel, loginViewModel);

    // final LogoutInputBoundary logoutInteractor = new
    // LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

    // final LogoutController logoutController = new
    // LogoutController(logoutInteractor);
    // dashboardView.setLogoutController(logoutController);
    // return this;
    // }
    public AppBuilder addDashboardView() {
        dashboardViewModel = new DashboardViewModel();
        dashboardView = new DashboardView(dashboardViewModel);

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
            dashboardView.getViewName());
        StartStudySessionInteractor configStudySessionInteractor = new StartStudySessionInteractor(
            startStudySessionPresenter, fileDataAccessObject);
        StartStudySessionController studySessionConfigController = new StartStudySessionController(
            configStudySessionInteractor);
        studySessionConfigView.setStartStudySessionController(studySessionConfigController);
        return this;
    }

    public AppBuilder addUploadSessionMaterialsView() {
        UploadSessionMaterialsView uploadSessionMaterialsView = new UploadSessionMaterialsView();
        cardPanel.add(uploadSessionMaterialsView, uploadSessionMaterialsView.getViewName());

        return this;
    }

    public AppBuilder addUploadMaterialsView() {
        UploadMaterialsView uploadMaterialsView = new UploadMaterialsView();
        cardPanel.add(uploadMaterialsView, uploadMaterialsView.getViewName());

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
            viewManagerModel);
        EndStudySessionInteractor endStudySessionInteractor = new EndStudySessionInteractor(endStudySessionPresenter,
            studySessionDataAccessObject,
            studySessionFactory);
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
        ViewStudyMetricsDataAccessInterface metricsDAO = new FirebaseMetricsDataAccessObject(
            studySessionDataAccessObject, quizDataAccessObject);
        // Create Interactor
        ViewStudyMetricsInteractor interactor = new ViewStudyMetricsInteractor(
            metricsDAO, presenter);

        ViewStudyMetricsController controller = new ViewStudyMetricsController(interactor);
        StudyMetricsView studyMetricsView = new StudyMetricsView(metricsViewModel, controller);

        cardPanel.add(studyMetricsView, studyMetricsView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame app = new JFrame(APP_TITLE);
        app.setSize(800, 600);
        app.setLayout(new BorderLayout());

        cardPanel.setPreferredSize(new Dimension(800, 600));

        app.add(cardPanel, BorderLayout.CENTER);

        // Default view to dashboard view
        viewManagerModel.setView(initialView.getViewName());

        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setVisible(true);

        return app;
    }
}

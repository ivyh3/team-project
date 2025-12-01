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
import frameworks_drivers.firebase.*;
import frameworks_drivers.gemini.GeminiDataAccessObject;
import interface_adapter.controller.*;
import interface_adapter.presenter.*;
import interface_adapter.view_model.*;
import repository.QuestionDataAccess;
import use_case.end_study_session.EndStudySessionInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.start_study_session.StartStudySessionInteractor;
import use_case.upload_reference_material.UploadReferenceMaterialInteractor;
import use_case.view_study_metrics.ViewStudyMetricsDataAccessInterface;
import use_case.view_study_metrics.ViewStudyMetricsInteractor;
import view.ChooseStudySessionView;
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
import view.VariableSessionView;
import view.ViewManager;

// import interface_adapter.repository.StudySessionRepository;
// import interface_adapter.repository.StudyQuizRepository;

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
    final GeminiDataAccessObject geminiDataAccessObject = new GeminiDataAccessObject();
    private final String APP_TITLE = "AI Study Companion";
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private DashboardView dashboardView;
    private StudySessionConfigView studySessionConfigView;
    private StudySessionView studySessionView;
    private StudySessionEndView studySessionEndView;
    public StudyQuizView studyQuizView;
    private UploadMaterialsView uploadMaterialsView;
    private StudySessionConfigViewModel studySessionConfigViewModel;
    private StudySessionViewModel studySessionViewModel;
    private StudySessionEndViewModel studySessionEndViewModel;
    public QuizViewModel quizViewModel;
    private UploadMaterialsViewModel uploadMaterialsViewModel;
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
        StudyQuizFactory factory = new StudyQuizFactory(); // or however you create it
        FirebaseStudyQuizDataAccessObject dao = new FirebaseStudyQuizDataAccessObject(factory);
        QuestionDataAccess questionDAO = new FirebaseQuestionDataAccessAdapter(dao);

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

        // NOTE: StartStudySessionInteractor currently expects:
        //    StartStudySessionInteractor(StartStudySessionOutputBoundary, StartStudySessionDataAccessInterface)
        // so pass exactly two arguments — presenter + the DAO implementing StartStudySessionDataAccessInterface.
        StartStudySessionInteractor configStudySessionInteractor =
                new StartStudySessionInteractor(startStudySessionPresenter, fileDataAccessObject);

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

    public AppBuilder addVariableSessionView() {
        VariableSessionView variableSessionView = new VariableSessionView();
        cardPanel.add(variableSessionView, variableSessionView.getViewName());
        return this;
    }

    public AppBuilder addStudySessionEndView() {
        studySessionEndViewModel = new StudySessionEndViewModel();
        studySessionEndView = new StudySessionEndView(studySessionEndViewModel);
        studySessionEndView.setQuizDependencies(
                new QuizViewModel(new GeminiClient(Config.getGeminiApiKey(), "gemini-2.5-flash"))
        );
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
        quizViewModel = new QuizViewModel(
                new GeminiClient(Config.getGeminiApiKey(), "gemini-2.5-flash")
        );
        studyQuizView = new StudyQuizView(quizViewModel);
        cardPanel.add(studyQuizView, "studyQuiz"); // simple string key
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

    public AppBuilder addChooseStudySessionView() {
        ChooseStudySessionView chooseStudySessionView = new ChooseStudySessionView();
        cardPanel.add(chooseStudySessionView, chooseStudySessionView.getViewName());
        return this;
    }

    public AppBuilder addUploadMaterialsView() {
        UploadMaterialsViewModel uploadVM = new UploadMaterialsViewModel();
        DashboardViewModel dashboardVM = new DashboardViewModel();
        UploadMaterialsView uploadView = new UploadMaterialsView(dashboardVM, uploadVM);

        UploadMaterialsPresenter presenter = new UploadMaterialsPresenter(uploadVM, dashboardVM, viewManagerModel);

        UploadReferenceMaterialInteractor uploadReferenceMaterialInteractor = new UploadReferenceMaterialInteractor(fileDataAccessObject, presenter);
        UploadReferenceMaterialController controller = new UploadReferenceMaterialController(uploadReferenceMaterialInteractor);

        // Wire the controller to the view
        uploadView.setUploadController(controller);

        cardPanel.add(uploadView, uploadView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame app = new JFrame(APP_TITLE);
        app.setSize(800, 600);
        app.setLayout(new BorderLayout());

        cardPanel.setPreferredSize(new Dimension(800, 600));
        app.add(cardPanel, BorderLayout.CENTER);

        // App should start at welcome page
        viewManagerModel.setView(initialView.getViewName());

        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setVisible(true);
        return app;
    }
}

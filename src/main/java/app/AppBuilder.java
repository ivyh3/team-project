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
import entity.ScheduledSession;
import entity.ScheduledSessionFactory;
import frameworks_drivers.firebase.FirebaseFileDataAccessObject;
import frameworks_drivers.firebase.FirebaseMetricsDataAccessObject;
import frameworks_drivers.firebase.FirebaseScheduledSessionDataAccessObject;
import frameworks_drivers.firebase.FirebaseStudyQuizDataAccessObject;
import frameworks_drivers.firebase.FirebaseStudySessionDataAccessObject;
import frameworks_drivers.firebase.FirebaseUserDataAccessObject;
import interface_adapter.controller.*;
import interface_adapter.presenter.*;
import interface_adapter.view_model.*;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.delete_reference_material.DeleteReferenceMaterialInputBoundary;
import use_case.delete_reference_material.DeleteReferenceMaterialInteractor;
import use_case.delete_reference_material.DeleteReferenceMaterialOutputBoundary;
import use_case.end_study_session.EndStudySessionInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionDataAccessInterface;
import use_case.schedule_study_session.ScheduleStudySessionInteractor;
import use_case.schedule_study_session.ScheduleStudySessionInputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.start_study_session.StartStudySessionInteractor;
import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;
import use_case.upload_reference_material.UploadReferenceMaterialInputBoundary;
import use_case.upload_reference_material.UploadReferenceMaterialInteractor;
import use_case.upload_reference_material.UploadReferenceMaterialOutputBoundary;
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
    final ScheduledSessionFactory scheduledSessionFactory = new ScheduledSessionFactory();
    final FirebaseScheduledSessionDataAccessObject scheduledSessionDataAccessObject = new FirebaseScheduledSessionDataAccessObject(
            scheduledSessionFactory);
    private final String APP_TITLE = "AI Study Companion";
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private DashboardView dashboardView;
    private QuizViewModel quizViewModel;
    private StudySessionConfigView studySessionConfigView;
    private StudySessionView studySessionView;
    private StudySessionEndView studySessionEndView;
    private StudySessionConfigViewModel studySessionConfigViewModel;
    private StudySessionViewModel studySessionViewModel;
    private StudySessionEndViewModel studySessionEndViewModel;
    private UploadMaterialsView uploadMaterialsView;
    private UploadSessionMaterialsView uploadSessionMaterialsView;
    private UploadMaterialsViewModel uploadMaterialsViewModel;
    private InitialView initialView;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private DashboardViewModel dashboardViewModel;
    private LoginView loginView;
    private ScheduleSessionView scheduleSessionView;

    private SettingsView settingsView;
    private SettingsViewModel settingsViewModel;

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

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                dashboardViewModel, settingsViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor = new ChangePasswordInteractor(userDataAccessObject,
                changePasswordOutputBoundary);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        settingsView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                dashboardViewModel);

        final LogoutInputBoundary logoutInteractor = new LogoutInteractor(logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        settingsView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addDashboardView() {
        dashboardViewModel = new DashboardViewModel();
        dashboardView = new DashboardView(dashboardViewModel);

        cardPanel.add(dashboardView, dashboardView.getViewName());

        return this;
    }

    public AppBuilder addSettingsView() {
        // 1. Create the ViewModel instances
        settingsViewModel = new SettingsViewModel(); // assign to field
        dashboardViewModel = new DashboardViewModel(); // assign to field

        // 2. Create your presenter for uploading
        String currentUsername = dashboardViewModel.getState().getEmail(); // or a fallback ""
        uploadMaterialsViewModel = new UploadMaterialsViewModel(currentUsername);
        UploadReferenceMaterialOutputBoundary uploadPresenter =
                new UploadMaterialsPresenter(uploadMaterialsViewModel);

        // 3. Use your Firebase DAO as data access
        UploadReferenceMaterialDataAccessInterface dataAccess = fileDataAccessObject;

        // 4. Create the interactor
        UploadReferenceMaterialInputBoundary uploadInteractor =
                new UploadReferenceMaterialInteractor(dataAccess, uploadPresenter);

        // 5. Use the existing ViewManager (already a field)

        // 6. Create the controller
        UploadReferenceMaterialController uploadController =
                new UploadReferenceMaterialController(uploadInteractor, dataAccess, viewManager);

        // 7. Assign to class field, not local variable
        settingsView = new SettingsView(settingsViewModel, dashboardViewModel, viewManagerModel);
        settingsView.setUploadController(uploadController);

        // 8. Add the view to your CardLayout
        cardPanel.add(settingsView, settingsView.getViewName());
        return this;
    }

    public AppBuilder addStudySessionView() {
        studySessionViewModel = new StudySessionViewModel();
        studySessionView = new StudySessionView(studySessionViewModel, dashboardViewModel);

        cardPanel.add(studySessionView, studySessionView.getViewName());
        return this;
    }

    public AppBuilder addStudySessionConfigView() {
        studySessionConfigViewModel = new StudySessionConfigViewModel();

        // Create controller for this view
        StartStudySessionInteractor configStudySessionInteractor = new StartStudySessionInteractor(
                new StartStudySessionPresenter(studySessionConfigViewModel, studySessionViewModel, viewManagerModel, dashboardView.getViewName()),
                fileDataAccessObject
        );
        StartStudySessionController studySessionConfigController = new StartStudySessionController(configStudySessionInteractor);

        // Pass controller to constructor
        studySessionConfigView = new StudySessionConfigView(
                studySessionConfigViewModel,
                dashboardViewModel,
                studySessionConfigController
        );

        cardPanel.add(studySessionConfigView, studySessionConfigView.getViewName());
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
        StudyQuizView studyQuizView = new StudyQuizView(quizViewModel);
        cardPanel.add(studyQuizView, "STUDY_QUIZ");
        return this;
    }

    public AppBuilder addFileManagerView() {
        FileManagerView fileManagerView = new FileManagerView();
        cardPanel.add(fileManagerView, fileManagerView.getViewName());
        return this;
    }

    public AppBuilder addUploadMaterialsView() {
        // 1. Initialize ViewModel
        uploadMaterialsViewModel = new UploadMaterialsViewModel(dashboardViewModel.getState().getEmail());

        // 2. Create the presenter and interactor for uploading
        UploadReferenceMaterialOutputBoundary uploadPresenter =
                new UploadMaterialsPresenter(uploadMaterialsViewModel);
        UploadReferenceMaterialInputBoundary uploadInteractor =
                new UploadReferenceMaterialInteractor(fileDataAccessObject, uploadPresenter);

        // 3. Create and attach controller
        UploadReferenceMaterialController uploadController =
                new UploadReferenceMaterialController(uploadInteractor, fileDataAccessObject, viewManager);

        // 4. Create the UploadMaterialsView with ViewModel + controller
        uploadMaterialsView = new UploadMaterialsView(uploadMaterialsViewModel, uploadController);

        // 5. Add the view to the card panel
        cardPanel.add(uploadMaterialsView, uploadMaterialsView.getViewName());

        return this;
    }

    public AppBuilder addQuizHistoryView() {
        QuizHistoryView quizHistoryView = new QuizHistoryView();
        cardPanel.add(quizHistoryView, quizHistoryView.getViewName());
        return this;
    }

    public AppBuilder addScheduleSessionView() {
        ScheduleSessionViewModel scheduleViewModel = new ScheduleSessionViewModel();
        ScheduleStudySessionOutputBoundary presenter = new ScheduleStudySessionPresenter(scheduleViewModel);
        ScheduleStudySessionDataAccessInterface dataAccess = scheduledSessionDataAccessObject;
        ScheduleStudySessionInputBoundary interactor = new ScheduleStudySessionInteractor(dataAccess, presenter);
        ScheduleStudySessionController controller = new ScheduleStudySessionController(interactor);
        scheduleSessionView = new ScheduleSessionView(controller, scheduleViewModel, dashboardViewModel);
        cardPanel.add(scheduleSessionView, scheduleSessionView.getViewName());
        return this;
    }

    public AppBuilder addStudyMetricsView() {
        MetricsViewModel metricsViewModel = new MetricsViewModel();
        ViewStudyMetricsPresenter presenter = new ViewStudyMetricsPresenter(metricsViewModel);

        ViewStudyMetricsDataAccessInterface metricsDAO = new FirebaseMetricsDataAccessObject(
            studySessionDataAccessObject, quizDataAccessObject);
        ViewStudyMetricsInteractor interactor = new ViewStudyMetricsInteractor(metricsDAO, presenter);

        ViewStudyMetricsController controller = new ViewStudyMetricsController(interactor);
        StudyMetricsView studyMetricsView = new StudyMetricsView(metricsViewModel, controller, dashboardViewModel);

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

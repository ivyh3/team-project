package app;


import interface_adapter.controller.EndStudySessionController;
import interface_adapter.controller.StudySessionConfigController;
import interface_adapter.presenter.ConfigStudySessionPresenter;
import interface_adapter.presenter.EndStudySessionPresenter;
import interface_adapter.view_model.*;
import use_case.config_study_session.ConfigStudySessionInteractor;
import use_case.end_study_session.EndStudySessionInteractor;
import view.*;
import view.SettingsView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final String appTitle = "My title";

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    public static final ViewManagerModel viewManagerModel = new ViewManagerModel();
    final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

//    final TestDataAccessObject testDataAccessObject = new TestDataAccessObject();

    private DashboardView dashboardView;
    private StudySessionConfigView studySessionConfigView;
    private StudySessionView studySessionView;
    private StudySessionEndView studySessionEndView;

    private StudySessionConfigViewModel studySessionConfigViewModel;
    private StudySessionViewModel studySessionViewModel;
    private StudySessionEndViewModel studySessionEndViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
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
        ConfigStudySessionPresenter configStudySessionPresenter = new ConfigStudySessionPresenter(studySessionConfigViewModel, studySessionViewModel);
        ConfigStudySessionInteractor configStudySessionInteractor = new ConfigStudySessionInteractor(configStudySessionPresenter);
        StudySessionConfigController studySessionConfigController = new StudySessionConfigController(configStudySessionInteractor);
        studySessionConfigView.setStudySessionConfigController(studySessionConfigController);
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
        EndStudySessionPresenter endStudySessionPresenter = new EndStudySessionPresenter(studySessionViewModel, studySessionEndViewModel);
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

    public AppBuilder addUploadMaterialsView() {
        FileManagerView fileManagerView = new FileManagerView();
        cardPanel.add(fileManagerView, fileManagerView.getViewName());

        UploadMaterialsView uploadMaterialsView = new UploadMaterialsView();
        cardPanel.add(uploadMaterialsView, uploadMaterialsView.getViewName());
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
        StudyMetricsView studyMetricsView = new StudyMetricsView();
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
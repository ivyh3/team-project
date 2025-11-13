package app;

import frameworks_drivers.firebase.*;
import frameworks_drivers.gemini.GeminiService;
import frameworks_drivers.google_calendar.GoogleCalendarService;
import frameworks_drivers.google_calendar.OAuthService;
import frameworks_drivers.storage.StorageService;
import interface_adapter.controller.*;
import interface_adapter.presenter.*;
import interface_adapter.view_model.*;
import use_case.account_creation.AccountCreationInteractor;
import use_case.generate_quiz.GenerateQuizInteractor;
import use_case.schedule_study_session.ScheduleStudySessionInteractor;
import use_case.start_study_session.StartStudySessionInteractor;
import use_case.view_study_metrics.ViewStudyMetricsInteractor;

/**
 * Configuration class for dependency injection (Main Component).
 * Sets up all the services, repositories, ViewModels, presenters, interactors,
 * and controllers.
 * This is where we assemble the Clean Architecture engine.
 */
public class AppConfig {

	// Services (Frameworks & Drivers layer)
	private FirebaseAuthService authService;
	private FirestoreService firestoreService;
	private StorageService storageService;
	private GeminiService geminiService;
	private GoogleCalendarService calendarService;
	private OAuthService oauthService;

	// Repositories (Frameworks & Drivers layer - implements interfaces from
	// Interface Adapter layer)
	private FirebaseUserRepositoryImpl userRepository;
	// TODO: Add other repository implementations
	// private FirebaseStudySessionRepositoryImpl sessionRepository;
	// private FirebaseStudyQuizRepositoryImpl quizRepository;
	// etc.

	// ViewModels (Interface Adapter layer)
	private LoginViewModel loginViewModel;
	private StudySessionViewModel studySessionViewModel;
	private QuizViewModel quizViewModel;
	private MetricsViewModel metricsViewModel;
	private UploadMaterialsViewModel uploadMaterialsViewModel;
	private ScheduleSessionViewModel scheduleSessionViewModel;

	// Presenters (Interface Adapter layer - implement Output Boundaries)
	private AccountCreationPresenter accountCreationPresenter;
	private StartStudySessionPresenter startStudySessionPresenter;
	private GenerateQuizPresenter generateQuizPresenter;
	private ScheduleStudySessionPresenter scheduleStudySessionPresenter;
	private ViewStudyMetricsPresenter viewStudyMetricsPresenter;

	// Interactors (Use Case layer - implement Input Boundaries)
	private AccountCreationInteractor accountCreationInteractor;
	private StartStudySessionInteractor startStudySessionInteractor;
	private GenerateQuizInteractor generateQuizInteractor;
	private ScheduleStudySessionInteractor scheduleStudySessionInteractor;
	private ViewStudyMetricsInteractor viewStudyMetricsInteractor;
	// TODO: Add other interactors

	// Controllers (Interface Adapter layer)
	private AccountCreationController accountCreationController;
	private StartStudySessionController startStudySessionController;
	private GenerateQuizController generateQuizController;
	private ScheduleStudySessionController scheduleStudySessionController;
	private ViewStudyMetricsController viewStudyMetricsController;
	// TODO: Add other controllers

	public AppConfig() {
		initializeServices();
		initializeRepositories();
		initializeViewModels();
		initializeUseCases();
	}

	private void initializeServices() {
		// Initialize all framework services (outermost layer)
		authService = new FirebaseAuthService();
		firestoreService = new FirestoreService();
		storageService = new StorageService();
		geminiService = new GeminiService();
		calendarService = new GoogleCalendarService();
		oauthService = new OAuthService();
	}

	private void initializeRepositories() {
		// Initialize all repositories with service dependencies
		userRepository = new FirebaseUserRepositoryImpl(firestoreService);
		// TODO: Create and initialize other repository implementations
		// sessionRepository = new FirebaseStudySessionRepositoryImpl(firestoreService);
		// quizRepository = new FirebaseStudyQuizRepositoryImpl(firestoreService);
		// materialRepository = new
		// FirebaseReferenceMaterialRepositoryImpl(firestoreService, storageService);
		// courseRepository = new FirebaseCourseRepositoryImpl(firestoreService);
	}

	private void initializeViewModels() {
		// Initialize all ViewModels (these hold the state for the Views)
		loginViewModel = new LoginViewModel();
		studySessionViewModel = new StudySessionViewModel();
		quizViewModel = new QuizViewModel();
		metricsViewModel = new MetricsViewModel();
		uploadMaterialsViewModel = new UploadMaterialsViewModel();
		scheduleSessionViewModel = new ScheduleSessionViewModel();
	}

	private void initializeUseCases() {
		// Initialize Presenters (Output Boundaries) with ViewModels
		accountCreationPresenter = new AccountCreationPresenter(loginViewModel);
		startStudySessionPresenter = new StartStudySessionPresenter(studySessionViewModel);
		generateQuizPresenter = new GenerateQuizPresenter(quizViewModel);
		scheduleStudySessionPresenter = new ScheduleStudySessionPresenter(scheduleSessionViewModel);
		viewStudyMetricsPresenter = new ViewStudyMetricsPresenter(metricsViewModel);

		// Initialize Interactors (Input Boundaries) with Repositories and Presenters
		// TODO: Complete these once repository implementations are created
		// accountCreationInteractor = new AccountCreationInteractor(
		// userRepository, authService, accountCreationPresenter
		// );
		// startStudySessionInteractor = new StartStudySessionInteractor(
		// sessionRepository, startStudySessionPresenter
		// );
		// generateQuizInteractor = new GenerateQuizInteractor(
		// quizRepository, geminiService, generateQuizPresenter
		// );
		// scheduleStudySessionInteractor = new ScheduleStudySessionInteractor(
		// sessionRepository, calendarService, scheduleStudySessionPresenter
		// );
		// viewStudyMetricsInteractor = new ViewStudyMetricsInteractor(
		// sessionRepository, quizRepository, viewStudyMetricsPresenter
		// );

		// Initialize Controllers with Interactors
		// TODO: Complete these once interactors are initialized
		// accountCreationController = new
		// AccountCreationController(accountCreationInteractor);
		// startStudySessionController = new
		// StartStudySessionController(startStudySessionInteractor);
		// generateQuizController = new GenerateQuizController(generateQuizInteractor);
		// scheduleStudySessionController = new
		// ScheduleStudySessionController(scheduleStudySessionInteractor);
		// viewStudyMetricsController = new
		// ViewStudyMetricsController(viewStudyMetricsInteractor);
	}

	// Getters to provide access to ViewModels and Controllers for Views

	public LoginViewModel getLoginViewModel() {
		return loginViewModel;
	}

	public StudySessionViewModel getStudySessionViewModel() {
		return studySessionViewModel;
	}

	public QuizViewModel getQuizViewModel() {
		return quizViewModel;
	}

	public MetricsViewModel getMetricsViewModel() {
		return metricsViewModel;
	}

	public UploadMaterialsViewModel getUploadMaterialsViewModel() {
		return uploadMaterialsViewModel;
	}

	public ScheduleSessionViewModel getScheduleSessionViewModel() {
		return scheduleSessionViewModel;
	}

	public AccountCreationController getAccountCreationController() {
		return accountCreationController;
	}

	public StartStudySessionController getStartStudySessionController() {
		return startStudySessionController;
	}

	// TODO: Add getters for other controllers
}

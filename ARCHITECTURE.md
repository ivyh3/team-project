# AI Study Companion - Clean Architecture

## Project Structure

This project follows **Clean Architecture** principles with clear separation of concerns across four layers:

### 1. Entities (Domain Layer)
Location: `src/main/java/entity/`

Core business entities representing the domain model:
- `User` - User account information
- `Course` - Course information
- `StudySession` - Study session data
- `StudyQuiz` - Quiz data and results
- `Question` - Individual quiz question
- `ReferenceMaterial` - Uploaded study materials metadata

### 2. Use Cases (Application Layer)
Location: `src/main/java/use_case/`

Business logic organized by use case:
- `start_study_session/` - Start a timed or untimed study session
- `generate_quiz/` - Generate AI-powered quiz after study session
- `upload_reference_material/` - Upload textbooks and study materials
- `delete_reference_material/` - Delete uploaded materials
- `review_quiz_history/` - View past quiz results
- `view_study_metrics/` - View study statistics and analytics
- `account_creation/` - Create account with email or Google
- `schedule_study_session/` - Schedule future study sessions
- `sync_google_calendar/` - Sync sessions with Google Calendar

Each use case contains:
- `*InputBoundary` - Interface for the interactor
- `*InputData` - Input data transfer object
- `*OutputBoundary` - Interface for the presenter
- `*OutputData` - Output data transfer object
- `*Interactor` - Business logic implementation

### 3. Interface Adapters (Interface Layer)
Location: `src/main/java/interface_adapter/`

Adapters that convert data between use cases and external systems:

#### Repositories
Define interfaces for data persistence:
- `UserRepository`
- `StudySessionRepository`
- `StudyQuizRepository`
- `ReferenceMaterialRepository`
- `CourseRepository`

#### Controllers
Handle user input and invoke use cases:
- `StartStudySessionController`
- `GenerateQuizController`
- `UploadReferenceMaterialController`
- `DeleteReferenceMaterialController`
- `AccountCreationController`
- `ScheduleStudySessionController`
- `ReviewQuizHistoryController`
- `ViewStudyMetricsController`

#### ViewModels
Store presentation state for the views (uses Observer pattern with PropertyChangeListener):
- `LoginViewModel`
- `StudySessionViewModel`
- `QuizViewModel`
- `MetricsViewModel`
- `UploadMaterialsViewModel`
- `ScheduleSessionViewModel`

#### Presenters
Format output data and update ViewModels:
- `StartStudySessionPresenter` (updates `StudySessionViewModel`)
- `GenerateQuizPresenter` (updates `QuizViewModel`)
- `AccountCreationPresenter` (updates `LoginViewModel`)
- `ScheduleStudySessionPresenter` (updates `ScheduleSessionViewModel`)
- `ViewStudyMetricsPresenter` (updates `MetricsViewModel`)

### 4. Frameworks & Drivers (Outer Layer)
Location: `src/main/java/frameworks_drivers/`

External frameworks and tools:

#### Firebase
- `FirebaseAuthService` - Authentication with email/password and Google OAuth
- `FirestoreService` - Cloud database operations
- `FirebaseUserRepositoryImpl` - Firestore implementation of UserRepository
- Additional repository implementations (to be created)

#### Storage
- `StorageService` - Firebase Storage for file upload/download

#### Gemini AI
- `GeminiService` - AI quiz generation using Gemini API

#### Google Calendar
- `GoogleCalendarService` - Calendar event creation and management
- `OAuthService` - OAuth 2.0 flow for Google services

### 5. View Layer (UI)
Location: `src/main/java/view/`

Java Swing UI components:
- `LoginView` - Login and sign-up screen
- `MainView` - Main application window
- `StudySessionView` - Start and manage study sessions
- `QuizView` - Take quizzes
- `MetricsView` - View study statistics
- `UploadMaterialsView` - Upload and manage reference materials
- `ScheduleSessionView` - Schedule future sessions

### 6. Application Entry Point
Location: `src/main/java/app/`

- `Main` - Application entry point
- `AppConfig` - Dependency injection configuration

## Data Flow (Following Clean Architecture from Course)

1. **User Input** → View captures user actions
2. **View** → Controller receives input and creates InputData
3. **Controller** → Interactor (via InputBoundary) receives InputData and executes business logic
4. **Interactor** → Repository/Service (via Data Access Interface) for data persistence or external API calls
5. **Interactor** → Entities to perform business logic
6. **Interactor** → Presenter (via OutputBoundary) with OutputData
7. **Presenter** → ViewModel updates its state with formatted data
8. **ViewModel** → View is notified (PropertyChangeListener) and updates UI

This matches the Clean Architecture pattern from CSC207 where:
- **Input Boundary** = Interface implemented by Use Case Interactor
- **Output Boundary** = Interface implemented by Presenter
- **ViewModel** = Storage class for information the View needs to display
- **Data Access Interface** = Repository interfaces

## Backend Services

### Firebase
- **Authentication**: Email/password and Google OAuth
- **Firestore**: Cloud NoSQL database
  - Collections: `/users/{uid}/`, `/users/{uid}/studySessions/`, `/users/{uid}/quizzes/`, etc.
- **Storage**: File storage for PDFs and textbooks

### Gemini API
- AI-powered quiz generation
- Document context for relevant questions
- Structured JSON output for reliable parsing

### Google Calendar API
- OAuth 2.0 authentication
- Create dedicated calendar for study sessions
- Sync scheduled sessions as calendar events

## Security Considerations

1. **API Keys**: Store in environment variables or secure config (not in source code)
2. **OAuth Tokens**: Encrypt refresh tokens before storing
3. **Firestore Rules**: Implement user-based access control
4. **Storage Rules**: Restrict file access to file owners

## Implementation TODO

Each class contains `TODO` comments indicating what needs to be implemented:
- Firebase SDK integration
- HTTP clients for Gemini API
- Google Calendar API integration
- UI layout and event handlers
- Data mapping between layers

## Dependencies

Add to `pom.xml`:
- Firebase Admin SDK
- Google API Client for Java
- HTTP client (e.g., OkHttp, Apache HttpClient)
- JSON parsing (e.g., Gson, Jackson)
- PDF text extraction library (e.g., Apache PDFBox)

## Running the Application

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="app.Main"
```

## Testing

Each layer should be tested independently:
- **Entities**: Unit tests for business logic
- **Use Cases**: Unit tests with mocked repositories
- **Repositories**: Integration tests with Firebase emulator
- **Controllers/Presenters**: Unit tests with mocked interactors
- **Views**: Manual testing and UI tests

## Team Responsibilities

- **David**: Use cases 8 & 9 (Schedule & Sync Calendar)
- **Sarah**: Use cases 3 & 4 (Upload & Delete Materials)
- **Tess**: Use case 7 (Account Creation)
- **Mia**: Use case 2 (Quiz Generation)
- **Ivy**: Use cases 5 & 6 (Quiz History & Metrics)
- **Freeman**: Use case 1 (Study Session)


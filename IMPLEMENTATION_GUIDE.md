# AI Study Companion - Implementation Guide

## Overview

This document provides a step-by-step guide for implementing the AI Study Companion application using Clean Architecture with Firebase, Gemini AI, and Google Calendar integration.

## Prerequisites

1. **Firebase Project Setup**
   - Create a Firebase project at https://console.firebase.google.com
   - Enable Authentication (Email/Password and Google Sign-In)
   - Enable Firestore Database
   - Enable Firebase Storage
   - Download service account key JSON file

2. **Google Cloud Setup**
   - Enable Google Calendar API
   - Create OAuth 2.0 credentials
   - Note Client ID and Client Secret

3. **Gemini API Setup**
   - Get API key from https://aistudio.google.com/app/apikey
   - Free tier: 30 RPM, 1M TPM, 200 RPD (Gemini 2.0 Flash-Lite)

## Dependencies to Add

Add these to `pom.xml`:

```xml
<dependencies>
    <!-- Firebase Admin SDK -->
    <dependency>
        <groupId>com.google.firebase</groupId>
        <artifactId>firebase-admin</artifactId>
        <version>9.2.0</version>
    </dependency>
    
    <!-- Google API Client -->
    <dependency>
        <groupId>com.google.api-client</groupId>
        <artifactId>google-api-client</artifactId>
        <version>2.2.0</version>
    </dependency>
    
    <!-- Google Calendar API -->
    <dependency>
        <groupId>com.google.apis</groupId>
        <artifactId>google-api-services-calendar</artifactId>
        <version>v3-rev20220715-2.0.0</version>
    </dependency>
    
    <!-- HTTP Client (OkHttp) -->
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.12.0</version>
    </dependency>
    
    <!-- JSON Parsing (Gson) -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- PDF Text Extraction -->
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>3.0.1</version>
    </dependency>
    
    <!-- JUnit for Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Mockito for Mocking -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.8.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Implementation Steps by Team Member

### Freeman - Use Case 1: Start Study Session

**Files to Implement:**
1. `use_case/start_study_session/StartStudySessionInteractor.java`
   - Create StudySession entity
   - Save to repository
   - Handle timer/stopwatch logic
   
2. `frameworks_drivers/firebase/FirebaseStudySessionRepositoryImpl.java` (create new)
   - Implement StudySessionRepository
   - Map entities to Firestore documents

3. `view/StudySessionView.java`
   - Complete UI layout
   - Add timer/stopwatch functionality
   - Wire up event listeners

**Key Considerations:**
- Store session start time immediately
- Update session status ("active", "completed", "cancelled")
- Calculate duration on session end

---

### Mia - Use Case 2: Generate Quiz

**Files to Implement:**
1. `use_case/generate_quiz/GenerateQuizInteractor.java`
   - Fetch reference materials
   - Call Gemini API
   - Parse and validate quiz JSON
   - Save quiz to repository

2. `frameworks_drivers/gemini/GeminiService.java`
   - Implement HTTP client for Gemini API
   - Handle structured output request
   - Implement rate limiting and retry logic
   - For files >20MB, use Gemini File API

3. `frameworks_drivers/firebase/FirebaseStudyQuizRepositoryImpl.java` (create new)
   - Implement StudyQuizRepository

4. `view/QuizView.java`
   - Complete quiz UI
   - Handle answer selection
   - Show explanations
   - Display score

**Gemini API Example:**

```java
public List<Question> generateQuiz(String prompt, List<String> materialTexts) {
    String contextText = String.join("\n\n", materialTexts);
    String fullPrompt = buildPrompt(prompt, contextText);
    
    // Build request
    JsonObject request = new JsonObject();
    // ... add prompt and configuration
    
    // Call API
    OkHttpClient client = new OkHttpClient();
    Request req = new Request.Builder()
        .url(GEMINI_API_URL + "?key=" + GEMINI_API_KEY)
        .post(RequestBody.create(request.toString(), MediaType.parse("application/json")))
        .build();
    
    Response response = client.newCall(req).execute();
    // Parse response JSON and map to Question entities
}
```

---

### Sarah - Use Cases 3 & 4: Upload & Delete Reference Materials

**Files to Implement:**
1. `use_case/upload_reference_material/UploadReferenceMaterialInteractor.java`
   - Calculate file fingerprint (SHA-256)
   - Check for duplicates
   - Upload to Firebase Storage
   - Save metadata to Firestore

2. `use_case/delete_reference_material/DeleteReferenceMaterialInteractor.java`
   - Confirm deletion with user
   - Delete from Storage
   - Delete from Firestore

3. `frameworks_drivers/storage/StorageService.java`
   - Implement file upload/download
   - Implement fingerprint calculation
   - Extract text from PDFs (using PDFBox)

4. `frameworks_drivers/firebase/FirebaseReferenceMaterialRepositoryImpl.java` (create new)
   - Implement ReferenceMaterialRepository

5. `view/UploadMaterialsView.java`
   - File chooser dialog
   - Upload progress indicator
   - Material list display

**File Upload Example:**

```java
public String uploadFile(String userId, File file, String filename) {
    try {
        // Initialize Firebase Storage
        Storage storage = StorageOptions.getDefaultInstance().getService();
        String blobPath = String.format("users/%s/materials/%s", userId, filename);
        BlobId blobId = BlobId.of("your-bucket-name", blobPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        
        // Upload file
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return blobPath;
    } catch (IOException e) {
        throw new RuntimeException("Upload failed", e);
    }
}
```

---

### Tess - Use Case 7: Account Creation

**Files to Implement:**
1. `use_case/account_creation/AccountCreationInteractor.java`
   - Validate email/password
   - Call Firebase Auth to create user
   - If Google OAuth, exchange token
   - Create User entity and save to Firestore

2. `frameworks_drivers/firebase/FirebaseAuthService.java`
   - Implement email/password sign-up and sign-in
   - Implement Google OAuth sign-in
   - Store and refresh ID tokens
   - Implement account linking

3. `view/LoginView.java`
   - Complete UI layout
   - Add form validation
   - Wire up login/signup buttons
   - Implement Google OAuth browser flow

**Firebase Auth Example:**

```java
public String createUserWithEmail(String email, String password) {
    try {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
            .setEmail(email)
            .setPassword(password);
        
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        return userRecord.getUid();
    } catch (FirebaseAuthException e) {
        throw new RuntimeException("User creation failed", e);
    }
}
```

---

### Ivy - Use Cases 5 & 6: Quiz History & Metrics

**Files to Implement:**
1. `use_case/review_quiz_history/ReviewQuizHistoryInteractor.java`
   - Query quizzes from repository
   - Filter by course if specified
   - Sort by date

2. `use_case/view_study_metrics/ViewStudyMetricsInteractor.java`
   - Fetch all sessions and quizzes
   - Calculate daily/weekly study time
   - Calculate average quiz scores
   - Determine strongest/weakest subjects

3. `view/MetricsView.java`
   - Complete charts (use JFreeChart or similar)
   - Display summary statistics
   - Implement filters

**Metrics Calculation Example:**

```java
public ViewStudyMetricsOutputData execute(ViewStudyMetricsInputData inputData) {
    List<StudySession> sessions = sessionRepository.findByUser(inputData.getUserId());
    List<StudyQuiz> quizzes = quizRepository.findByUser(inputData.getUserId());
    
    // Calculate daily study durations
    Map<String, Duration> dailyDurations = new HashMap<>();
    for (StudySession session : sessions) {
        String date = session.getStartTime().toLocalDate().toString();
        dailyDurations.merge(date, session.getDuration(), Duration::plus);
    }
    
    // Calculate average quiz scores by course
    Map<String, Float> avgScores = quizzes.stream()
        .collect(Collectors.groupingBy(
            StudyQuiz::getCourseId,
            Collectors.averagingDouble(StudyQuiz::getScore)
        ));
    
    // ... more calculations
    
    return new ViewStudyMetricsOutputData(dailyDurations, avgScores, ...);
}
```

---

### David - Use Cases 8 & 9: Schedule Session & Sync Calendar

**Files to Implement:**
1. `use_case/schedule_study_session/ScheduleStudySessionInteractor.java`
   - Validate start/end times
   - Create StudySession entity with future date
   - If syncCalendar, call calendar service
   - Save to repository

2. `use_case/sync_google_calendar/SyncGoogleCalendarInteractor.java`
   - Fetch session from repository
   - Create/update calendar event
   - Store event ID in session

3. `frameworks_drivers/google_calendar/GoogleCalendarService.java`
   - Implement OAuth flow
   - Create app calendar
   - Create/update/delete events
   - Store encrypted refresh tokens

4. `frameworks_drivers/google_calendar/OAuthService.java`
   - Generate authorization URL
   - Exchange code for tokens
   - Refresh access tokens

5. `view/ScheduleSessionView.java`
   - Date/time pickers
   - Sync checkbox
   - Display scheduled sessions

**Google Calendar API Example:**

```java
public String createEvent(String userId, String calendarId, StudySession session) {
    // Get user's refresh token from Firestore
    String refreshToken = getUserRefreshToken(userId);
    String accessToken = oauthService.refreshAccessToken(refreshToken);
    
    // Build calendar service
    Calendar service = new Calendar.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance(),
        request -> request.getHeaders().setAuthorization("Bearer " + accessToken)
    ).build();
    
    // Create event
    Event event = new Event()
        .setSummary("Study Session - " + session.getCourseId())
        .setStart(new EventDateTime().setDateTime(new DateTime(session.getStartTime())))
        .setEnd(new EventDateTime().setDateTime(new DateTime(session.getEndTime())));
    
    Event result = service.events().insert(calendarId, event).execute();
    return result.getId();
}
```

**OAuth Flow:**
1. User clicks "Connect Google Calendar"
2. Open browser to OAuth URL
3. User grants permissions
4. Redirect to localhost callback
5. Exchange code for tokens
6. Store encrypted refresh token in Firestore

---

## Configuration Files

### Create `src/main/resources/firebase-config.json`
Place your Firebase service account key here. **Do not commit to git!**

### Create `src/main/resources/application.properties`
```properties
# Firebase
firebase.project.id=your-project-id
firebase.storage.bucket=your-project-id.appspot.com

# Gemini API
gemini.api.key=your-gemini-api-key

# Google OAuth
google.oauth.client.id=your-client-id.apps.googleusercontent.com
google.oauth.client.secret=your-client-secret
google.oauth.redirect.uri=http://localhost:8080/oauth/callback
```

**Add to `.gitignore`:**
```
src/main/resources/firebase-config.json
src/main/resources/application.properties
target/
```

---

## Firestore Security Rules

Deploy these rules to Firebase Console:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // User can only access their own data
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Firebase Storage Security Rules

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/materials/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

---

## Testing Strategy

### Unit Tests
- Test entities with business logic
- Test interactors with mocked repositories
- Test controllers with mocked interactors

Example:
```java
@Test
public void testStartStudySession() {
    // Arrange
    StudySessionRepository mockRepo = mock(StudySessionRepository.class);
    StartStudySessionOutputBoundary mockPresenter = mock(StartStudySessionOutputBoundary.class);
    StartStudySessionInteractor interactor = new StartStudySessionInteractor(mockRepo, mockPresenter);
    
    StartStudySessionInputData inputData = new StartStudySessionInputData(
        "user123", "CSC207", LocalDateTime.now(), null, "Study chapter 5"
    );
    
    // Act
    interactor.execute(inputData);
    
    // Assert
    verify(mockRepo, times(1)).save(any(StudySession.class));
    verify(mockPresenter, times(1)).prepareSuccessView(any());
}
```

### Integration Tests
- Use Firebase Emulator for Firestore and Auth
- Test repository implementations
- Test API integrations with test credentials

---

## Running the Application

1. **Initialize Firebase in `AppConfig`:**

```java
private void initializeServices() {
    try {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-config.json");
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
        FirebaseApp.initializeApp(options);
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    authService = new FirebaseAuthService();
    // ... initialize other services
}
```

2. **Compile and Run:**

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="app.Main"
```

---

## Common Pitfalls & Solutions

1. **Rate Limiting (Gemini API)**
   - Implement exponential backoff
   - Queue requests if needed
   - Cache results when appropriate

2. **OAuth Token Expiry**
   - Always check token expiry before API calls
   - Automatically refresh using refresh token
   - Handle refresh failures gracefully

3. **Large File Uploads**
   - Show progress indicator
   - Handle timeouts
   - Validate file size before upload

4. **Firestore Pagination**
   - For large result sets, implement pagination
   - Use startAfter() cursor for efficient queries

5. **Thread Safety (Swing)**
   - Always update UI on EDT using `SwingUtilities.invokeLater()`
   - Run long operations on background threads

---

## Next Steps

1. Each team member implements their assigned use cases
2. Create unit tests for each interactor
3. Integrate with Firebase (start with Auth)
4. Test end-to-end flows
5. Implement UI polish and error handling
6. Add logging and monitoring
7. Write user documentation

## Resources

- [Firebase Java Admin SDK](https://firebase.google.com/docs/admin/setup)
- [Gemini API Documentation](https://ai.google.dev/gemini-api/docs)
- [Google Calendar API Java](https://developers.google.com/calendar/api/quickstart/java)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)


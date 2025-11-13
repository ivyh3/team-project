# Quick Start Guide - AI Study Companion

## 🚀 Getting Started in 5 Minutes

### Step 1: Review the Structure (2 min)
```bash
# See what was created
find src/main/java -type d | sort

# Count files
find src/main/java -name "*.java" | wc -l
# Result: 85 Java files
```

### Step 2: Read the Docs (3 min)
1. **ARCHITECTURE.md** - Understand the Clean Architecture layers
2. **PROJECT_SUMMARY.md** - See what's been created
3. **IMPLEMENTATION_GUIDE.md** - Your personal implementation roadmap

---

## 📋 What Each Team Member Should Do

### Freeman (Study Session)
**Priority Files:**
1. `use_case/start_study_session/StartStudySessionInteractor.java`
2. `view/StudySessionView.java`
3. Create: `FirebaseStudySessionRepositoryImpl.java`

**Key Tasks:**
- Implement timer/stopwatch logic
- Create StudySession entities
- Save sessions to Firestore

---

### Mia (Quiz Generation)
**Priority Files:**
1. `frameworks_drivers/gemini/GeminiService.java` ⭐ **Most Important**
2. `use_case/generate_quiz/GenerateQuizInteractor.java`
3. `view/QuizView.java`

**Key Tasks:**
- Integrate Gemini API with HTTP client
- Parse JSON quiz responses
- Display quizzes in UI

**Gemini API Quick Example:**
```java
// POST to: https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=YOUR_KEY
// Body: { "contents": [{ "parts": [{ "text": "YOUR_PROMPT" }] }] }
```

---

### Sarah (Upload/Delete Materials)
**Priority Files:**
1. `frameworks_drivers/storage/StorageService.java` ⭐ **Most Important**
2. `use_case/upload_reference_material/UploadReferenceMaterialInteractor.java`
3. `view/UploadMaterialsView.java`

**Key Tasks:**
- Firebase Storage file upload
- Calculate file fingerprints (SHA-256)
- Extract PDF text with PDFBox

---

### Tess (Account Creation)
**Priority Files:**
1. `frameworks_drivers/firebase/FirebaseAuthService.java` ⭐ **Most Important**
2. `use_case/account_creation/AccountCreationInteractor.java`
3. `view/LoginView.java`

**Key Tasks:**
- Firebase Auth email/password
- Google OAuth integration
- Account linking

---

### Ivy (Quiz History & Metrics)
**Priority Files:**
1. `use_case/view_study_metrics/ViewStudyMetricsInteractor.java` ⭐ **Most Important**
2. `use_case/review_quiz_history/ReviewQuizHistoryInteractor.java`
3. `view/MetricsView.java`

**Key Tasks:**
- Calculate study metrics (averages, totals)
- Query and filter quizzes
- Display charts (use JFreeChart or similar)

---

### David (Schedule & Calendar)
**Priority Files:**
1. `frameworks_drivers/google_calendar/GoogleCalendarService.java` ⭐ **Most Important**
2. `frameworks_drivers/google_calendar/OAuthService.java`
3. `use_case/schedule_study_session/ScheduleStudySessionInteractor.java`

**Key Tasks:**
- Google OAuth 2.0 flow
- Calendar event creation
- Store/refresh OAuth tokens

---

## 🔧 Setup Checklist

### Everyone Should Do This First:

#### 1. Add Dependencies to `pom.xml`
```xml
<!-- Copy from IMPLEMENTATION_GUIDE.md -->
<dependencies>
    <!-- Firebase Admin SDK -->
    <!-- Google API Client -->
    <!-- OkHttp -->
    <!-- Gson -->
    <!-- PDFBox -->
    <!-- JUnit & Mockito -->
</dependencies>
```

#### 2. Create `.gitignore`
```
src/main/resources/firebase-config.json
src/main/resources/application.properties
target/
.idea/
*.iml
```

#### 3. Set Up Firebase (Tess can lead this)
- Create Firebase project: https://console.firebase.google.com
- Enable Auth, Firestore, Storage
- Download service account key
- Place in `src/main/resources/firebase-config.json`

#### 4. Get API Keys
- **Gemini**: https://aistudio.google.com/app/apikey (Mia)
- **Google OAuth**: https://console.cloud.google.com (David)

#### 5. Create Config File
`src/main/resources/application.properties`:
```properties
firebase.project.id=your-project-id
gemini.api.key=your-key
google.oauth.client.id=your-client-id
google.oauth.client.secret=your-secret
```

---

## 📝 Implementation Pattern

### Standard Flow for Each Use Case:

1. **Interactor** (Business Logic)
```java
@Override
public void execute(InputData inputData) {
    try {
        // 1. Validate input
        // 2. Call repositories/services
        // 3. Create entities
        // 4. Save to repository
        // 5. Prepare success view
        outputBoundary.prepareSuccessView(outputData);
    } catch (Exception e) {
        outputBoundary.prepareFailView(e.getMessage());
    }
}
```

2. **Repository Implementation**
```java
@Override
public void save(Entity entity) {
    Map<String, Object> data = entityToMap(entity);
    String path = "users/" + entity.getOwnerId() + "/collection/" + entity.getId();
    firestoreService.saveDocument(path, entity.getId(), data);
}
```

3. **View**
```java
public MyView() {
    initializeComponents();
    layoutComponents();
}

private void layoutComponents() {
    setLayout(new BorderLayout());
    // Add components
}
```

4. **Controller**
```java
public void execute(...params) {
    InputData inputData = new InputData(...params);
    interactor.execute(inputData);
}
```

---

## 🧪 Testing Template

```java
@Test
public void testUseCase() {
    // Arrange
    Repository mockRepo = mock(Repository.class);
    OutputBoundary mockPresenter = mock(OutputBoundary.class);
    Interactor interactor = new Interactor(mockRepo, mockPresenter);
    
    InputData input = new InputData(...);
    
    // Act
    interactor.execute(input);
    
    // Assert
    verify(mockRepo, times(1)).save(any());
    verify(mockPresenter, times(1)).prepareSuccessView(any());
}
```

---

## 🆘 Common Issues & Solutions

### Issue: Firebase SDK not initialized
**Solution**: Initialize in `AppConfig.initializeServices()`:
```java
FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-config.json");
FirebaseOptions options = FirebaseOptions.builder()
    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    .build();
FirebaseApp.initializeApp(options);
```

### Issue: Rate limit errors from Gemini
**Solution**: Implement exponential backoff:
```java
int retries = 0;
while (retries < 3) {
    try {
        return callGeminiAPI();
    } catch (RateLimitException e) {
        Thread.sleep((long) Math.pow(2, retries) * 1000);
        retries++;
    }
}
```

### Issue: OAuth callback not working
**Solution**: Run local server on port 8080:
```java
HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
server.createContext("/oauth/callback", new OAuthCallbackHandler());
server.start();
```

### Issue: UI not updating
**Solution**: Always use EDT:
```java
SwingUtilities.invokeLater(() -> {
    // Update UI here
});
```

---

## 📞 Communication

### Questions About Architecture?
Ask in the team Discord/Slack. Reference the specific file and line.

### Blocked on Dependencies?
Check if someone else needs to implement something first. Use TODO comments.

### Need Help with Firebase/APIs?
Check `IMPLEMENTATION_GUIDE.md` for code examples.

---

## 🎯 Definition of Done

For each use case to be "complete":

- ✅ Interactor fully implemented (no TODOs)
- ✅ At least 2 unit tests passing
- ✅ Repository implementation created
- ✅ View layout complete
- ✅ Controller wired to view
- ✅ Tested manually end-to-end
- ✅ Error handling added
- ✅ Comments/documentation added

---

## 🏃 Sprint Schedule (Suggested)

### Week 1: Setup & Core Services
- All: Add dependencies, config files
- Tess: Firebase Auth
- Sarah: Firebase Storage
- Mia: Gemini API

### Week 2: Use Case Implementation
- Everyone: Implement assigned interactors
- Everyone: Write unit tests

### Week 3: UI & Integration
- Everyone: Complete view layouts
- Everyone: Wire controllers to views
- Everyone: Test end-to-end

### Week 4: Polish & Testing
- All: Bug fixes
- All: Integration testing
- All: Code review
- All: Prepare demo

---

## 🔗 Quick Links

- **Firebase Console**: https://console.firebase.google.com
- **Gemini API Studio**: https://aistudio.google.com
- **Google Cloud Console**: https://console.cloud.google.com
- **Firebase Admin SDK Docs**: https://firebase.google.com/docs/admin/setup
- **Gemini API Docs**: https://ai.google.dev/gemini-api/docs
- **Google Calendar API**: https://developers.google.com/calendar/api

---

## 💡 Pro Tips

1. **Start with tests** - Write the test first, then implement
2. **Use mocks** - Don't depend on external services in unit tests
3. **Keep entities pure** - No Firebase/Gemini code in entity classes
4. **Log everything** - Add logging for debugging
5. **Commit often** - Small, focused commits with good messages
6. **Ask early** - If stuck for >30 min, ask for help

---

**Good luck! You have everything you need to build this. 🚀**


package frameworks_drivers.firebase;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import entity.StudyQuiz;
import entity.StudyQuizFactory;

/**
 * Data access object for StudyQuiz entities.
 */
public class FirebaseStudyQuizDataAccessObject {
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String DURATION_SECONDS = "duration_seconds";
    public static final String SCORE = "score";
    public static final String START_TIME_TIMESTAMP = "start_time_timestamp";
    public static final String END_TIME_TIMESTAMP = "end_time_timestamp";
    private static final String USERS_COLLECTION = "users";
    private static final String QUIZZES_COLLECTION = "studyQuizzes";
    private final Firestore firestore;
    private final StudyQuizFactory studyQuizFactory;

    public FirebaseStudyQuizDataAccessObject(StudyQuizFactory studyQuizFactory) {
        this.studyQuizFactory = studyQuizFactory;
        this.firestore = FirestoreClient.getFirestore();
    }

    /**
     * Adds a StudyQuiz entity to the firebase.
     *
     * @param userId The USer ID
     * @param quiz   The StudyQuiz to add
     * @return The StudyQuiz added, including its ID
     */
    public StudyQuiz addStudyQuiz(String userId, StudyQuiz quiz) {
        final CollectionReference quizzesRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(QUIZZES_COLLECTION);

        final Map<String, Object> data = new HashMap<>();

        // Store times as ISO strings in utc and as epoch millis for range queries.
        final ZonedDateTime utcStartTime = convertToUtc(quiz.getStartTime());
        final ZonedDateTime utcEndTime = convertToUtc(quiz.getEndTime());

        data.put(START_TIME, utcStartTime.toString());
        data.put(END_TIME, utcEndTime.toString());
        data.put(DURATION_SECONDS, quiz.getDuration().toSeconds());
        data.put(SCORE, quiz.getScore());
        data.put(START_TIME_TIMESTAMP, utcStartTime.toInstant().toEpochMilli());
        data.put(END_TIME_TIMESTAMP, utcEndTime.toInstant().toEpochMilli());

        try {
            final ApiFuture<DocumentReference> result = quizzesRef.add(data);
            final String documentId = result.get().getId();

            return studyQuizFactory.create(
                documentId,
                quiz.getScore(),
                quiz.getStartTime(),
                quiz.getEndTime());
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error adding study quiz: " + err.getMessage(), err);
        }
    }

    /**
     * Gets a StudyQuiz entity based on the given quizID.
     *
     * @param userId The user ID
     * @param quizId The QuizID
     * @return The StudyQuiz entity with the given ID or null if does not exist
     */
    public StudyQuiz getStudyQuizById(String userId, String quizId) {
        final DocumentReference quizRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(QUIZZES_COLLECTION)
            .document(quizId);

        try {
            final ApiFuture<DocumentSnapshot> future = quizRef.get();
            final DocumentSnapshot document = future.get();

            if (document.exists()) {
                return createStudyQuizFromDocument(document);
            }
            else {
                return null;
            }
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study quiz: " + err.getMessage(), err);
        }
    }

    /**
     * Gets all study quizzes for a user.
     *
     * @param userId The userID
     * @return List of all StudyQuizzes that the user had
     */
    public List<StudyQuiz> getStudyQuizzes(String userId) {
        final CollectionReference quizzesRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(QUIZZES_COLLECTION);

        final List<StudyQuiz> quizzes = new ArrayList<>();

        try {
            final ApiFuture<QuerySnapshot> future = quizzesRef.get();
            final List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                quizzes.add(createStudyQuizFromDocument(document));
            }

            return quizzes;
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study quizzes: " + err.getMessage(), err);
        }
    }

    /**
     * Gets the study quizzes that STARTED within the given time range.
     *
     * @param userId    The user ID
     * @param startTime The start time of the range
     * @param endTime   The end time of the range
     * @return The list of StudyQuizzes within the range
     */
    public List<StudyQuiz> getStudyQuizzesInRange(String userId, LocalDateTime startTime, LocalDateTime endTime) {

        final CollectionReference quizzesRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(QUIZZES_COLLECTION);

        final ZonedDateTime utcStartTime = convertToUtc(startTime);
        final ZonedDateTime utcEndTime = convertToUtc(endTime);

        final long startTimeStamp = utcStartTime.toInstant().toEpochMilli();
        final long endTimeStamp = utcEndTime.toInstant().toEpochMilli();

        final Query query = quizzesRef
            .whereGreaterThanOrEqualTo(START_TIME_TIMESTAMP, startTimeStamp)
            .whereLessThan(START_TIME_TIMESTAMP, endTimeStamp);

        final ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            final List<StudyQuiz> quizzes = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                quizzes.add(createStudyQuizFromDocument(document));
            }

            return quizzes;
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study quizzes: " + err.getMessage(), err);
        }
    }

    /**
     * Create a StudyQuiz entity from a Firestore DocumentSnapshot.
     *
     * @param document the document with the study quiz data
     * @return the study quiz entity
     */
    private StudyQuiz createStudyQuizFromDocument(DocumentSnapshot document) {
        final String startTimeStr = document.getString(START_TIME);
        final String endTimeStr = document.getString(END_TIME);

        final ZonedDateTime startZoned = ZonedDateTime.parse(startTimeStr);
        final ZonedDateTime endZoned = ZonedDateTime.parse(endTimeStr);

        final LocalDateTime startTime = convertToLocalDateTime(startZoned);
        final LocalDateTime endTime = convertToLocalDateTime(endZoned);

        final Double scoreDouble = document.getDouble(SCORE);
        final float score;
        if (scoreDouble != null) {
            score = scoreDouble.floatValue();
        }
        else {
            score = 0f;
        }

        return studyQuizFactory.create(
            document.getId(),
            score,
            startTime,
            endTime);
    }

    /**
     * Return the UTC equivalent of a LocalDateTime in the system default timezone.
     *
     * @param localDateTime the LocalDateTime to convert
     * @return the ZonedDateTime in UTC
     */
    private ZonedDateTime convertToUtc(LocalDateTime localDateTime) {
        final ZonedDateTime ldtZoned = localDateTime.atZone(ZoneId.systemDefault());
        return ldtZoned.withZoneSameInstant(ZoneOffset.UTC);
    }

    /**
     * Return the LocalDateTime equivalent of a ZonedDateTime in the system default
     * timezone.
     *
     * @param zonedDateTime the ZonedDateTime to convert
     * @return the LocalDateTime in the system default timezone
     */
    private LocalDateTime convertToLocalDateTime(ZonedDateTime zonedDateTime) {
        final ZonedDateTime localZoned = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
}

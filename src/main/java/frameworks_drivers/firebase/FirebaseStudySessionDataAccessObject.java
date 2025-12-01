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
import entity.StudySession;
import entity.StudySessionFactory;
import use_case.end_study_session.EndStudySessionDataAccessInterface;

/**
 * Firebase Data Access Object for StudySession entities.
 */
public class FirebaseStudySessionDataAccessObject implements EndStudySessionDataAccessInterface {
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String DURATION_SECONDS = "duration_seconds";
    public static final String START_TIME_TIMESTAMP = "start_time_timestamp";
    public static final String END_TIME_TIMESTAMP = "end_time_timestamp";
    private static final String USERS_COLLECTION = "users";
    private static final String STUDY_SESSIONS_COLLECTION = "studySessions";
    private final Firestore firestore;
    private final StudySessionFactory studySessionFactory;

    public FirebaseStudySessionDataAccessObject(StudySessionFactory studySessionFactory) {
        this.studySessionFactory = studySessionFactory;
        this.firestore = FirestoreClient.getFirestore();
    }

    /**
     * Adds a study session to the firebase.
     *
     * @param userId  The user id associated with the study session
     * @param session The study session to save.
     * @return The StudySession entity with its firebaseID included
     */
    public StudySession addStudySession(String userId, StudySession session) {
        final CollectionReference studySessionRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(STUDY_SESSIONS_COLLECTION);

        final Map<String, Object> data = new HashMap<>();

        // Store times as an ISO string in UTC tz
        // Store duration in seconds.
        // Store epoch millis for range queries.

        final ZonedDateTime utcStartTime = convertToUtc(session.getStartTime());
        final ZonedDateTime utcEndTime = convertToUtc(session.getEndTime());

        data.put(START_TIME, utcStartTime.toString());
        data.put(END_TIME, utcEndTime.toString());
        data.put(DURATION_SECONDS, session.getDuration().toSeconds());
        data.put(START_TIME_TIMESTAMP, utcStartTime.toInstant().toEpochMilli());
        data.put(END_TIME_TIMESTAMP, utcEndTime.toInstant().toEpochMilli());

        try {
            final ApiFuture<DocumentReference> result = studySessionRef.add(data);

            final String documentId = result.get().getId();

            return studySessionFactory.create(
                documentId,
                session.getStartTime(),
                session.getEndTime());
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error adding study session: " + err.getMessage());
        }
    }

    /**
     * Gets a StudySession Entity from storage given its ID.
     *
     * @param userId    The user id to look for
     * @param sessionId The session id to look for
     * @return The StudySession entity with the given sessionID, null if does not exist
     */
    public StudySession getStudySessionById(String userId, String sessionId) {
        final DocumentReference sessionRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(STUDY_SESSIONS_COLLECTION)
            .document(sessionId);

        try {
            final ApiFuture<DocumentSnapshot> future = sessionRef.get();
            final DocumentSnapshot document = future.get();

            if (document.exists()) {
                return createStudySessionFromDocument(document);
            }
            else {
                return null;
            }
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study session: " + err.getMessage());
        }
    }

    /**
     * Gets all study sessions for a user.
     *
     * @param userId The user to look at
     * @return List of StudySession entities for all the sessions the user had
     */
    public List<StudySession> getStudySessions(String userId) {
        final CollectionReference studySessionRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(STUDY_SESSIONS_COLLECTION);

        final List<StudySession> sessions = new ArrayList<>();

        try {
            final ApiFuture<QuerySnapshot> future = studySessionRef.get();
            final List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                sessions.add(createStudySessionFromDocument(document));
            }

            return sessions;
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study sessions: " + err.getMessage());
        }
    }

    /**
     * Get the study sessions that STARTED within the given range.
     *
     * @param userId    The userID
     * @param startTime The start time of the range
     * @param endTime   The end time of the range
     * @return List of StudySession entities which started within the given range.
     */
    public List<StudySession> getStudySessionsInRange(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        final CollectionReference studySessionRef = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(STUDY_SESSIONS_COLLECTION);

        final ZonedDateTime startTimeUtc = convertToUtc(startTime);
        final ZonedDateTime endTimeUtc = convertToUtc(endTime);
        final long startTimeStamp = startTimeUtc.toInstant().toEpochMilli();
        final long endTimeStamp = endTimeUtc.toInstant().toEpochMilli();

        final Query query = studySessionRef
            .whereGreaterThanOrEqualTo(START_TIME_TIMESTAMP, startTimeStamp)
            .whereLessThan(START_TIME_TIMESTAMP, endTimeStamp);

        final ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            final List<StudySession> sessions = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                sessions.add(createStudySessionFromDocument(document));
            }

            return sessions;
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study sessions: " + err.getMessage());
        }
    }

    /**
     * Create a StudySession entity from a Firestore DocumentSnapshot.
     *
     * @param document the document with the study session data
     * @return the study session entity
     */
    private StudySession createStudySessionFromDocument(DocumentSnapshot document) {
        final String startTimeStr = document.getString(START_TIME);
        final String endTimeStr = document.getString(END_TIME);

        final ZonedDateTime startZoned = ZonedDateTime.parse(startTimeStr);
        final ZonedDateTime endZoned = ZonedDateTime.parse(endTimeStr);

        return studySessionFactory.create(
            document.getId(),
            convertToLocalDateTime(startZoned),
            convertToLocalDateTime(endZoned));
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

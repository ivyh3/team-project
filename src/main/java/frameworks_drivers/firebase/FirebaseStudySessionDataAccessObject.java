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

    public StudySession addStudySession(String userId, StudySession session) {
        final CollectionReference studySessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(STUDY_SESSIONS_COLLECTION);

        final Map<String, Object> data = new HashMap<>();
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
        } catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error adding study session: " + err.getMessage());
        }
    }

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
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study session: " + err.getMessage());
        }
    }

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
        } catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study sessions: " + err.getMessage());
        }
    }

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
        } catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving study sessions: " + err.getMessage());
        }
    }

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

    private ZonedDateTime convertToUtc(LocalDateTime localDateTime) {
        final ZonedDateTime ldtZoned = localDateTime.atZone(ZoneId.systemDefault());
        return ldtZoned.withZoneSameInstant(ZoneOffset.UTC);
    }

    private LocalDateTime convertToLocalDateTime(ZonedDateTime zonedDateTime) {
        final ZonedDateTime localZoned = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
}
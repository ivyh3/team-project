package frameworks_drivers.firebase;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String USERS_COLLECTION = "users";
    private static final String STUDY_SESSIONS_COLLECTION = "studySessions";
    private final Firestore firestore;
    private final StudySessionFactory studySessionFactory;

    public FirebaseStudySessionDataAccessObject(StudySessionFactory studySessionFactory) {
        this.studySessionFactory = studySessionFactory;
        this.firestore = FirestoreClient.getFirestore();
    }

    public StudySession addStudySession(String userId, StudySession session) {
        CollectionReference studySessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(STUDY_SESSIONS_COLLECTION);

        Map<String, Object> data = new HashMap<>();

        // Store times as an ISO string in UTC tz
        // Store duration in seconds.
        // Store epoch millis for range queries.

        ZonedDateTime utcStartTime = convertToUtc(session.getStartTime());
        ZonedDateTime utcEndTime = convertToUtc(session.getEndTime());

        data.put("start_time", utcStartTime.toString());
        data.put("end_time", utcEndTime.toString());
        data.put("duration_seconds", session.getDuration().toSeconds());
        data.put("start_time_timestamp", utcStartTime.toInstant().toEpochMilli());
        data.put("end_time_timestamp", utcEndTime.toInstant().toEpochMilli());

        try {
            ApiFuture<DocumentReference> result = studySessionRef.add(data);

            String documentId = result.get().getId();

            return studySessionFactory.create(
                    documentId,
                    session.getStartTime(),
                    session.getEndTime());
        } catch (Exception e) {
            throw new RuntimeException("Error adding study session: " + e.getMessage());
        }
    }

    public StudySession getStudySessionById(String userId, String sessionId) {
        DocumentReference sessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(STUDY_SESSIONS_COLLECTION)
                .document(sessionId);

        try {
            ApiFuture<DocumentSnapshot> future = sessionRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return createStudySessionFromDocument(document);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving study session: " + e.getMessage());
        }
    }

    public List<StudySession> getStudySessions(String userId) {
        CollectionReference studySessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(STUDY_SESSIONS_COLLECTION);

        List<StudySession> sessions = new ArrayList<>();

        try {
            ApiFuture<QuerySnapshot> future = studySessionRef.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                sessions.add(createStudySessionFromDocument(document));
            }

            return sessions;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving study sessions: " + e.getMessage());
        }
    }

    public List<StudySession> getStudySessionsInRange(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        CollectionReference studySessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(STUDY_SESSIONS_COLLECTION);

        ZonedDateTime startTimeUtc = convertToUtc(startTime);
        ZonedDateTime endTimeUtc = convertToUtc(endTime);
        long startTimeStamp = startTimeUtc.toInstant().toEpochMilli();
        long endTimeStamp = endTimeUtc.toInstant().toEpochMilli();

        Query query = studySessionRef
                .whereGreaterThanOrEqualTo("start_time_timestamp", startTimeStamp)
                .whereLessThan("start_time_timestamp", endTimeStamp);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<StudySession> sessions = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                sessions.add(createStudySessionFromDocument(document));
            }

            return sessions;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving study sessions: " + e.getMessage());
        }
    }

    /**
     * Create a StudySession entity from a Firestore DocumentSnapshot.
     * 
     * @param document the document with the study session data
     * @return the study session entity
     */
    private StudySession createStudySessionFromDocument(DocumentSnapshot document) {
        String startTimeStr = document.getString("start_time");
        String endTimeStr = document.getString("end_time");

        ZonedDateTime startZoned = ZonedDateTime.parse(startTimeStr);
        ZonedDateTime endZoned = ZonedDateTime.parse(endTimeStr);

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
        ZonedDateTime ldtZoned = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneOffset.UTC);
        return utcZoned;
    }

    /**
     * Return the LocalDateTime equivalent of a ZonedDateTime in the system default
     * timezone.
     * 
     * @param zonedDateTime the ZonedDateTime to convert
     * @return the LocalDateTime in the system default timezone
     */
    private LocalDateTime convertToLocalDateTime(ZonedDateTime zonedDateTime) {
        ZonedDateTime localZoned = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
}

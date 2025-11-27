package frameworks_drivers.firebase;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

        // Store times as an ISO string
        // Store duration in seconds.
        data.put("start_time", session.getStartTime().toString());
        data.put("end_time", session.getEndTime().toString());
        data.put("duration_seconds", session.getDuration().toSeconds());
        data.put("start_time_timestamp", session.getStartTime().toInstant(ZoneOffset.UTC).toEpochMilli());
        data.put("end_time_timestamp", session.getEndTime().toInstant(ZoneOffset.UTC).toEpochMilli());

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
                String startTimeStr = document.getString("start_time");
                String endTimeStr = document.getString("end_time");

                return studySessionFactory.create(
                        document.getId(),
                        java.time.LocalDateTime.parse(startTimeStr),
                        java.time.LocalDateTime.parse(endTimeStr));
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
                String startTimeStr = document.getString("start_time");
                String endTimeStr = document.getString("end_time");

                StudySession session = studySessionFactory.create(
                        document.getId(),
                        java.time.LocalDateTime.parse(startTimeStr),
                        java.time.LocalDateTime.parse(endTimeStr));

                sessions.add(session);
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

        long startTimeStamp = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endTimeStamp = endTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        Query query = studySessionRef
                .whereGreaterThanOrEqualTo("start_time_timestamp", startTimeStamp)
                .whereLessThan("start_time_timestamp", endTimeStamp);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<StudySession> sessions = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Long startTimestamp = document.getLong("start_time_timestamp");
                Long endTimestamp = document.getLong("end_time_timestamp");

                if (startTimestamp != null && endTimestamp != null) {
                    LocalDateTime sessionStart = LocalDateTime.ofInstant(
                            java.time.Instant.ofEpochMilli(startTimestamp),
                            java.time.ZoneOffset.UTC
                    );

                    LocalDateTime sessionEnd = LocalDateTime.ofInstant(
                            java.time.Instant.ofEpochMilli(endTimestamp),
                            java.time.ZoneOffset.UTC
                    );

                    StudySession session = studySessionFactory.create(
                            document.getId(),
                            sessionStart,
                            sessionEnd);

                    sessions.add(session);
                }
            }

            return sessions;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving study sessions: " + e.getMessage());
        }
    }
}

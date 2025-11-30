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
import entity.ScheduledSession;
import entity.ScheduledSessionFactory;
import use_case.schedule_study_session.ScheduleStudySessionDataAccessInterface;

/**
 * Data access object for scheduled study sessions.
 */
public class FirebaseScheduledSessionDataAccessObject implements ScheduleStudySessionDataAccessInterface {
    public static final String START_TIME_TIMESTAMP = "start_time_timestamp";
    public static final String END_TIME = "end_time";
    public static final String START_TIME = "start_time";
    public static final String END_TIME_TIMESTAMP = "end_time_timestamp";
    private static final String USERS_COLLECTION = "users";
    private static final String SCHEDULED_SESSION_COLLECTION = "scheduledSessions";
    private static final String TITLE = "title";
    private final Firestore firestore;
    private final ScheduledSessionFactory scheduledSessionFactory;

    public FirebaseScheduledSessionDataAccessObject(ScheduledSessionFactory scheduledSessionFactory) {
        this.scheduledSessionFactory = scheduledSessionFactory;
        this.firestore = FirestoreClient.getFirestore();
    }

    @Override
    public ScheduledSession saveSession(String userId, ScheduledSession session) {
        return addScheduledSession(userId, session);
    }

    @Override
    public List<ScheduledSession> getAllSessions(String userId) {
        return getScheduledSessions(userId);
    }
    /**
     * Adds a scheduled study session.
     *
     * @param userId  The user to add the session for
     * @param session The scheduled session
     * @return The added scheduled session
     */
    public ScheduledSession addScheduledSession(String userId, ScheduledSession session) {
        final CollectionReference scheduledSessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(SCHEDULED_SESSION_COLLECTION);

        final Map<String, Object> data = new HashMap<>();

        // Store times as an ISO string in UTC tz
        // Store duration in seconds.
        // Store epoch millis for range queries.

        final ZonedDateTime utcStartTime = convertToUtc(session.getStartTime());
        final ZonedDateTime utcEndTime = convertToUtc(session.getEndTime());

        data.put(START_TIME, utcStartTime.toString());
        data.put(END_TIME, utcEndTime.toString());
        data.put(START_TIME_TIMESTAMP, utcStartTime.toInstant().toEpochMilli());
        data.put(END_TIME_TIMESTAMP, utcEndTime.toInstant().toEpochMilli());
        data.put(TITLE, session.getTitle());

        try {
            final ApiFuture<DocumentReference> result = scheduledSessionRef.add(data);

            final String documentId = result.get().getId();

            return scheduledSessionFactory.create(
                    documentId,
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getTitle());
        }
        catch (ExecutionException | InterruptedException err) {
            throw new RuntimeException("Error adding scheduled session: " + err.getMessage());
        }
    }

    public ScheduledSession getScheduledSessionById(String userId, String sessionId) {
        final DocumentReference sessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(SCHEDULED_SESSION_COLLECTION)
                .document(sessionId);

        try {
            final ApiFuture<DocumentSnapshot> future = sessionRef.get();
            final DocumentSnapshot document = future.get();

            if (document.exists()) {
                return createScheduledSessionFromDocument(document);
            }
            else {
                return null;
            }
        }
        catch (ExecutionException | InterruptedException err) {
            throw new RuntimeException("Error retrieving scheduled session: " + err.getMessage());
        }
    }

    /**
     * Gets all scheduled sessions for a user.
     *
     * @param userId The user to get their scheduled sessinos from
     * @return List of all scheduled sessions they have
     */
    public List<ScheduledSession> getScheduledSessions(String userId) {
        final CollectionReference scheduledSessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(SCHEDULED_SESSION_COLLECTION);

        final List<ScheduledSession> sessions = new ArrayList<>();

        try {
            final ApiFuture<QuerySnapshot> future = scheduledSessionRef.get();
            final List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                sessions.add(createScheduledSessionFromDocument(document));
            }

            return sessions;
        }
        catch (ExecutionException | InterruptedException err) {
            throw new RuntimeException("Error retrieving scheduled sessions: " + err.getMessage());
        }
    }

    /**
     * Gets scheduled sessions within the given time range.
     *
     * @param userId    The user of interest to look at their scheduled sessions
     * @param startTime The start time of the range
     * @param endTime   The end time of the range
     * @return A list of scheduled sessions within the range
     */
    public List<ScheduledSession> getScheduledSessionsInRange(String userId, LocalDateTime startTime,
                                                              LocalDateTime endTime) {
        final CollectionReference scheduledSessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(SCHEDULED_SESSION_COLLECTION);

        final ZonedDateTime startTimeUtc = convertToUtc(startTime);
        final ZonedDateTime endTimeUtc = convertToUtc(endTime);
        final long startTimeStamp = startTimeUtc.toInstant().toEpochMilli();
        final long endTimeStamp = endTimeUtc.toInstant().toEpochMilli();

        final Query query = scheduledSessionRef
                .whereGreaterThanOrEqualTo(START_TIME_TIMESTAMP, startTimeStamp)
                .whereLessThan(START_TIME_TIMESTAMP, endTimeStamp);

        final ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            final List<ScheduledSession> sessions = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                sessions.add(createScheduledSessionFromDocument(document));
            }

            return sessions;
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving scheduled sessions: " + err.getMessage());
        }
    }

    /**
     * Gets all scheduled sessions after the given time.
     *
     * @param userId    The user id
     * @param startTime The start time
     * @return List of all scheduled sessions that start after the given start time
     */
    public List<ScheduledSession> getScheduledSessionsAfterTime(String userId, LocalDateTime startTime) {
        final CollectionReference scheduledSessionRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(SCHEDULED_SESSION_COLLECTION);

        final ZonedDateTime startTimeUtc = convertToUtc(startTime);
        final long startTimeStamp = startTimeUtc.toInstant().toEpochMilli();

        final Query query = scheduledSessionRef
                .whereGreaterThanOrEqualTo(START_TIME_TIMESTAMP, startTimeStamp);

        final ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            final List<ScheduledSession> sessions = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                sessions.add(createScheduledSessionFromDocument(document));
            }

            return sessions;
        }
        catch (InterruptedException | ExecutionException err) {
            throw new RuntimeException("Error retrieving scheduled sessions: " + err.getMessage());
        }
    }

    /**
     * Create a ScheduledSession entity from a Firestore DocumentSnapshot.
     *
     * @param document the document with the scheduled session data
     * @return the scheduled session entity
     */
    private ScheduledSession createScheduledSessionFromDocument(DocumentSnapshot document) {
        final String startTimeStr = document.getString(START_TIME);
        final String endTimeStr = document.getString(END_TIME);
        final String title = document.getString(TITLE);

        if (startTimeStr == null || endTimeStr == null) {
            throw new RuntimeException("Invalid start time or end time in document");
        }

        final ZonedDateTime startZoned = ZonedDateTime.parse(startTimeStr);
        final ZonedDateTime endZoned = ZonedDateTime.parse(endTimeStr);

        return scheduledSessionFactory.create(
                document.getId(),
                convertToLocalDateTime(startZoned),
                convertToLocalDateTime(endZoned),
                title != null ? title : "Untitled");
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
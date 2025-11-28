package frameworks_drivers.firebase;

import java.time.LocalDate;
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

import entity.Question;
import entity.StudyQuiz;
import entity.StudyQuizFactory;

// TODO: Fix this AI generated code up (although I probably wouldn't do much better...)
/**
 * Data access object for StudyQuiz entities.
 */
public class FirebaseStudyQuizDataAccessObject {
	private static final String USERS_COLLECTION = "users";
	private static final String QUIZZES_COLLECTION = "studyQuizzes";
	private final Firestore firestore;
	private final StudyQuizFactory studyQuizFactory;

	public FirebaseStudyQuizDataAccessObject(StudyQuizFactory studyQuizFactory) {
		this.studyQuizFactory = studyQuizFactory;
		this.firestore = FirestoreClient.getFirestore();
	}

	public StudyQuiz addStudyQuiz(String userId, StudyQuiz quiz) {
		CollectionReference quizzesRef = firestore.collection(USERS_COLLECTION)
				.document(userId)
				.collection(QUIZZES_COLLECTION);

		Map<String, Object> data = new HashMap<>();

		// Store times as ISO strings in utc and as epoch millis for range queries.
		ZonedDateTime utcStartTime = convertToUtc(quiz.getStartTime());
		ZonedDateTime utcEndTime = convertToUtc(quiz.getEndTime());

		data.put("start_time", utcStartTime.toString());
		data.put("end_time", utcEndTime.toString());
		data.put("duration_seconds", quiz.getDuration().toSeconds());
		data.put("score", quiz.getScore());
		data.put("start_time_timestamp", utcStartTime.toInstant().toEpochMilli());
		data.put("end_time_timestamp", utcEndTime.toInstant().toEpochMilli());

		try {
			ApiFuture<DocumentReference> result = quizzesRef.add(data);
			String documentId = result.get().getId();

			return studyQuizFactory.create(
                    quiz.getQuestions(),
					quiz.getScore(),
					quiz.getStartTime(),
					quiz.getEndTime());
		} catch (Exception e) {
			throw new RuntimeException("Error adding study quiz: " + e.getMessage(), e);
		}
	}

	public StudyQuiz getStudyQuizById(String userId, String quizId) {
		DocumentReference quizRef = firestore.collection(USERS_COLLECTION)
				.document(userId)
				.collection(QUIZZES_COLLECTION)
				.document(quizId);

		try {
			ApiFuture<DocumentSnapshot> future = quizRef.get();
			DocumentSnapshot document = future.get();

			if (document.exists()) {
				return createStudyQuizFromDocument(document);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving study quiz: " + e.getMessage(), e);
		}
	}

	public List<StudyQuiz> getStudyQuizzes(String userId) {
		CollectionReference quizzesRef = firestore.collection(USERS_COLLECTION)
				.document(userId)
				.collection(QUIZZES_COLLECTION);

		List<StudyQuiz> quizzes = new ArrayList<>();

		try {
			ApiFuture<QuerySnapshot> future = quizzesRef.get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();

			for (QueryDocumentSnapshot document : documents) {
				quizzes.add(createStudyQuizFromDocument(document));
			}

			return quizzes;
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving study quizzes: " + e.getMessage(), e);
		}
	}

	public List<StudyQuiz> getStudyQuizzesInRange(String userId, LocalDateTime startTime, LocalDateTime endTime) {

		CollectionReference quizzesRef = firestore.collection(USERS_COLLECTION)
				.document(userId)
				.collection(QUIZZES_COLLECTION);

		ZonedDateTime utcStartTime = convertToUtc(startTime);
		ZonedDateTime utcEndTime = convertToUtc(endTime);

		long startTimeStamp = utcStartTime.toInstant().toEpochMilli();
		long endTimeStamp = utcEndTime.toInstant().toEpochMilli();

		Query query = quizzesRef
				.whereGreaterThanOrEqualTo("start_time_timestamp", startTimeStamp)
				.whereLessThan("start_time_timestamp", endTimeStamp);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		try {
			List<StudyQuiz> quizzes = new ArrayList<>();
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				quizzes.add(createStudyQuizFromDocument(document));
			}

			return quizzes;
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving study quizzes: " + e.getMessage(), e);
		}
	}

	/**
	 * Create a StudyQuiz entity from a Firestore DocumentSnapshot.
	 * 
	 * @param document the document with the study quiz data
	 * @return the study quiz entity
	 */
	private StudyQuiz createStudyQuizFromDocument(DocumentSnapshot document) {
		String startTimeStr = document.getString("start_time");
		String endTimeStr = document.getString("end_time");

		ZonedDateTime startZoned = ZonedDateTime.parse(startTimeStr);
		ZonedDateTime endZoned = ZonedDateTime.parse(endTimeStr);

		LocalDateTime startTime = convertToLocalDateTime(startZoned);
		LocalDateTime endTime = convertToLocalDateTime(endZoned);

		Double scoreDouble = document.getDouble("score");
		float score = scoreDouble != null ? scoreDouble.floatValue() : 0f;
        List<Question> questions = new ArrayList<>();

		StudyQuiz quiz = studyQuizFactory.create(
				questions,
				score,
				startTime,
				endTime);

		return quiz;
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

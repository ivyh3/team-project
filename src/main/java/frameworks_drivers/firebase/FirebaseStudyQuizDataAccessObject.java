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

import entity.StudyQuiz;
import entity.StudyQuizFactory;

// TODO: Fix this AI generated code up (although I probably wouldn't do much better...)
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

		// Store times as ISO strings and as epoch millis for range queries.
		data.put("start_time", quiz.getStartTime().toString());
		data.put("end_time", quiz.getEndTime().toString());
		data.put("duration_seconds", quiz.getDuration().toSeconds());
		data.put("score", quiz.getScore());
		data.put("start_time_timestamp", quiz.getStartTime().toInstant(ZoneOffset.UTC).toEpochMilli());
		data.put("end_time_timestamp", quiz.getEndTime().toInstant(ZoneOffset.UTC).toEpochMilli());

		try {
			ApiFuture<DocumentReference> result = quizzesRef.add(data);
			String documentId = result.get().getId();

			return studyQuizFactory.create(
					documentId,
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
				String startTimeStr = document.getString("start_time");
				String endTimeStr = document.getString("end_time");
				Double scoreDouble = document.getDouble("score");
				float score = scoreDouble != null ? scoreDouble.floatValue() : 0f;

				return studyQuizFactory.create(
						document.getId(),
						score,
						LocalDateTime.parse(startTimeStr),
						LocalDateTime.parse(endTimeStr));
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
				String startTimeStr = document.getString("start_time");
				String endTimeStr = document.getString("end_time");
				Double scoreDouble = document.getDouble("score");
				float score = scoreDouble != null ? scoreDouble.floatValue() : 0f;

				StudyQuiz quiz = studyQuizFactory.create(
						document.getId(),
						score,
						LocalDateTime.parse(startTimeStr),
						LocalDateTime.parse(endTimeStr));

				quizzes.add(quiz);
			}

			return quizzes;
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving study quizzes: " + e.getMessage(), e);
		}
	}

	public List<StudyQuiz> getStudyQuizzesBetweenDays(String userId, LocalDateTime startDay, LocalDateTime endDay) {

		CollectionReference quizzesRef = firestore.collection(USERS_COLLECTION)
				.document(userId)
				.collection(QUIZZES_COLLECTION);

		long startTimeStamp = startDay.toInstant(ZoneOffset.UTC).toEpochMilli();
		long endTimeStamp = endDay.toInstant(ZoneOffset.UTC).toEpochMilli();

		Query query = quizzesRef
				.whereGreaterThanOrEqualTo("start_time_timestamp", startTimeStamp)
				.whereLessThan("start_time_timestamp", endTimeStamp);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		try {
			List<StudyQuiz> quizzes = new ArrayList<>();
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				String startTimeStr = document.getString("start_time");
				String endTimeStr = document.getString("end_time");
				Double scoreDouble = document.getDouble("score");
				float score = scoreDouble != null ? scoreDouble.floatValue() : 0f;

				StudyQuiz quiz = studyQuizFactory.create(
						document.getId(),
						score,
						LocalDateTime.parse(startTimeStr),
						LocalDateTime.parse(endTimeStr));

				quizzes.add(quiz);
			}

			return quizzes;
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving study quizzes: " + e.getMessage(), e);
		}
	}
}

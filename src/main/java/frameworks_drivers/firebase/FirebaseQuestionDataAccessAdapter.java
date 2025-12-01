package frameworks_drivers.firebase;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import repository.QuestionDataAccess;
import entity.Question;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class FirebaseQuestionDataAccessAdapter implements QuestionDataAccess {

    private static final String USERS_COLLECTION = "users";
    private static final String QUESTIONS_COLLECTION = "questions";

    private final Firestore firestore;

    public FirebaseQuestionDataAccessAdapter(FirebaseStudyQuizDataAccessObject quizDataAccessObject) {
        this.firestore = FirestoreClient.getFirestore();
    }

    @Override
    public Question save(Question question) {
        try {
            DocumentReference docRef = firestore.collection(QUESTIONS_COLLECTION)
                    .document(question.getId());
            Map<String, Object> data = new HashMap<>();
            data.put("questionText", question.getText());
            data.put("choices", question.getOptions());
            data.put("correctIndex", question.getCorrectIndex());
            data.put("explanation", question.getExplanation());
            data.put("wasCorrect", question.isCorrect()); // updated getter

            ApiFuture<WriteResult> result = docRef.set(data);
            result.get(); // wait for write to complete
            return question;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error saving question: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean saveAll(List<Question> questions) {
        try {
            List<ApiFuture<WriteResult>> futures = new ArrayList<>();
            for (Question question : questions) {
                futures.add(firestore.collection(QUESTIONS_COLLECTION)
                        .document(question.getId())
                        .set(Map.of(
                                "questionText", question.getText(),
                                "choices", question.getOptions(),
                                "correctIndex", question.getCorrectIndex(),
                                "explanation", question.getExplanation(),
                                "wasCorrect", question.isCorrect()
                        )));
            }
            for (ApiFuture<WriteResult> future : futures) {
                future.get(); // wait for all writes
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error saving questions: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Question> findById(String id) {
        try {
            DocumentSnapshot doc = firestore.collection(QUESTIONS_COLLECTION)
                    .document(id)
                    .get()
                    .get();

            if (doc.exists()) {
                Question question = new Question(
                        doc.getId(),
                        doc.getString("questionText"),
                        (List<String>) doc.get("choices"),
                        doc.getLong("correctIndex").intValue(),
                        doc.getString("explanation")
                );
                question.setCorrect(Boolean.TRUE.equals(doc.getBoolean("wasCorrect")));
                return Optional.of(question);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error retrieving question: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Question> findAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(QUESTIONS_COLLECTION).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            return documents.stream().map(doc -> {
                Question q = new Question(
                        doc.getId(),
                        doc.getString("questionText"),
                        (List<String>) doc.get("choices"),
                        doc.getLong("correctIndex").intValue(),
                        doc.getString("explanation")
                );
                q.setCorrect(Boolean.TRUE.equals(doc.getBoolean("wasCorrect")));
                return q;
            }).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error retrieving all questions: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            firestore.collection(QUESTIONS_COLLECTION)
                    .document(id)
                    .delete()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error deleting question: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(List<String> ids) {
        try {
            List<ApiFuture<WriteResult>> futures = new ArrayList<>();
            for (String id : ids) {
                futures.add(firestore.collection(QUESTIONS_COLLECTION)
                        .document(id)
                        .delete());
            }
            for (ApiFuture<WriteResult> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error deleting questions: " + e.getMessage(), e);
        }
    }

    @Override
    public long count() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(QUESTIONS_COLLECTION).get();
            return future.get().size();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error counting questions: " + e.getMessage(), e);
        }
    }
}

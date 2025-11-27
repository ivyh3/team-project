package repository;

import entity.Question;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of QuestionDataAccess.
 */
public class InMemoryQuestionRepository implements QuestionDataAccess {

    private final Map<String, Question> storage = new ConcurrentHashMap<>();

    @Override
    public Question save(Question question) {
        storage.put(question.getId(), question);
        return question;
    }

    @Override
    public boolean saveAll(List<Question> questions) {
        try {
            for (Question q : questions) {
                storage.put(q.getId(), q);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<Question> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public void deleteAll(List<String> ids) {
        for (String id : ids) {
            storage.remove(id);
        }
    }

    @Override
    public long count() {
        return storage.size();
    }
}

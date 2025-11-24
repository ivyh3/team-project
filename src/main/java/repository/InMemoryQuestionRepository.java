package repository;

import entity.Question;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryQuestionRepository implements QuestionRepository {
    private final Map<String, Question> map = new ConcurrentHashMap<>();

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<Question> findById(String id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Question save(Question entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }
        String id = entity.getId();
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
            try {
                entity.setId(id);
            } catch (Exception ignored) {
                // if entity has no setId, rely on existing id; still put with generated id key
            }
        }
        map.put(id, entity);
        return entity;
    }

    @Override
    public void deleteById(String id) {
        map.remove(id);
    }

    @Override
    public long count() {
        return map.size();
    }
}

package repository;

import entity.Question;
import java.util.List;
import java.util.Optional;

public interface QuestionDataAccess {
    Question save(Question question);
    void saveAll(List<Question> questions);
    Optional<Question> findById(String id);
    List<Question> findAll();
    void deleteById(String id);

    long count();
}

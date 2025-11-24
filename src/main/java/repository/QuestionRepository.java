package repository;

import entity.Question;

public interface QuestionRepository extends DataRepository<Question, String> {
    // add question-specific queries here if needed
}

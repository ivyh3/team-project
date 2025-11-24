package service.impl;

import repository.QuestionRepository;
import service.QuestionService;
import entity.Question;

import java.util.List;
import java.util.Optional;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repository;

    public QuestionServiceImpl(QuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Question> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Question> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Question save(Question entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }
}

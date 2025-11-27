package repository;

import entity.Question;
import java.util.List;
import java.util.Optional;

public interface QuestionDataAccess {

    /**
     * Saves a single question.
     * @param question the question to save
     * @return the saved question with updated fields (e.g., ID)
     */
    Question save(Question question);

    /**
     * Saves a list of questions.
     * @param questions the questions to save
     * @return true if all questions were saved successfully, false otherwise
     */
    boolean saveAll(List<Question> questions);

    /**
     * Finds a question by its ID.
     * @param id the question ID
     * @return an Optional containing the question if found, empty otherwise
     */
    Optional<Question> findById(String id);

    /**
     * Returns all questions.
     * @return a list of all questions, empty if none exist
     */
    List<Question> findAll();

    /**
     * Deletes a question by ID.
     * @param id the ID of the question to delete
     */
    void deleteById(String id);

    /**
     * Deletes multiple questions by their IDs.
     * @param ids the IDs of questions to delete
     */
    void deleteAll(List<String> ids);

    /**
     * Counts the total number of questions.
     * @return the number of questions
     */
    long count();
}

package frameworks_drivers.gemini;

import entity.Question;
import java.util.List;

public interface GeminiQuizDataAccess {
    List<Question> generateQuiz(String prompt, List<String> referenceMaterialTexts) throws Exception;
    List<Question> generateQuizWithFiles(String prompt, List<String> fileUris) throws Exception;
}
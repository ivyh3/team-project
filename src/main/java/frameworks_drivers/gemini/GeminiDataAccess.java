package frameworks_drivers.gemini;

import entity.Question;
import java.util.List;

public interface GeminiDataAccess {
    List<Question> generateQuiz(String prompt, List<String> referenceMaterialTexts) throws Exception;
    List<Question> generateQuizWithFiles(String prompt, List<String> fileUris) throws Exception;
    String uploadFileToGemini(byte[] fileContent, String mimeType, String displayName) throws Exception;
    void deleteFileFromGemini(String fileUri) throws Exception;
    String generateText(String prompt) throws Exception;
}

package frameworks_drivers.gemini;

public interface GeminiFileDataAccess {
    String uploadFileToGemini(byte[] fileContent, String mimeType, String displayName) throws Exception;
    void deleteFileFromGemini(String fileUri) throws Exception;
}
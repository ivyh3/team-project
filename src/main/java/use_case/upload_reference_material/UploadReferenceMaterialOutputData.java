package use_case.upload_reference_material;

public class UploadReferenceMaterialOutputData {
    private final String fileName;
    private final String storagePath;
    private final String prompt;

    public UploadReferenceMaterialOutputData(String fileName, String storagePath, String prompt) {
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.prompt = prompt;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getPrompt() {
        return prompt;
    }
}

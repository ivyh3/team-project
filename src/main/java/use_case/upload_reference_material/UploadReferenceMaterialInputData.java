package use_case.upload_reference_material;

import java.io.File;

/**
 * Input data for the Upload Reference Material use case.
 */
public class UploadReferenceMaterialInputData {
    private final String userId;
    private final File file;
    private final String prompt;
    
    public UploadReferenceMaterialInputData(String userId, File file, String prompt) {
        this.userId = userId;
        this.file = file;
        this.prompt = prompt;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public File getFile() {
        return file;
    }
    
    public String getPrompt() {
        return prompt;
    }
}


package use_case.upload_reference_material;

import java.io.File;
import java.util.Objects;

/**
 * Input data for the Upload Reference Material use case.
 */
public class UploadReferenceMaterialInputData {
    private final String userId;
    private final File file;
    private final String mimeType;
    private final String prompt; // add this

    public UploadReferenceMaterialInputData(String userId, File file, String mimeType, String prompt) {
        this.userId = Objects.requireNonNull(userId);
        this.file = Objects.requireNonNull(file);
        this.mimeType = mimeType; // nullable if optional
        this.prompt = prompt; // store the prompt
    }

    public UploadReferenceMaterialInputData(String userId, File file, String prompt) {
        this(userId, file, null, prompt); // calls the 4-argument constructor
    }

    public String getUserId() { return userId; }
    public File getFile() { return file; }
    public String getMimeType() { return mimeType; }
    public String getPrompt() { return prompt; } // add getter
}

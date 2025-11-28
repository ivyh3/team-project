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

    public UploadReferenceMaterialInputData(String userId, File file, String mimeType) {
        this.userId = Objects.requireNonNull(userId);
        this.file = Objects.requireNonNull(file);
        this.mimeType = mimeType; // nullable if optional
    }

    public String getUserId() { return userId; }
    public File getFile() { return file; }
    public String getMimeType() { return mimeType; }
}


package use_case.upload_reference_material;

/**
 * Output data for the Upload Reference Material use case.
 */
public class UploadReferenceMaterialOutputData {
    private final String filename;
    private final String storagePath;

    public UploadReferenceMaterialOutputData(String filename, String storagePath) {
        this.filename = filename;
        this.storagePath = storagePath;
    }

    public String getFilename() {
        return filename;
    }

    public String getStoragePath() {
        return storagePath;
    }
}

package use_case.upload_reference_material;

/**
 * Output data for the Upload Reference Material use case.
 */
public class UploadReferenceMaterialOutputData {
    private final String materialId;
    private final String filename;

    public UploadReferenceMaterialOutputData(String materialId, String filename) {
        this.materialId = materialId;
        this.filename = filename;
    }

    public String getMaterialId() {
        return materialId;
    }

    public String getFilename() {
        return filename;
    }
}

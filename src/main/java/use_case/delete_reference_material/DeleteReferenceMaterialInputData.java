package use_case.delete_reference_material;

import java.util.List;
import java.util.Objects;

/**
 * Input data for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialInputData {
    private final String userId;
    private final String fileName;

    /**
     * Full constructor with confirmation flag.
     *
     * @param userId    the ID of the user
     * @param fileName  the list of material IDs to delete
     */
    public DeleteReferenceMaterialInputData(String userId, String fileName) {
        this.userId = userId;
        this.fileName = fileName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileName() {
        return fileName;
    }
}

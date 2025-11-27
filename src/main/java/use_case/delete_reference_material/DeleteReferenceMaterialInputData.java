package use_case.delete_reference_material;

import java.util.List;
import java.util.Objects;

/**
 * Input data for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialInputData {
    private final String userId;
    private final List<String> materialIds;
    private final boolean confirmed;

    /**
     * Full constructor with confirmation flag.
     *
     * @param userId      the ID of the user
     * @param materialIds the list of material IDs to delete
     * @param confirmed   whether the deletion is confirmed
     */
    public DeleteReferenceMaterialInputData(String userId, List<String> materialIds, boolean confirmed) {
        this.userId = Objects.requireNonNull(userId);
        this.materialIds = Objects.requireNonNull(materialIds);
        this.confirmed = confirmed;
    }

    /**
     * Overloaded constructor defaults confirmed to true.
     *
     * @param userId      the ID of the user
     * @param materialIds the list of material IDs to delete
     */
    public DeleteReferenceMaterialInputData(String userId, List<String> materialIds) {
        this(userId, materialIds, true);
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getMaterialIds() {
        return materialIds;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

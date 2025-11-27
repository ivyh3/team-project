package use_case.delete_reference_material;

import java.util.List;
import java.util.Collections;

/**
 * Output data for the Delete Reference Material use case.
 * Holds the IDs of successfully deleted materials.
 */
public class DeleteReferenceMaterialOutputData {
    private final List<String> deletedMaterialIds;

    public DeleteReferenceMaterialOutputData(List<String> deletedMaterialIds) {
        // Defensive copy and null safety
        this.deletedMaterialIds = deletedMaterialIds == null
                ? Collections.emptyList()
                : List.copyOf(deletedMaterialIds);
    }

    /**
     * Returns the IDs of the deleted reference materials.
     */
    public List<String> getDeletedMaterialIds() {
        return deletedMaterialIds;
    }
}

package use_case.delete_reference_material;

import java.util.List;

/**
 * Output data for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialOutputData {
    private final List<String> deletedMaterialIds;
    
    public DeleteReferenceMaterialOutputData(List<String> deletedMaterialIds) {
        this.deletedMaterialIds = deletedMaterialIds;
    }
    
    public List<String> getDeletedMaterialIds() {
        return deletedMaterialIds;
    }
}


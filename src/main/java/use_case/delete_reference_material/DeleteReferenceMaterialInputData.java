package use_case.delete_reference_material;

import java.util.List;

/**
 * Input data for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialInputData {
    private final String userId;
    private final List<String> materialIds;
    
    public DeleteReferenceMaterialInputData(String userId, List<String> materialIds) {
        this.userId = userId;
        this.materialIds = materialIds;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public List<String> getMaterialIds() {
        return materialIds;
    }

    public boolean isConfirmed() {
        return false;
    }
}


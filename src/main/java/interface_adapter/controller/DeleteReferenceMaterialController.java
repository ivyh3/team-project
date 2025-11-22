package interface_adapter.controller;

import use_case.delete_reference_material.DeleteReferenceMaterialInputBoundary;
import use_case.delete_reference_material.DeleteReferenceMaterialInputData;

import java.util.List;

/**
 * Controller for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialController {
    private final DeleteReferenceMaterialInputBoundary interactor;

    public DeleteReferenceMaterialController(DeleteReferenceMaterialInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the delete reference material use case.
     * 
     * @param userId      the user ID
     * @param materialIds the material IDs to delete
     */
    public void execute(String userId, List<String> materialIds) {
        DeleteReferenceMaterialInputData inputData = new DeleteReferenceMaterialInputData(
                userId, materialIds);
        interactor.execute(inputData);
    }
}

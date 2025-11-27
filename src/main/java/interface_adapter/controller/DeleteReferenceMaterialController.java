package interface_adapter.controller;

import use_case.delete_reference_material.DeleteReferenceMaterialInputBoundary;
import use_case.delete_reference_material.DeleteReferenceMaterialInputData;

import java.util.List;
import java.util.Objects;

/**
 * Controller for the Delete Reference Material use case.
 * Delegates user requests to the corresponding interactor.
 */
public class DeleteReferenceMaterialController {
    private final DeleteReferenceMaterialInputBoundary interactor;

    /**
     * Constructs the controller with the given interactor.
     *
     * @param interactor the input boundary interactor
     */
    public DeleteReferenceMaterialController(DeleteReferenceMaterialInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    /**
     * Handles a request to delete reference materials.
     *
     * @param userId      the user ID
     * @param materialIds the material IDs to delete
     */
    public void deleteMaterials(String userId, List<String> materialIds) {
        if (userId == null || materialIds == null || materialIds.isEmpty()) return;

        DeleteReferenceMaterialInputData inputData = new DeleteReferenceMaterialInputData(
                userId, materialIds);
        interactor.execute(inputData);
    }
}

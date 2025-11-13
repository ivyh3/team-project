package use_case.delete_reference_material;

import interface_adapter.repository.ReferenceMaterialRepository;
import frameworks_drivers.storage.StorageService;

/**
 * Interactor for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialInteractor implements DeleteReferenceMaterialInputBoundary {
    private final ReferenceMaterialRepository materialRepository;
    private final StorageService storageService;
    private final DeleteReferenceMaterialOutputBoundary outputBoundary;
    
    public DeleteReferenceMaterialInteractor(ReferenceMaterialRepository materialRepository,
                                            StorageService storageService,
                                            DeleteReferenceMaterialOutputBoundary outputBoundary) {
        this.materialRepository = materialRepository;
        this.storageService = storageService;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(DeleteReferenceMaterialInputData inputData) {
        // TODO: Implement the business logic for deleting reference material
        // 1. Confirm deletion with user
        // 2. Delete files from Firebase Storage
        // 3. Delete metadata from repository
        // 4. Prepare success or failure view
    }
}


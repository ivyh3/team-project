package use_case.delete_reference_material;

// import interface_adapter.repository.ReferenceMaterialRepository;
// import frameworks_drivers.storage.StorageService;

import java.util.List;

/**
 * Interactor for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialInteractor implements DeleteReferenceMaterialInputBoundary {
    // private final ReferenceMaterialRepository materialRepository;
    // private final StorageService storageService;
    private final DeleteReferenceMaterialOutputBoundary outputBoundary;

    public DeleteReferenceMaterialInteractor(
            // ReferenceMaterialRepository materialRepository,
            // StorageService storageService,
            DeleteReferenceMaterialOutputBoundary outputBoundary) {
        // this.materialRepository = materialRepository;
        // this.storageService = storageService;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteReferenceMaterialInputData inputData) {
        // // TODO: Implement the business logic for deleting reference material
        // // 1. Confirm deletion with user
        // // 2. Delete files from Firebase Storage
        // // 3. Delete metadata from repository
        // // 4. Prepare success or failure view

        // // Implemented business logic (hard-coded storage paths)
        // // 1. Confirm deletion with user
        // // 2. Delete files from Firebase Storage (hard-coded paths)
        // // 3. Delete metadata from repository
        // // 4. Prepare success or failure view
        // if (inputData == null) {
        // outputBoundary.prepareFailView("Invalid input");
        // return;
        // }

        // // 1. Confirm deletion with user
        // if (!inputData.isConfirmed()) {
        // outputBoundary.prepareFailView("Deletion not confirmed by user");
        // return;
        // }

        // List<String> materialIds = inputData.getMaterialIds();

        // try {
        // // Ensure material exists
        // for (String materialId : materialIds) {
        // var material = materialRepository.getById(inputData.getUserId(), materialId);
        // if (material == null) {
        // outputBoundary.prepareFailView(materialId);
        // return;
        // }

        // }

        // // 2. Delete files from Firebase Storage (hard-coded paths)
        // for (String materialId : materialIds) {
        // String filePath = "reference_materials/" + materialId + ".pdf";
        // String thumbnailPath = "reference_materials/thumbnails/" + materialId +
        // ".png";

        // storageService.deleteFile(filePath);
        // storageService.deleteFile(thumbnailPath);
        // }

        // // 3. Delete metadata from repository
        // for (String materialId : materialIds) {
        // materialRepository.delete(inputData.getUserId(), materialId);
        // }

        // // 4. Prepare success view
        // DeleteReferenceMaterialOutputData outputData = new
        // DeleteReferenceMaterialOutputData(materialIds);
        // outputBoundary.prepareSuccessView(outputData);
        // } catch (Exception e) {
        // outputBoundary.prepareFailView("Failed to delete material: " +
        // e.getMessage());
        // }
    }
}

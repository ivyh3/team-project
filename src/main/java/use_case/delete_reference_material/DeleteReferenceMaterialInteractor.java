package use_case.delete_reference_material;

import repository.ReferenceMaterialRepository;
import frameworks_drivers.storage.StorageService;

import java.util.List;

/**
 * Interactor for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialInteractor implements DeleteReferenceMaterialInputBoundary {
    private final ReferenceMaterialRepository materialRepository;
    private final StorageService storageService;
    private final DeleteReferenceMaterialOutputBoundary outputBoundary;

    public DeleteReferenceMaterialInteractor(
            ReferenceMaterialRepository materialRepository,
            StorageService storageService,
            DeleteReferenceMaterialOutputBoundary outputBoundary) {
        this.materialRepository = materialRepository;
        this.storageService = storageService;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(DeleteReferenceMaterialInputData inputData) {
        if (inputData == null) {
            outputBoundary.prepareFailView("Invalid input");
            return;
        }

        if (!inputData.isConfirmed()) {
            outputBoundary.prepareFailView("Deletion not confirmed by user");
            return;
        }

        List<String> materialIds = inputData.getMaterialIds();
        if (materialIds == null || materialIds.isEmpty()) {
            outputBoundary.prepareFailView("No materials specified for deletion");
            return;
        }

        String userId = inputData.getUserId();

        try {
            // Ensure each material exists
            for (String materialId : materialIds) {
                var material = materialRepository.getById(userId, materialId);
                if (material == null) {
                    outputBoundary.prepareFailView("Material not found: " + materialId);
                    return;
                }
            }

            // Delete files from storage and then delete metadata
            for (String materialId : materialIds) {
                String filePath = "reference_materials/" + materialId + ".pdf";
                String thumbnailPath = "reference_materials/thumbnails/" + materialId + ".png";

                // StorageService may throw; let it be handled by catch below
                storageService.deleteFile(filePath);
                storageService.deleteFile(thumbnailPath);

                // Remove metadata (adapter may return boolean or void; assume void or may throw)
                materialRepository.delete(userId, materialId);
            }

            DeleteReferenceMaterialOutputData outputData = new DeleteReferenceMaterialOutputData(materialIds);
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to delete material: " + e.getMessage());
        }
    }
}

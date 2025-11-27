package use_case.delete_reference_material;

import repository.ReferenceMaterialRepository;
import frameworks_drivers.storage.StorageService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        this.materialRepository = Objects.requireNonNull(materialRepository);
        this.storageService = Objects.requireNonNull(storageService);
        this.outputBoundary = Objects.requireNonNull(outputBoundary);
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
            // Validate all materials exist
            List<String> missingMaterials = materialIds.stream()
                    .filter(id -> materialRepository.getById(userId, id) == null)
                    .collect(Collectors.toList());

            if (!missingMaterials.isEmpty()) {
                outputBoundary.prepareFailView("Materials not found: " + missingMaterials);
                return;
            }

            // Delete files and metadata
            for (String materialId : materialIds) {
                storageService.deleteFile(getFilePath(materialId));
                storageService.deleteFile(getThumbnailPath(materialId));
                materialRepository.delete(userId, materialId);
            }

            DeleteReferenceMaterialOutputData outputData = new DeleteReferenceMaterialOutputData(materialIds);
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to delete material: " + e.getMessage());
        }
    }

    private String getFilePath(String materialId) {
        return "reference_materials/" + materialId + ".pdf";
    }

    private String getThumbnailPath(String materialId) {
        return "reference_materials/thumbnails/" + materialId + ".png";
    }
}

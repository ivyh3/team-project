package interface_adapter.controller;

import use_case.delete_reference_material.DeleteReferenceMaterialInputBoundary;

import java.util.List;

public class DeleteReferenceMaterialController {

    private final DeleteReferenceMaterialInputBoundary inputBoundary;

    public DeleteReferenceMaterialController(DeleteReferenceMaterialInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Deletes multiple reference materials for a user.
     * @param userId The ID of the user.
     * @param files The list of files to delete.
     */
    public void deleteFiles(String userId, List<String> files) {
        validateFileList(files);

        try {
            inputBoundary.delete(userId, files);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete reference materials.", e);
        }
    }

    /**
     * Deletes a single reference material for a user.
     * @param userId The ID of the user.
     * @param fileName The name of the file to delete.
     */
    public void deleteFile(String userId, String fileName) {
        validateFileName(fileName);

        try {
            inputBoundary.delete(userId, List.of(fileName));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete reference material.", e);
        }
    }

    // --- Helper validation methods ---

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or blank.");
        }
    }

    private void validateFileList(List<String> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("File list cannot be null or empty.");
        }
        for (String file : files) {
            validateFileName(file);
        }
    }
}
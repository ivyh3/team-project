package use_case.delete_reference_material;

import java.util.List;

/**
 * Data class to transfer deletion results to the presenter.
 */
public class DeleteReferenceMaterialOutputData {

    private final List<String> deletedFiles;

    public DeleteReferenceMaterialOutputData(List<String> deletedFiles) {
        this.deletedFiles = deletedFiles;
    }

    public List<String> getDeletedFiles() {
        return deletedFiles;
    }
}
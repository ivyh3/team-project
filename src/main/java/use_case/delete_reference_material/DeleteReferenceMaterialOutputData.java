package use_case.delete_reference_material;

import java.util.List;

public class DeleteReferenceMaterialOutputData {
    private final boolean isDeleted;
    private final List<String> deletedFiles;

    public DeleteReferenceMaterialOutputData(boolean isDeleted, List<String> deletedFiles) {
        this.isDeleted = isDeleted;
        this.deletedFiles = deletedFiles;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public List<String> getDeletedFiles() {
        return deletedFiles;
    }
}
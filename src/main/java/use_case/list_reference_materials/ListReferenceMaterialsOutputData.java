package use_case.list_reference_materials;

import java.util.List;

/**
 * Output data for the List Reference Materials use case.
 */
public class ListReferenceMaterialsOutputData {

    private final List<String> fileNames;

    public ListReferenceMaterialsOutputData(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public List<String> getFileNames() {
        return fileNames;
    }
}
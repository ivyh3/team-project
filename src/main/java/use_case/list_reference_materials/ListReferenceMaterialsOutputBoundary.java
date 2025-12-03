package use_case.list_reference_materials;

import java.util.List;

public interface ListReferenceMaterialsOutputBoundary {
    void presentFiles(List<String> fileNames);
    void presentError(String errorMessage);
}
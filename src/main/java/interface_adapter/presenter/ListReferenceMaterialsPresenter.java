package interface_adapter.presenter;

import use_case.list_reference_materials.ListReferenceMaterialsOutputBoundary;

import java.util.List;

public class ListReferenceMaterialsPresenter implements ListReferenceMaterialsOutputBoundary {

    @Override
    public void presentFiles(List<String> fileNames) {
        // Example implementation: print to console or update view model
        System.out.println("Available files: " + fileNames);
    }

    @Override
    public void presentError(String errorMessage) {
        // Example implementation
        System.err.println("Error: " + errorMessage);
    }
}
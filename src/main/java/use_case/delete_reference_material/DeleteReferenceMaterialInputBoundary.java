package use_case.delete_reference_material;

/**
 * Input boundary for the Delete Reference Material use case.
 */
public interface DeleteReferenceMaterialInputBoundary {
    /**
     * Executes the delete reference material use case.
     * @param inputData the input data for deleting reference material
     */
    void execute(DeleteReferenceMaterialInputData inputData);
}


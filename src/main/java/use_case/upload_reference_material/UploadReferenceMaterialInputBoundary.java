package use_case.upload_reference_material;

/**
 * Input boundary for the Upload Reference Material use case.
 */
public interface UploadReferenceMaterialInputBoundary {
    /**
     * Executes the upload reference material use case.
     * 
     * @param inputData the input data for uploading reference material
     */
    void execute(UploadReferenceMaterialInputData inputData);
}

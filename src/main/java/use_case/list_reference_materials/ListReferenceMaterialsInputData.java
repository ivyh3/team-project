package use_case.list_reference_materials;

/**
 * Input data for the List Reference Materials use case.
 */
public class ListReferenceMaterialsInputData {

    private final String userId;

    public ListReferenceMaterialsInputData(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
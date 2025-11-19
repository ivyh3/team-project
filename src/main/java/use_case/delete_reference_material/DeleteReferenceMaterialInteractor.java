package use_case.delete_reference_material;

/**
 * Interactor for the Delete Reference Material use case.
 */
public class DeleteReferenceMaterialInteractor implements DeleteReferenceMaterialInputBoundary {
	private final DeleteReferenceMaterialOutputBoundary outputBoundary;

	public DeleteReferenceMaterialInteractor(DeleteReferenceMaterialOutputBoundary outputBoundary) {
		this.outputBoundary = outputBoundary;
	}

	@Override
	public void execute(DeleteReferenceMaterialInputData inputData) {
		// TODO: Implement the business logic for deleting reference material
		// 1. Confirm deletion with user
		// 2. Delete files from Firebase Storage
		// 3. Delete metadata from repository
		// 4. Prepare success or failure view
	}
}

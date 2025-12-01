package use_case.generate_quiz;

/**
 * Interface for accessing file data.
 */
public interface GenerateQuizFileDataAccessInterface {
	/**
	 * Retrieves the byte array of a file for a given user.
	 *
	 * @param userId   The ID of the user.
	 * @param filename The name of the file.
	 * @return A byte array representing the file's data.
	 */
	byte[] getFileContents(String userId, String filename);
}
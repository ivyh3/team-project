package use_case.start_study_session;

import java.util.List;

/**
 * Data Access Interface for Starting Study Sessions.
 */
public interface StartStudySessionDataAccessInterface {
    List<String> getFilesForUser(String userId);
    /**
     * Return whether the file exists within persistent storage based on the given
     * name.
     *
     * @param userId The user ID
     * @param fileName The name of the file
     * @return whether the file exists.
     */
    boolean fileExistsByName(String userId, String fileName);

    /**
     * Return a list of filenames to file ids from persistent storage.
     *
     * @param userId The user ID
     * @return A list of filenames for files that the user has uploaded.
     */
    List<String> getAllUserFiles(String userId);
}

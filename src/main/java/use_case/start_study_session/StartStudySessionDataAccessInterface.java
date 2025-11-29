package use_case.start_study_session;

import java.util.List;

public interface StartStudySessionDataAccessInterface {
    List<String> getFilesForUser(String userId);
    /**
     * Return whether the file exists within persistent storage based on the given
     * name.
     *
     * @param fileName The name of the file
     * @return whether the file exists.
     */
    public boolean fileExistsByName(String userId, String fileName);

    /**
     * Return a list of filenames to file ids from persistent storage.
     *
     * @return A list of filenames for files that the user has uploaded.
     */
    public List<String> getAllUserFiles(String userId);
}

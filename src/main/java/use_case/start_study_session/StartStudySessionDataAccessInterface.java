package use_case.start_study_session;

import java.util.List;

public interface StartStudySessionDataAccessInterface {
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
    * @return A list with filename keys and name values
    */
    public List<String> getReferenceFileOptions();
}

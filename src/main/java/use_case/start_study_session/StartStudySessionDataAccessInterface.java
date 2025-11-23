package use_case.start_study_session;

import java.util.Map;

public interface StartStudySessionDataAccessInterface {
    /**
     * Return whether the file exists within persistent storage based on the given id.
     *
     * @param id The id of the file
     * @return whether the file exists.
     */
    public boolean fileExistsById(int id);

    /**
     * Return a map of filenames to file ids from persistent storage.
     *
     * @return A map with filename keys and id values
     */
    public Map<String, Integer> getReferenceFileOptions();
}

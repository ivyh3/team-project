package frameworks_drivers.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.StudyQuiz;
import entity.StudySession;
import use_case.end_study_session.EndStudySessionDataAccessInterface;
import use_case.start_study_session.StartStudySessionDataAccessInterface;

/**
 * In memory database for study sessions and quizzes.
 * No multiuser functionality, assumes there's only one user.
 */
public class InMemoryDatabase implements StartStudySessionDataAccessInterface, EndStudySessionDataAccessInterface {

    private final Map<String, StudySession> sessionTable;
    private final Map<String, StudyQuiz> quizTable;
    // Primary key for sessions
    private int sessionIdKey;
    // Primary key for quizzes.
    private int quizIdKey;

    // The keys are the filenames. The values are nothing
    private Map<String, String> fileStore;

    public InMemoryDatabase() {
        sessionTable = new HashMap<>();
        quizTable = new HashMap<>();
        sessionIdKey = 0;
        quizIdKey = 0;
        fileStore = new HashMap<>();
    }

    public InMemoryDatabase(Map<String, String> initialFiles) {
        sessionTable = new HashMap<>();
        quizTable = new HashMap<>();
        fileStore = initialFiles;
    }

    public String uploadFile(String fileName, String file) {
        fileStore.put(fileName, file);
        return file;
    }

    public String getFile(String fileName) {
        return fileStore.get(fileName);
    }

    public String deleteFile(String fileName) {
        return fileStore.remove(fileName);
    }

    public Map<String, StudySession> getSessionTable() {
        return sessionTable;
    }

    public Map<String, StudyQuiz> getQuizTable() {
        return quizTable;
    }

    public StudySession addStudySession(String userId, StudySession studySession) {
        studySession.setId(String.valueOf(sessionIdKey));
        sessionTable.put(String.valueOf(sessionIdKey), studySession);
        sessionIdKey++;

        return studySession;
    }

    public List<StudySession> getStudySessions() {
        return new ArrayList<>(sessionTable.values());
    }

    public StudySession getStudySessionById(String userId, String sessionId) {
        return sessionTable.get(sessionId);
    }

    public StudySession updateStudySession(String sessionId, StudySession studySession) {
        sessionTable.put(sessionId, studySession);

        return studySession;
    }

    public StudySession deleteStudySession(String sessionId) {
        return sessionTable.remove(sessionId);
    }

    public List<StudySession> getStudyQuizzes() {
        return new ArrayList<>(sessionTable.values());
    }

    public StudyQuiz addStudyQuiz(StudyQuiz studyQuiz) {
        studyQuiz.setId(String.valueOf(quizIdKey));
        quizTable.put(String.valueOf(quizIdKey), studyQuiz);
        quizIdKey++;
        return studyQuiz;
    }

    public StudyQuiz getStudyQuiz(String quizId) {
        return quizTable.get(quizId);
    }

    public StudyQuiz updateStudyQuiz(String quizId, StudyQuiz studyQuiz) {
        quizTable.put(quizId, studyQuiz);
        return studyQuiz;
    }

    public StudyQuiz removeStudyQuiz(String quizId) {
        return quizTable.remove(quizId);
    }

    @Override
    public boolean fileExistsByName(String userId, String fileName) {
        return fileStore.containsKey(fileName);
    }

    @Override
    public List<String> getAllUserFiles(String userId) {
        return new ArrayList<>(fileStore.keySet());
    }
}

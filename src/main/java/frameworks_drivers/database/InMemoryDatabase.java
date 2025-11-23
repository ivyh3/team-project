package frameworks_drivers.database;

import entity.StudyQuiz;
import entity.StudySession;
import use_case.end_study_session.EndStudySessionDataAccessInterface;
import use_case.start_study_session.StartStudySessionDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In memory database for study sessions and quizzes.
 * <p>
 * No multiuser functionality, assumes there's one logged in already.
 */
public class InMemoryDatabase implements StartStudySessionDataAccessInterface, EndStudySessionDataAccessInterface {

    private final Map<Integer, StudySession> sessionTable;
    private final Map<Integer, StudyQuiz> scoreTable;
    private int sessionIdKey = 0; // Primary key for sessions
    private int quizIdKey = 0; // Primary key for quizzes.

    private Map<String, String> fileStore;

    public InMemoryDatabase() {
        sessionTable = new HashMap<>();
        scoreTable = new HashMap<>();

        // Temporary removal until dummy file data in the application can be removed.
        // fileStore = new HashMap<>();
        fileStore = Map.of(
                "mat223.pdf", "mat223.pdf",
                "longer_textbook_name_adfasdf.pdf", "longer_textbook_name_adfasdf.pdf",
                "csc222.pdf", "csc222.pdf",
                "pdf.pdf", "pdf.pdf"
        );
    }

    public InMemoryDatabase(Map<String, String> initialFiles) {
        sessionTable = new HashMap<>();
        scoreTable = new HashMap<>();
        fileStore = initialFiles;
    }

    // TODO: detemrine how we are using files/file entity class
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

    public Map<Integer, StudySession> getSessionTable() {
        return sessionTable;
    }

    public Map<Integer, StudyQuiz> getScoreTable() {
        return scoreTable;
    }

    public StudySession addStudySession(StudySession studySession) {
        studySession.setId(String.valueOf(sessionIdKey));
        sessionTable.put(sessionIdKey, studySession);
        sessionIdKey++;

        return studySession;
    }

    public List<StudySession> getStudySessions() {
        return new ArrayList<>(sessionTable.values());
    }

    public StudySession getStudySession(int sessionId) {
        return sessionTable.get(sessionId);
    }

    public StudySession updateStudySession(int sessionId, StudySession studySession) {
        sessionTable.put(sessionId, studySession);

        return studySession;
    }

    public StudySession deleteStudySession(int sessionId) {
        return sessionTable.remove(sessionId);
    }

    public List<StudySession> getStudyQuizzes() {
        return new ArrayList<>(sessionTable.values());
    }

    public StudyQuiz addStudyQuiz(StudyQuiz studyQuiz) {
        studyQuiz.setId(String.valueOf(quizIdKey));
        scoreTable.put(quizIdKey, studyQuiz);
        quizIdKey++;
        return studyQuiz;
    }

    public StudyQuiz getStudyQuiz(int quizId) {
        return scoreTable.get(quizId);
    }

    public StudyQuiz updateStudyQuiz(int quizId, StudyQuiz studyQuiz) {
        scoreTable.put(quizId, studyQuiz);
        return studyQuiz;
    }

    public StudyQuiz removeStudyQuiz(int quizId) {
        return scoreTable.remove(quizId);
    }

    @Override
    public boolean fileExistsByName(String fileName) {
        return fileStore.containsKey(fileName);
    }

    @Override
    public List<String> getReferenceFileOptions() {
        return new ArrayList<>(fileStore.keySet());
    }
}

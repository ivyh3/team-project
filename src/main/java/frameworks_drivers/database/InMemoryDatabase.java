package frameworks_drivers.database;

import entity.StudyQuiz;
import entity.StudySession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In memory database for study sessions and quizzes.
 *
 * No multiuser functionality, assumes there's one logged in already.
 */
public class InMemoryDatabase {

    private final Map<Integer, StudySession> sessionTable;
    private final Map<Integer, StudyQuiz> scoreTable;
    private int sessionIdKey = 0; // Primary key for sessions
    private int quizIdKey = 0; // Primary key for quizzes.

    public InMemoryDatabase() {
        sessionTable = new HashMap<>();
        scoreTable = new HashMap<>();
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
}

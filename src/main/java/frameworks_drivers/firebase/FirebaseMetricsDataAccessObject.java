package frameworks_drivers.firebase;

import entity.User;
import interface_adapter.view_model.DashboardState;
import entity.StudySession;
import entity.StudyQuiz;
import entity.Question;
import use_case.view_study_metrics.ViewStudyMetricsDataAccessInterface;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

//TODO: replace fake data and implement the getSessionsPerWeek and getQuizzesPerWeek methods from the DAI

/**
 * Temporary mock implementation of ViewStudyMetricsDataAccessInterface.
 * Returns dummy data for testing.
 */
public class FirebaseMetricsDataAccessObject implements ViewStudyMetricsDataAccessInterface {

    private FirebaseStudySessionDataAccessObject sessionDAO;
    private FirebaseStudyQuizDataAccessObject quizDAO;

    public FirebaseMetricsDataAccessObject(FirebaseStudySessionDataAccessObject sessionDAO,
            FirebaseStudyQuizDataAccessObject quizDAO) {
        this.sessionDAO = sessionDAO;
        this.quizDAO = quizDAO;
    }

    @Override
    public List<StudySession> getSessionsPerWeek(User user, LocalDateTime currentTime, String courseId) {
        // Get date range between today and a week ago.
        LocalDateTime weekAgo = currentTime.minusWeeks(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime today = currentTime.truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);

        List<StudySession> sessions = sessionDAO.getStudySessionsInRange(DashboardState.userId, weekAgo, today);

        return sessions;
    }

    @Override
    public List<StudyQuiz> getQuizzesPerWeek(User user, LocalDateTime currentTime, String courseId) {
        // Get date range between today and a week ago.
        LocalDateTime weekAgo = currentTime.minusWeeks(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime today = currentTime.truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);

        List<StudyQuiz> quizzes = quizDAO.getStudyQuizzesInRange(DashboardState.userId, weekAgo, today);

        return quizzes;
    }
}

// @Override
// public List<StudySession> getSessionsPerWeek(User user, LocalDateTime week,
// String courseId) {
// String weekKey = getWeekKey(week);
// List<StudySession> sessions = getSessionsForWeek(weekKey, week,
// getUserId(user));

// // Filter by courseId if not "all"
// if (!courseId.equals("all")) {
// sessions.removeIf(s -> !s.getCourseId().equals(courseId));
// }

// return sessions;
// }

// @Override
// public List<StudyQuiz> getQuizzesPerWeek(User user, LocalDateTime week,
// String courseId) {
// String weekKey = getWeekKey(week);
// List<StudyQuiz> quizzes = getQuizzesForWeek(weekKey, week, getUserId(user));

// // Filter by courseId if not "all"
// if (!courseId.equals("all")) {
// quizzes.removeIf(q -> !q.getCourseId().equals(courseId));
// }

// return quizzes;
// }

// /**
// * Determines which week key to use based on the requested week.
// */
// private String getWeekKey(LocalDateTime requestedWeek) {
// LocalDateTime now = LocalDateTime.now();

// // Find start of current week (Sunday)
// int dayOfWeek = now.getDayOfWeek().getValue();
// int daysToSubtract = (dayOfWeek == 7) ? 0 : dayOfWeek;
// LocalDateTime currentWeekStart =
// now.minusDays(daysToSubtract).truncatedTo(ChronoUnit.DAYS);

// // Calculate difference in weeks
// long weeksDiff =
// ChronoUnit.WEEKS.between(requestedWeek.truncatedTo(ChronoUnit.DAYS),
// currentWeekStart);

// // Map to our available data
// if (weeksDiff == 0) return "0"; // Current week
// if (weeksDiff == 1) return "-1"; // Last week
// if (weeksDiff == 2) return "-2"; // Two weeks ago

// // For any other week, return empty data
// return "unknown";
// }

// /**
// * Get sessions for a specific week, with dates adjusted to that week.
// */
// private List<StudySession> getSessionsForWeek(String weekKey, LocalDateTime
// week, String userId) {
// List<StudySession> sessions = new ArrayList<>();

// switch (weekKey) {
// case "0":
// // Sun: 30 min, Mon: 60 min, Tue: 120 min, Wed: 90 min, Thu: 120 min, Fri: 0
// min, Sat: 90 min
// sessions.add(createSession("w0_sun_1", userId, "Calculus with Proofs", week,
// 0, 30));
// sessions.add(createSession("w0_mon_1", userId, "Linear Algebra 1", week, 1,
// 60));
// sessions.add(createSession("w0_tue_1", userId, "Calculus with Proofs", week,
// 2, 60));
// sessions.add(createSession("w0_tue_2", userId, "Literature and the Sciences",
// week, 2, 60));
// sessions.add(createSession("w0_wed_1", userId, "Calculus with Proofs", week,
// 3, 90));
// sessions.add(createSession("w0_thu_1", userId, "Linear Algebra 1", week, 4,
// 120));
// sessions.add(createSession("w0_sat_1", userId, "Calculus with Proofs", week,
// 6, 90));
// break;

// case "-1":
// // Sun: 45 min, Mon: 90 min, Tue: 180 min, Wed: 120 min, Thu: 150 min, Fri:
// 60 min, Sat: 105 min
// sessions.add(createSession("w-1_sun_1", userId, "Calculus with Proofs", week,
// 0, 45));
// sessions.add(createSession("w-1_mon_1", userId, "Linear Algebra 1", week, 1,
// 90));
// sessions.add(createSession("w-1_tue_1", userId, "Calculus with Proofs", week,
// 2, 90));
// sessions.add(createSession("w-1_tue_2", userId, "Literature and the
// Sciences", week, 2, 90));
// sessions.add(createSession("w-1_wed_1", userId, "Calculus with Proofs", week,
// 3, 120));
// sessions.add(createSession("w-1_thu_1", userId, "Linear Algebra 1", week, 4,
// 150));
// sessions.add(createSession("w-1_fri_1", userId, "Literature and the
// Sciences", week, 5, 60));
// sessions.add(createSession("w-1_sat_1", userId, "Calculus with Proofs", week,
// 6, 105));
// break;

// case "-2":
// // Sun: 15 min, Mon: 30 min, Tue: 60 min, Wed: 45 min, Thu: 30 min, Fri: 0
// min, Sat: 60 min
// sessions.add(createSession("w-2_sun_1", userId, "Linear Algebra 1", week, 0,
// 15));
// sessions.add(createSession("w-2_mon_1", userId, "Literature and the
// Sciences", week, 1, 30));
// sessions.add(createSession("w-2_tue_1", userId, "Calculus with Proofs", week,
// 2, 60));
// sessions.add(createSession("w-2_wed_1", userId, "Linear Algebra 1", week, 3,
// 45));
// sessions.add(createSession("w-2_thu_1", userId, "Literature and the
// Sciences", week, 4, 30));
// sessions.add(createSession("w-2_sat_1", userId, "Calculus with Proofs", week,
// 6, 60));
// break;
// }

// return sessions;
// }

// /**
// * Get quizzes for a specific week, with dates adjusted to that week.
// */
// private List<StudyQuiz> getQuizzesForWeek(String weekKey, LocalDateTime
// weekStart, String userId) {
// List<StudyQuiz> quizzes = new ArrayList<>();

// switch (weekKey) {
// case "0":
// // scores: 80%, 70%, 85%, 90%, 75%, 0%, 95%
// quizzes.add(createQuiz("w0_sun_q1", userId, "Calculus with Proofs",
// weekStart, 0, 80f));
// quizzes.add(createQuiz("w0_mon_q1", userId, "Linear Algebra 1", weekStart, 1,
// 70f));
// quizzes.add(createQuiz("w0_tue_q1", userId, "Calculus with Proofs",
// weekStart, 2, 85f));
// quizzes.add(createQuiz("w0_wed_q1", userId, "Calculus with Proofs",
// weekStart, 3, 90f));
// quizzes.add(createQuiz("w0_thu_q1", userId, "Linear Algebra 1", weekStart, 4,
// 75f));
// quizzes.add(createQuiz("w0_sat_q1", userId, "Calculus with Proofs",
// weekStart, 6, 95f));
// break;

// case "-1":
// // scores: 85%, 88%, 92%, 95%, 90%, 87%, 93%
// quizzes.add(createQuiz("w-1_sun_q1", userId, "Calculus with Proofs",
// weekStart, 0, 85f));
// quizzes.add(createQuiz("w-1_mon_q1", userId, "Linear Algebra 1", weekStart,
// 1, 88f));
// quizzes.add(createQuiz("w-1_tue_q1", userId, "Calculus with Proofs",
// weekStart, 2, 92f));
// quizzes.add(createQuiz("w-1_wed_q1", userId, "Calculus with Proofs",
// weekStart, 3, 95f));
// quizzes.add(createQuiz("w-1_thu_q1", userId, "Linear Algebra 1", weekStart,
// 4, 90f));
// quizzes.add(createQuiz("w-1_fri_q1", userId, "Literature and the Sciences",
// weekStart, 5, 87f));
// quizzes.add(createQuiz("w-1_sat_q1", userId, "Calculus with Proofs",
// weekStart, 6, 93f));
// break;

// case "-2":
// // scores: 65%, 68%, 72%, 70%, 66%, 0%, 75%
// quizzes.add(createQuiz("w-2_sun_q1", userId, "Linear Algebra 1", weekStart,
// 0, 65f));
// quizzes.add(createQuiz("w-2_mon_q1", userId, "Literature and the Sciences",
// weekStart, 1, 68f));
// quizzes.add(createQuiz("w-2_tue_q1", userId, "Calculus with Proofs",
// weekStart, 2, 72f));
// quizzes.add(createQuiz("w-2_wed_q1", userId, "Linear Algebra 1", weekStart,
// 3, 70f));
// quizzes.add(createQuiz("w-2_thu_q1", userId, "Literature and the Sciences",
// weekStart, 4, 66f));
// quizzes.add(createQuiz("w-2_sat_q1", userId, "Calculus with Proofs",
// weekStart, 6, 75f));
// break;
// }

// return quizzes;
// }

// /**
// * Helper to create a study session for a specific week and day.
// * dayOffset: 0=Sun, 1=Mon, 2=Tue, 3=Wed, 4=Thu, 5=Fri, 6=Sat
// */
// private StudySession createSession(String id, String userId, String courseId,
// LocalDateTime weekStart, int dayOffset, int durationMinutes) {
// LocalDateTime startTime =
// weekStart.plusDays(dayOffset).withHour(10).withMinute(0);

// StudySession session = new StudySession(id, userId, courseId, startTime);
// session.setDuration(Duration.ofMinutes(durationMinutes));
// session.setEndTime(startTime.plus(Duration.ofMinutes(durationMinutes)));
// session.setStatus("completed");

// return session;
// }

// /**
// * Helper to create a quiz for a specific week and day.
// */
// private StudyQuiz createQuiz(String id, String userId, String courseId,
// LocalDateTime weekStart, int dayOffset, float score) {
// LocalDateTime startDate =
// weekStart.plusDays(dayOffset).withHour(14).withMinute(0);

// StudyQuiz quiz = new StudyQuiz(
// id,
// userId,
// "session_" + id,
// new ArrayList<>(),
// courseId,
// "Test quiz for " + courseId,
// List.of("material_1", "material_2")
// );

// quiz.setScore(score);
// quiz.setStartDate(startDate);
// quiz.setDuration(Duration.ofMinutes(15));
// quiz.setEndDate(startDate.plus(Duration.ofMinutes(15)));

// return quiz;
// }

// /**
// * Helper to get user ID.
// */
// private String getUserId(User user) {
// return user != null ? user.getUserId() : "test_user";
// }
// }
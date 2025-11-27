 package use_case.view_study_metrics;

 import entity.StudyQuiz;
 import entity.StudySession;
 import entity.User;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.ArgumentCaptor;

 import java.time.DayOfWeek;
 import java.time.Duration;
 import java.time.LocalDateTime;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertNull;
 import static org.mockito.Mockito.*;

 public class StudyMetricsInteractorTest {
     private ViewStudyMetricsInteractor interactor;
     private ViewStudyMetricsDataAccessInterface metricsDAO;
     private ViewStudyMetricsOutputBoundary presenter;
     private User user;

     @BeforeEach
     void setUp() {
         metricsDAO = mock(ViewStudyMetricsDataAccessInterface.class);
         presenter = mock(ViewStudyMetricsOutputBoundary.class);
         interactor = new ViewStudyMetricsInteractor(metricsDAO, presenter);
         user = new User("userid", "name@gmail.com", LocalDateTime.now());
     }

     @Test
     // check durations make sense when there are multiple sessions in a day
     void dailyStudyDurationsMultipleSessionsTest() {
         LocalDateTime Sunday = LocalDateTime.of(2025, 11, 2, 0, 0);
         List<StudySession> testSessions = new ArrayList<>();

         // sunday
         testSessions.add(createTestSession("s1", Sunday.plusDays(0), Sunday.plusDays(0).plusMinutes(30)));
         testSessions.add(createTestSession("s2", Sunday.plusDays(0), Sunday.plusDays(0).plusMinutes(30)));

         // monday
         testSessions.add(createTestSession("s3", Sunday.plusDays(1), Sunday.plusDays(1).plusMinutes(90)));

         // tuesday
         testSessions.add(createTestSession("s4", Sunday.plusDays(2), Sunday.plusDays(2).plusMinutes(120)));

         when(metricsDAO.getSessionsPerWeek(user.getUserId(), Sunday))
                 .thenReturn(testSessions);
         when(metricsDAO.getQuizzesPerWeek(user.getUserId(), Sunday))
                 .thenReturn(new ArrayList<>());

         ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(user.getUserId(), Sunday);
         interactor.execute(inputData);

         ArgumentCaptor<ViewStudyMetricsOutputData> captor =
                 ArgumentCaptor.forClass(ViewStudyMetricsOutputData.class);
         verify(presenter).prepareSuccessView(captor.capture());

         ViewStudyMetricsOutputData outputData = captor.getValue();
         Map<DayOfWeek, Duration> dailyDurations = outputData.getDailyStudyDurations();

         assertEquals(Duration.ofMinutes(60), dailyDurations.get(DayOfWeek.SUNDAY));
         assertEquals(Duration.ofMinutes(90), dailyDurations.get(DayOfWeek.MONDAY));
         assertEquals(Duration.ofMinutes(120), dailyDurations.get(DayOfWeek.TUESDAY));
         assertNull(dailyDurations.get(DayOfWeek.WEDNESDAY));

     }

     void dailyStudyDurationsEmptyTest() {
         // TODO: write the test, should be zero and empty
     }

     @Test
     // check average scores make sense when there are multiple quizzes in a day
     void averageQuizScoresMultipleQuizzesTest() {
         LocalDateTime Sunday = LocalDateTime.of(2025, 11, 2, 0, 0);
         List<StudyQuiz> testQuizzes = new ArrayList<>();

         // sunday: average 85%
         testQuizzes.add(createTestQuiz("q1", 0.8f, Sunday.plusDays(0), Sunday.plusDays(0)));
         testQuizzes.add(createTestQuiz("q2", 0.9f, Sunday.plusDays(0), Sunday.plusDays(0)));

         // monday: average 70%
         testQuizzes.add(createTestQuiz("q3", 0.7f, Sunday.plusDays(1), Sunday.plusDays(1)));

         when(metricsDAO.getSessionsPerWeek(user.getUserId(), Sunday))
                 .thenReturn(new ArrayList<>()); // Empty sessions for this test
         when(metricsDAO.getQuizzesPerWeek(user.getUserId(), Sunday))
                 .thenReturn(testQuizzes);

         ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(user.getUserId(), Sunday);
         interactor.execute(inputData);

         ArgumentCaptor<ViewStudyMetricsOutputData> captor =
                 ArgumentCaptor.forClass(ViewStudyMetricsOutputData.class);
         verify(presenter).prepareSuccessView(captor.capture());

         ViewStudyMetricsOutputData outputData = captor.getValue();
         Map<DayOfWeek, Float> averageScores = outputData.getAverageQuizScores();

         assertEquals(85f, averageScores.get(DayOfWeek.SUNDAY), 0.01);
         assertEquals(70f, averageScores.get(DayOfWeek.MONDAY), 0.01);
         assertNull(averageScores.get(DayOfWeek.TUESDAY));
     }

     void averageQuizScoresEmptyTest() {
         // TODO: write the test, should be zero and empty
     }


     // helpers
     private StudySession createTestSession(String id, LocalDateTime startTime, LocalDateTime endTime) {
         return new StudySession(id, startTime, endTime);
     }

     private StudyQuiz createTestQuiz(String id, float score, LocalDateTime startTime, LocalDateTime endTime) {
         return new StudyQuiz(
                 id,
                 score,
                 startTime,
                 endTime
         );
     }

 }

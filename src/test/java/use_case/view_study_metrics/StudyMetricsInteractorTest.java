 package use_case.view_study_metrics;

 import entity.*;
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

     // check durations make sense when there are multiple sessions, one session, or zero sessions in a day
     @Test
     void dailyStudyDurationsTest() {
         StudySessionFactory studySessionFactory = new StudySessionFactory();
         LocalDateTime sunday = LocalDateTime.of(2025, 11, 2, 0, 0);
         List<StudySession> testSessions = new ArrayList<>();

         testSessions.add(studySessionFactory.create("s1", sunday.plusDays(0), sunday.plusDays(0).plusMinutes(30)));
         testSessions.add(studySessionFactory.create("s2", sunday.plusDays(0), sunday.plusDays(0).plusMinutes(30)));
         testSessions.add(studySessionFactory.create("s3", sunday.plusDays(1), sunday.plusDays(1).plusMinutes(90)));

         when(metricsDAO.getSessionsPerWeek(user.getUserId(), sunday))
                 .thenReturn(testSessions);
         when(metricsDAO.getQuizzesPerWeek(user.getUserId(), sunday))
                 .thenReturn(new ArrayList<>());

         ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(user.getUserId(), sunday);
         interactor.execute(inputData);

         ArgumentCaptor<ViewStudyMetricsOutputData> captor =
                 ArgumentCaptor.forClass(ViewStudyMetricsOutputData.class);
         verify(presenter).prepareSuccessView(captor.capture());

         ViewStudyMetricsOutputData outputData = captor.getValue();
         Map<DayOfWeek, Duration> dailyDurations = outputData.getDailyStudyDurations();

         assertEquals(Duration.ofMinutes(60), dailyDurations.get(DayOfWeek.SUNDAY));
         assertEquals(Duration.ofMinutes(90), dailyDurations.get(DayOfWeek.MONDAY));
         assertNull(dailyDurations.get(DayOfWeek.WEDNESDAY));
     }

     // check average scores make sense when there are multiple quizzes, one quiz, or zero quizzes in a day
     @Test
     void averageQuizScoresTest() {
         StudyQuizFactory studyQuizFactory = new StudyQuizFactory();
         LocalDateTime sunday = LocalDateTime.of(2025, 11, 2, 0, 0);
         List<StudyQuiz> testQuizzes = new ArrayList<>();

         testQuizzes.add(studyQuizFactory.create("q1", 0.8f, sunday.plusDays(0), sunday.plusDays(0)));
         testQuizzes.add(studyQuizFactory.create("q2", 0.9f, sunday.plusDays(0), sunday.plusDays(0)));
         testQuizzes.add(studyQuizFactory.create("q3", 0.7f, sunday.plusDays(1), sunday.plusDays(1)));

         when(metricsDAO.getSessionsPerWeek(user.getUserId(), sunday))
                 .thenReturn(new ArrayList<>());
         when(metricsDAO.getQuizzesPerWeek(user.getUserId(), sunday))
                 .thenReturn(testQuizzes);

         ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(user.getUserId(), sunday);
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
 }

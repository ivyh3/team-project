package entity;

public class StudyQuizFactory {
	public StudyQuiz create(String id, float score, java.time.LocalDateTime startDate,
			java.time.LocalDateTime endDate) {
		return new StudyQuiz(id, score, startDate, endDate);
	}

	public StudyQuiz create(float score, java.time.LocalDateTime startDate,
			java.time.LocalDateTime endDate) {
		return new StudyQuiz(null, score, startDate, endDate);
	}
}

package use_case.generate_quiz;

import java.util.List;

/**
 * Input data for the Generate Quiz use case.
 */
public class GenerateQuizInputData {
	private final String userId;
	private final String sessionId;
	private final String courseId;
	private final String prompt;
	private final List<String> referenceMaterialIds;

	public GenerateQuizInputData(String userId, String sessionId, String courseId,
			String prompt, List<String> referenceMaterialIds) {
		this.userId = userId;
		this.sessionId = sessionId;
		this.courseId = courseId;
		this.prompt = prompt;
		this.referenceMaterialIds = referenceMaterialIds;
	}

	public String getUserId() {
		return userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getPrompt() {
		return prompt;
	}

	public List<String> getReferenceMaterialIds() {
		return referenceMaterialIds;
	}
}

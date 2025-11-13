package entity;

/**
 * Represents a course in the AI Study Companion application.
 */
public class Course {
	private String id;
	private String code;
	private String title;

	public Course(String id, String code, String title) {
		this.id = id;
		this.code = code;
		this.title = title;
	}

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

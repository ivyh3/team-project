package use_case.upload_reference_material;

import java.io.File;
import java.io.InputStream;

public class UploadReferenceMaterialInputData {

    private String courseCode;
    private String title;
    private File file;        // Local file
    private InputStream fileStream; // Stream content

    // Constructor for File
    public UploadReferenceMaterialInputData(String courseCode, String title, File file) {
        setCourseCode(courseCode);
        setTitle(title);

        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("file cannot be null and must exist");
        }
        this.file = file;
        this.fileStream = null; // Ensure only one is used
    }

    // Constructor for InputStream
    public UploadReferenceMaterialInputData(String courseCode, String title, InputStream fileStream) {
        setCourseCode(courseCode);
        setTitle(title);

        if (fileStream == null) {
            throw new IllegalArgumentException("fileStream cannot be null");
        }
        this.fileStream = fileStream;
        this.file = null; // Ensure only one is used
    }

    // Getters and Setters with validation
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        if (courseCode == null || courseCode.isEmpty()) {
            throw new IllegalArgumentException("courseCode cannot be null or empty");
        }
        this.courseCode = courseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title cannot be null or empty");
        }
        this.title = title;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("file cannot be null and must exist");
        }
        this.file = file;
        this.fileStream = null; // Clear stream if switching to file
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        if (fileStream == null) {
            throw new IllegalArgumentException("fileStream cannot be null");
        }
        this.fileStream = fileStream;
        this.file = null; // Clear file if switching to stream
    }

    // Helper methods
    public boolean isFileBased() {
        return file != null;
    }

    public boolean isStreamBased() {
        return fileStream != null;
    }

    @Override
    public String toString() {
        return "UploadReferenceMaterialInputData{" +
                "courseCode='" + courseCode + '\'' +
                ", title='" + title + '\'' +
                ", file=" + (file != null ? file.getName() : "null") +
                ", fileStream=" + (fileStream != null ? "provided" : "null") +
                '}';
    }
}
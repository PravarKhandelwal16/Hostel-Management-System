import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a complaint made by a student.
 * This class stores details about the complaint, who filed it, and its status.
 */
public class Complaint implements Serializable {
    private String studentRollNumber;
    private String description;
    private LocalDate dateFiled;
    private boolean isResolved;

    // Constructor to initialize a Complaint object
    public Complaint(String studentRollNumber, String description) {
        this.studentRollNumber = studentRollNumber;
        this.description = description;
        this.dateFiled = LocalDate.now();
        this.isResolved = false; // A new complaint is unresolved by default
    }

    // Getters and setters
    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateFiled() {
        return dateFiled;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    @Override
    public String toString() {
        return "Complaint from " + studentRollNumber + " on " + dateFiled + ": '" + description + "' (Resolved: " + isResolved + ")";
    }
}

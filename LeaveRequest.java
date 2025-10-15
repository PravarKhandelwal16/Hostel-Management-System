import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a leave request made by a student.
 */
public class LeaveRequest implements Serializable {
    public enum Status { PENDING, APPROVED, DENIED }

    private String studentRollNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private Status status;

    public LeaveRequest(String studentRollNumber, LocalDate startDate, LocalDate endDate, String reason) {
        this.studentRollNumber = studentRollNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = Status.PENDING; // Default status for any new request
    }

    // Getters
    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public Status getStatus() {
        return status;
    }

    // Setter
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LeaveRequest from " + studentRollNumber + " (" + status + ")";
}
}
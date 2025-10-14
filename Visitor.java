import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a visitor to the hostel.
 * This class logs visitor details and their entry/exit times.
 */
public class Visitor implements Serializable {
    private String visitorName;
    private String studentRollToVisit;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    // Constructor for a new visitor entry
    public Visitor(String visitorName, String studentRollToVisit) {
        this.visitorName = visitorName;
        this.studentRollToVisit = studentRollToVisit;
        this.entryTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getVisitorName() {
        return visitorName;
    }

    public String getStudentRollToVisit() {
        return studentRollToVisit;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    // Method to log exit time
    public void logExit() {
        this.exitTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String exitStr = (exitTime == null) ? "Still in hostel" : exitTime.toString();
        return "Visitor: " + visitorName + ", To Visit: " + studentRollToVisit + ", Entry: " + entryTime + ", Exit: " + exitStr;
    }
}

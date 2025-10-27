import java.time.LocalDate;
// Removed: import java.util.ArrayList;
// Removed: import java.util.List;

/**
 * This class now acts as a "session manager" to hold the
 * currently logged-in student and other non-persistent data.
 */
public class Hostel {

    // Basic Hostel status variables
    private double thisMonthFoodWastage = 120.5;
    private double lastMonthFoodWastage = 120.0;
    private boolean cleaningDoneToday = false;
    private LocalDate lastConfirmationDate = null;

    // --- REMOVED STUDENT LIST ---
    // private List<Student> students;

    // --- NEW FIELD ---
    // The currently logged-in student
    private Student loggedInStudent;

    public Hostel() {
        // Constructor is now empty. All students are in the database.
    }

    // --- REMOVED METHOD ---
    // public List<Student> getAllStudents() { ... }

    // --- NEW Session Methods ---
    public void setLoggedInStudent(Student student) {
        this.loggedInStudent = student;
    }

    public Student getLoggedInStudent() {
        return loggedInStudent;
    }

    // --- Meal Plan Logic ---

    public boolean isMealPlanConfirmed() {
        // Button is disabled only if confirmed TODAY
        return lastConfirmationDate != null && lastConfirmationDate.isEqual(LocalDate.now());
    }

    public void setMealPlanConfirmed(boolean confirmed) {
        if (confirmed) {
            this.lastConfirmationDate = LocalDate.now();
        } else {
            this.lastConfirmationDate = null;
        }
    }

    // --- Cleaning Status Logic ---

    public boolean isCleaningDoneToday() {
        return cleaningDoneToday;
    }

    public void setCleaningDoneToday(boolean cleaningDoneToday) {
        this.cleaningDoneToday = cleaningDoneToday;
    }

    // --- Food Wastage Logic ---

    public double getThisMonthFoodWastage() {
        return thisMonthFoodWastage;
    }

    public void setThisMonthFoodWastage(double thisMonthFoodWastage) {
        // Ensure wastage doesn't go below zero
        if (thisMonthFoodWastage >= 0) {
            this.thisMonthFoodWastage = thisMonthFoodWastage;
        } else {
            this.thisMonthFoodWastage = 0;
        }
    }

    public double getFoodWastageChangePercent() {
        if (lastMonthFoodWastage == 0) return 0;
        double change = thisMonthFoodWastage - lastMonthFoodWastage;
        return (change / lastMonthFoodWastage) * 100;
    }
}

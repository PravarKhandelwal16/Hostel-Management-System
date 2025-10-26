import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hostel {
    
    // Basic Hostel status variables
    private double thisMonthFoodWastage = 120.5; 
    private double lastMonthFoodWastage = 120.0; 
    private boolean cleaningDoneToday = false;
    private LocalDate lastConfirmationDate = null;
    
    // Student Data
    private List<Student> students;

    public Hostel() {
        // Initialize sample student data
        students = new ArrayList<>();
        
        // The first student is the one assumed to be "logged in" (Ashish)
        students.add(new Student("Ashish V.", "205", "Active", "B.Tech CSE", "IInd", "19CS1001", "9876543210"));
        students.add(new Student("Bob Smith", "102", "On Leave", "B.Tech ECE", "IIIrd", "18EC1002", "8888877777"));
        students.add(new Student("Charlie Brown", "103", "Active", "B.Sc Physics", "Ist", "20PH1003", "9999900000"));
    }
    
    // --- Methods ---
    
    public List<Student> getAllStudents() {
        return students;
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
    
    // --- Food Wastage Logic (Fix for StaffPanel Error) ---
    
    public double getThisMonthFoodWastage() {
        return thisMonthFoodWastage;
    }

    /**
     * Setter method required by StaffPanel to increase/decrease food wastage.
     */
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
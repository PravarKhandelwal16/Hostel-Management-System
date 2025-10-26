import java.io.Serializable; // Make sure this is imported if you plan to serialize students

// Add Serializable if not already present
public class Student implements Serializable {
    private String name;
    private String roomNumber;
    private String status; 
    private String course; 
    private String year; 
    private String rollNumber; 
    private String mobileNumber; 
    
    // --- NEW FIELDS ---
    private String username;
    private String password;

    // --- UPDATED CONSTRUCTOR ---
    public Student(String name, String roomNumber, String status, String course, String year, String rollNumber, String mobileNumber, String username, String password) {
        this.name = name;
        this.roomNumber = (roomNumber == null || roomNumber.isEmpty()) ? "Not Allocated" : roomNumber;
        this.status = status;
        this.course = course;
        this.year = year;
        this.rollNumber = rollNumber;
        this.mobileNumber = mobileNumber;
        this.username = username;
        this.password = password;
    }

    // --- Getters ---
    
    public String getName() {
        return name;
    }

    public String getRoomNumber() {
        // Return "Not Allocated" if the field is null/empty for display consistency
        return (roomNumber == null || roomNumber.isEmpty()) ? "Not Allocated" : roomNumber;
    }

    public String getStatus() {
        return status;
    }
    
    public String getCourse() {
        return course;
    }
    
    public String getYear() {
        return year;
    }
    
    public String getRollNumber() {
        return rollNumber;
    }
    
    public String getMobileNumber() {
        return mobileNumber;
    }

    // --- NEW GETTERS ---
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // --- Setters (REQUIRED for Room Allocation and Status Updates) ---

    /**
     * Allows staff to change a student's assigned room.
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Allows staff to change a student's activity status (e.g., Active, On Leave).
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
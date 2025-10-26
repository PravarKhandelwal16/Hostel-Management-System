public class Student {
    private String name;
    private String roomNumber;
    private String status; 
    private String course; 
    private String year; 
    private String rollNumber; 
    private String mobileNumber; 

    public Student(String name, String roomNumber, String status, String course, String year, String rollNumber, String mobileNumber) {
        this.name = name;
        // Use "Not Allocated" if a room number wasn't provided at creation
        this.roomNumber = (roomNumber == null || roomNumber.isEmpty()) ? "Not Allocated" : roomNumber;
        this.status = status;
        this.course = course;
        this.year = year;
        this.rollNumber = rollNumber;
        this.mobileNumber = mobileNumber;
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
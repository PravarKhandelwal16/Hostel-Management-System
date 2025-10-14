import java.io.Serializable;

/**
 * Represents a student in the hostel.
 * This class holds all information related to a student.
 * It is Serializable to allow for saving the object's state.
 */
public class Student implements Serializable {
    private String name;
    private String rollNumber;
    private String roomNumber;
    private String mobileNumber;
    private String parentMobileNumber;
    private String course;

    // Constructor to initialize a Student object
    public Student(String name, String rollNumber, String mobileNumber, String parentMobileNumber, String course) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.mobileNumber = mobileNumber;
        this.parentMobileNumber = parentMobileNumber;
        this.course = course;
        this.roomNumber = "Not Allocated"; // Default room status
    }

    // Getters and setters for student attributes
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getParentMobileNumber() {
        return parentMobileNumber;
    }

    public void setParentMobileNumber(String parentMobileNumber) {
        this.parentMobileNumber = parentMobileNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
}

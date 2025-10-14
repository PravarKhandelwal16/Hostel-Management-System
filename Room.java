import java.io.Serializable;

/**
 * Represents a room in the hostel.
 * This class holds details about the room number, type, and availability.
 * It is Serializable for saving the object's state.
 */
public class Room implements Serializable {
    private String roomNumber;
    private String roomType; // e.g., "Single", "Double"
    private boolean isAvailable;
    private int capacity;

    // Constructor to initialize a Room object
    public Room(String roomNumber, String roomType, int capacity) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.isAvailable = true; // A new room is available by default
    }

    // Getters and setters for room attributes
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

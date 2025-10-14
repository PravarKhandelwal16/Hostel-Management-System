import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages all hostel data and operations.
 * This class acts as the backend logic for the hostel management system.
 * It handles adding, retrieving, and updating rooms, students, and other entities.
 */
public class Hostel {
    private List<Room> rooms;
    private List<Student> students;
    private List<Complaint> complaints;
    private List<Visitor> visitors;
    private List<Notice> notices;

    private static final String ROOM_FILE = "rooms.dat";
    private static final String STUDENT_FILE = "students.dat";

    // Constructor initializes lists and loads data from files
    public Hostel() {
        rooms = loadData(ROOM_FILE);
        students = loadData(STUDENT_FILE);
        complaints = new ArrayList<>(); // These could also be serialized if needed
        visitors = new ArrayList<>();
        notices = new ArrayList<>();
    }

    // Generic method to load serialized data from a file
    @SuppressWarnings("unchecked")
    private <T> List<T> loadData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>(); // Return new list if file doesn't exist yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Generic method to save serialized data to a file
    private <T> void saveData(String filename, List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Room Management ---
    public void addRoom(Room room) {
        rooms.add(room);
        saveData(ROOM_FILE, rooms);
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public Optional<Room> findRoom(String roomNumber) {
        return rooms.stream().filter(r -> r.getRoomNumber().equals(roomNumber)).findFirst();
    }

    // --- Student Management ---
    public void addStudent(Student student) {
        students.add(student);
        saveData(STUDENT_FILE, students);
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Optional<Student> findStudent(String rollNumber) {
        return students.stream().filter(s -> s.getRollNumber().equals(rollNumber)).findFirst();
    }

    public void updateStudent(Student updatedStudent) {
        saveData(STUDENT_FILE, students);
    }


    // --- Room Allocation ---
    public boolean allocateRoom(String rollNumber, String roomNumber) {
        Optional<Student> studentOpt = findStudent(rollNumber);
        Optional<Room> roomOpt = findRoom(roomNumber);

        if (studentOpt.isPresent() && roomOpt.isPresent()) {
            Student student = studentOpt.get();
            Room room = roomOpt.get();
            if (room.isAvailable()) {
                student.setRoomNumber(roomNumber);
                room.setAvailable(false);
                saveData(STUDENT_FILE, students);
                saveData(ROOM_FILE, rooms);
                return true;
            }
        }
        return false;
    }

    public boolean vacateRoom(String rollNumber) {
        Optional<Student> studentOpt = findStudent(rollNumber);
        if(studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Optional<Room> roomOpt = findRoom(student.getRoomNumber());
            if(roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setAvailable(true);
                student.setRoomNumber("Not Allocated");
                saveData(STUDENT_FILE, students);
                saveData(ROOM_FILE, rooms);
                return true;
            }
        }
        return false;
    }

    // --- Other Functionalities (to be implemented) ---
    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void addVisitor(Visitor visitor) {
        visitors.add(visitor);
    }

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void addNotice(Notice notice) {
        notices.add(notice);
    }

    public List<Notice> getNotices() {
        return notices;
    }
}

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * GUI Panel for allocating rooms to students.
 */
public class RoomAllocationPanel extends JPanel {
    private Hostel hostel;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> roomComboBox;

    public RoomAllocationPanel(Hostel hostel) {
        this.hostel = hostel;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel allocationPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        allocationPanel.setBorder(new TitledBorder("Allocate a Room"));

        // --- Student and Room Selection ---
        allocationPanel.add(new JLabel("Select Student (by Roll No):"));
        studentComboBox = new JComboBox<>();
        allocationPanel.add(studentComboBox);

        allocationPanel.add(new JLabel("Select Available Room:"));
        roomComboBox = new JComboBox<>();
        allocationPanel.add(roomComboBox);

        JButton allocateButton = new JButton("Allocate Room");
        JButton vacateButton = new JButton("Vacate Room");
        allocationPanel.add(allocateButton);
        allocationPanel.add(vacateButton);

        // --- Add a refresh button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("Refresh Lists");
        bottomPanel.add(refreshButton);

        add(allocationPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        allocateButton.addActionListener(e -> allocateRoom());
        vacateButton.addActionListener(e -> vacateRoom());
        refreshButton.addActionListener(e -> refreshComboBoxes());

        // Initial data load
        refreshComboBoxes();
    }

    private void allocateRoom() {
        String studentRoll = (String) studentComboBox.getSelectedItem();
        String roomNumber = (String) roomComboBox.getSelectedItem();

        if (studentRoll == null || roomNumber == null) {
            JOptionPane.showMessageDialog(this, "Please select a student and a room.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (hostel.allocateRoom(studentRoll, roomNumber)) {
            JOptionPane.showMessageDialog(this, "Room allocated successfully!");
            refreshComboBoxes();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to allocate room. It might be occupied or student/room not found.", "Allocation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void vacateRoom() {
        String studentRoll = (String) studentComboBox.getSelectedItem();
        if (studentRoll == null) {
            JOptionPane.showMessageDialog(this, "Please select a student to vacate.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(hostel.vacateRoom(studentRoll)) {
            JOptionPane.showMessageDialog(this, "Room vacated successfully!");
            refreshComboBoxes();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to vacate room.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshComboBoxes() {
        // --- Refresh Students ---
        studentComboBox.removeAllItems();
        hostel.getAllStudents().stream()
                .filter(s -> "Not Allocated".equals(s.getRoomNumber()))
                .forEach(s -> studentComboBox.addItem(s.getRollNumber()));

        //Also add allocated students to vacate them
        hostel.getAllStudents().stream()
                .filter(s -> !"Not Allocated".equals(s.getRoomNumber()))
                .forEach(s -> studentComboBox.addItem(s.getRollNumber()));

        // --- Refresh Rooms ---
        roomComboBox.removeAllItems();
        hostel.getAllRooms().stream()
                .filter(Room::isAvailable)
                .forEach(r -> roomComboBox.addItem(r.getRoomNumber()));
    }
}

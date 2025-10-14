import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * GUI Panel for managing hostel rooms.
 * Allows adding new rooms and viewing existing ones.
 */
public class RoomManagementPanel extends JPanel {
    private Hostel hostel;
    private JTextField roomNumberField, capacityField;
    private JComboBox<String> roomTypeCombo;
    private JTable roomTable;
    private DefaultTableModel tableModel;

    public RoomManagementPanel(Hostel hostel) {
        this.hostel = hostel;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Form for adding a new room ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(new TitledBorder("Add New Room"));

        formPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Room Type:"));
        roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple"});
        formPanel.add(roomTypeCombo);

        formPanel.add(new JLabel("Capacity:"));
        capacityField = new JTextField();
        formPanel.add(capacityField);

        JButton addButton = new JButton("Add Room");
        formPanel.add(new JLabel()); // Placeholder
        formPanel.add(addButton);

        // --- Table to display existing rooms ---
        String[] columnNames = {"Room Number", "Type", "Capacity", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(new TitledBorder("Existing Rooms"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Event Listener for Add Button ---
        addButton.addActionListener(e -> addRoom());

        // Load initial room data
        refreshRoomTable();
    }

    private void addRoom() {
        String roomNumber = roomNumberField.getText();
        String roomType = (String) roomTypeCombo.getSelectedItem();
        int capacity;
        try {
            capacity = Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid capacity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room number cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        hostel.addRoom(new Room(roomNumber, roomType, capacity));
        JOptionPane.showMessageDialog(this, "Room added successfully!");

        // Clear fields and refresh table
        roomNumberField.setText("");
        capacityField.setText("");
        refreshRoomTable();
    }

    private void refreshRoomTable() {
        tableModel.setRowCount(0); // Clear existing data
        List<Room> rooms = hostel.getAllRooms();
        for (Room room : rooms) {
            String status = room.isAvailable() ? "Available" : "Occupied";
            Object[] row = {room.getRoomNumber(), room.getRoomType(), room.getCapacity(), status};
            tableModel.addRow(row);
        }
    }
}

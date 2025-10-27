import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomAllocationPanel extends JPanel {

    // --- UI COMPONENTS ---
    private JTextField studentField;
    private JTextField roomField;
    private JButton checkButton;
    private JButton allocateButton;
    private JButton refreshLiveViewButton; // --- NEW ---

    // --- NEW: Live Status Table ---
    private DefaultTableModel liveStatusTableModel;
    private JTable liveStatusTable;

    public RoomAllocationPanel(Hostel hostel) {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        add(createAllocationForm(), BorderLayout.NORTH);

        // --- NEW: Replace Placeholder with functional table ---
        JPanel liveStatusPanel = new JPanel(new BorderLayout());
        liveStatusPanel.setBorder(BorderFactory.createTitledBorder("Live Room Status"));

        String[] columnNames = {"Room Number", "Type", "Capacity", "Occupancy", "Status", "Occupants (Roll No)"};
        liveStatusTableModel = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Read-only
        };
        liveStatusTable = new JTable(liveStatusTableModel);
        liveStatusTable.setFillsViewportHeight(true);

        liveStatusPanel.add(new JScrollPane(liveStatusTable), BorderLayout.CENTER);

        add(liveStatusPanel, BorderLayout.CENTER);
        // --- END NEW ---

        setupListeners();

        // --- NEW: Load data on open ---
        loadLiveStatusData();
    }

    private JPanel createAllocationForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Allocate / Reallocate Room", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)
        );
        panel.setBorder(titledBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel studentLabel = new JLabel("Student Roll No:");
        studentField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(studentField, gbc);

        JLabel roomLabel = new JLabel("Target Room No:");
        roomField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(roomLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(roomField, gbc);

        checkButton = new JButton("Check Status");
        checkButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 0; gbc.ipadx = 10; panel.add(checkButton, gbc);

        allocateButton = new JButton("Allocate Room");
        allocateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 1; gbc.ipadx = 10; panel.add(allocateButton, gbc);

        // --- NEW: Refresh Button ---
        refreshLiveViewButton = new JButton("Refresh Live View");
        refreshLiveViewButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; panel.add(refreshLiveViewButton, gbc);
        // --- END NEW ---

        return panel;
    }

    // --- UPDATED: Connects new button ---
    private void setupListeners() {
        checkButton.addActionListener(e -> checkStatus());
        allocateButton.addActionListener(e -> allocateRoom());
        refreshLiveViewButton.addActionListener(e -> loadLiveStatusData()); // --- NEW ---
    }

    // --- NEW: Logic for Live Status Table ---
    private void loadLiveStatusData() {
        liveStatusTableModel.setRowCount(0); // Clear table

        // This query joins rooms and students, counts occupancy, and lists all occupants
        String sql = "SELECT "
                + "    r.room_number, "
                + "    r.type, "
                + "    r.capacity, "
                + "    COUNT(s.roll_number) AS occupancy, "
                + "    GROUP_CONCAT(s.roll_number SEPARATOR ', ') AS occupants "
                + "FROM rooms r "
                + "LEFT JOIN students s ON r.room_number = s.room_number "
                + "GROUP BY r.room_number "
                + "ORDER BY r.room_number;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int capacity = rs.getInt("capacity");
                int occupancy = rs.getInt("occupancy");
                String occupants = rs.getString("occupants");
                if (occupants == null) occupants = ""; // Handle empty rooms

                String status;
                if (occupancy == 0) status = "Available";
                else if (occupancy < capacity) status = "Partially Occupied";
                else status = "Full";

                liveStatusTableModel.addRow(new Object[]{
                        rs.getString("room_number"),
                        rs.getString("type"),
                        capacity,
                        occupancy,
                        status,
                        occupants
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading live status: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- checkStatus() and helpers are unchanged ---
    private void checkStatus() {
        String rollNo = studentField.getText().trim();
        String roomNo = roomField.getText().trim();

        if (rollNo.isEmpty() || roomNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter BOTH a Student Roll No and a Target Room No.", "Input Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentInfo = getStudentInfo(rollNo);
        String roomInfo = getRoomInfo(roomNo);

        JOptionPane.showMessageDialog(this,
                "--- STUDENT STATUS ---\n" + studentInfo +
                        "\n\n--- ROOM STATUS ---\n" + roomInfo,
                "Allocation Status Check",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String getStudentInfo(String rollNo) {
        String sql = "SELECT name, room_number FROM students WHERE roll_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "Student: " + rs.getString("name") + " (Roll: " + rollNo + ")"
                        + "\nCurrent Room: " + rs.getString("room_number");
            } else {
                return "Student with Roll No '" + rollNo + "' NOT FOUND.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error checking student: " + e.getMessage();
        }
    }

    private String getRoomInfo(String roomNo) {
        String roomSql = "SELECT type, capacity, is_available FROM rooms WHERE room_number = ?";
        String occupancySql = "SELECT COUNT(*) as count FROM students WHERE room_number = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement roomPstmt = conn.prepareStatement(roomSql);
             PreparedStatement occPstmt = conn.prepareStatement(occupancySql)) {

            roomPstmt.setString(1, roomNo);
            ResultSet roomRs = roomPstmt.executeQuery();

            if (roomRs.next()) {
                int capacity = roomRs.getInt("capacity");
                boolean isAvailable = roomRs.getBoolean("is_available");

                occPstmt.setString(1, roomNo);
                ResultSet occRs = occPstmt.executeQuery();
                int currentOccupancy = 0;
                if (occRs.next()) {
                    currentOccupancy = occRs.getInt("count");
                }

                String status = isAvailable ? "Available" : "Full";
                if (currentOccupancy > 0 && isAvailable) status = "Partially Occupied";

                return "Room: " + roomNo + " (" + roomRs.getString("type") + ")"
                        + "\nCapacity: " + currentOccupancy + " / " + capacity
                        + "\nStatus: " + status;
            } else {
                return "Room with No '" + roomNo + "' NOT FOUND.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error checking room: " + e.getMessage();
        }
    }

    // --- UPDATED: allocateRoom() now refreshes table ---
    private void allocateRoom() {
        String rollNo = studentField.getText().trim();
        String targetRoomNo = roomField.getText().trim();

        if (rollNo.isEmpty() || targetRoomNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter BOTH a Student Roll No and a Target Room No.", "Input Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // --- START TRANSACTION ---

            // 1. Get student's current room
            String oldRoomNo = null;
            String studentSql = "SELECT room_number FROM students WHERE roll_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(studentSql)) {
                pstmt.setString(1, rollNo);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Student with Roll No '" + rollNo + "' not found.");
                }
                oldRoomNo = rs.getString("room_number");
            }

            if (targetRoomNo.equals(oldRoomNo)) {
                throw new SQLException("Student is already in that room.");
            }

            // 2. Check target room capacity
            int capacity = 0;
            int occupancy = 0;
            String roomSql = "SELECT capacity FROM rooms WHERE room_number = ?";
            String occSql = "SELECT COUNT(*) as count FROM students WHERE room_number = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(roomSql)) {
                pstmt.setString(1, targetRoomNo);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Target Room '" + targetRoomNo + "' not found.");
                }
                capacity = rs.getInt("capacity");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(occSql)) {
                pstmt.setString(1, targetRoomNo);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) occupancy = rs.getInt("count");
            }

            if (occupancy >= capacity) {
                throw new SQLException("Target Room '" + targetRoomNo + "' is already full.");
            }

            // 3. Update student's room
            String updateStudentSql = "UPDATE students SET room_number = ? WHERE roll_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateStudentSql)) {
                pstmt.setString(1, targetRoomNo);
                pstmt.setString(2, rollNo);
                pstmt.executeUpdate();
            }

            // 4. Update NEW room's availability
            // (Check if new occupancy fills it)
            boolean newRoomAvailable = (occupancy + 1) < capacity;
            String updateNewRoomSql = "UPDATE rooms SET is_available = ? WHERE room_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateNewRoomSql)) {
                pstmt.setBoolean(1, newRoomAvailable);
                pstmt.setString(2, targetRoomNo);
                pstmt.executeUpdate();
            }

            // 5. Update OLD room's availability (if student had one)
            if (oldRoomNo != null && !oldRoomNo.equals("Not Allocated")) {
                String updateOldRoomSql = "UPDATE rooms SET is_available = 1 WHERE room_number = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateOldRoomSql)) {
                    pstmt.setString(1, oldRoomNo);
                    pstmt.executeUpdate();
                }
            }

            // --- COMMIT TRANSACTION ---
            conn.commit();
            JOptionPane.showMessageDialog(this, "Allocation Successful!\nStudent " + rollNo + " moved to Room " + targetRoomNo, "Success", JOptionPane.INFORMATION_MESSAGE);

            loadLiveStatusData(); // --- NEW: Refresh table on success ---

        } catch (SQLException e) {
            // --- ROLLBACK TRANSACTION ---
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Allocation Failed:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset to default
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


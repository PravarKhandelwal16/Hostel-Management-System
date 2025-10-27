import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// --- NEW IMPORTS ---
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomManagementPanel extends JPanel {
    private DefaultTableModel tableModel;

    // UI Components
    private JTextField roomNumberField;
    private JComboBox<String> roomTypeCombo;
    private JTextField capacityField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton refreshButton; // --- NEW ---

    public RoomManagementPanel(Hostel hostel) {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        add(createNewRoomPanel(), BorderLayout.NORTH);
        add(createExistingRoomsSection(), BorderLayout.CENTER);

        setupListeners();

        // --- NEW: Load rooms from DB ---
        loadRoomsFromDB();
    }

    // Unchanged panel layout
    private JPanel createNewRoomPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Add New Room", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)
        );
        panel.setBorder(titledBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomNumberLabel = new JLabel("Room Number:");
        JLabel roomTypeLabel = new JLabel("Room Type:");
        JLabel capacityLabel = new JLabel("Capacity:");

        roomNumberField = new JTextField(10);
        roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple"});
        capacityField = new JTextField(5);
        addButton = new JButton("Add Room");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; panel.add(roomNumberLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(roomNumberField, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(roomTypeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(roomTypeCombo, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(capacityLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(capacityField, gbc); row++;
        gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST; panel.add(addButton, gbc);

        return panel;
    }

    // --- UPDATED METHOD ---
    // Creates an empty table. Data is loaded by loadRoomsFromDB()
    private JPanel createExistingRoomsSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setOpaque(false);

        String[] columnNames = {"Room Number", "Type", "Capacity", "Status"};

        tableModel = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Rooms"));
        section.add(scrollPane, BorderLayout.CENTER);

        // --- NEW: Bottom panel for buttons ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Refresh Button (Left-aligned)
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshPanel.setOpaque(false);
        refreshButton = new JButton("Refresh List");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshPanel.add(refreshButton);
        bottomPanel.add(refreshPanel, BorderLayout.WEST);

        // Delete Button (Right-aligned)
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deletePanel.setOpaque(false);
        deleteButton = new JButton("Delete Selected Room");
        deleteButton.setBackground(new Color(231, 76, 60)); // Red color
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        deletePanel.add(deleteButton);
        bottomPanel.add(deletePanel, BorderLayout.EAST);

        section.add(bottomPanel, BorderLayout.SOUTH);
        // --- END NEW ---

        return section;
    }

    // --- UPDATED METHOD ---
    private void setupListeners() {
        addButton.addActionListener(e -> addNewRoom());
        deleteButton.addActionListener(e -> deleteSelectedRoom());
        refreshButton.addActionListener(e -> loadRoomsFromDB()); // --- NEW ---
    }

    // --- NEW METHOD ---
    /**
     * Loads all rooms from the database and populates the JTable.
     */
    private void loadRoomsFromDB() {
        tableModel.setRowCount(0); // Clear existing table
        String sql = "SELECT room_number, type, capacity, is_available FROM rooms";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String status = rs.getBoolean("is_available") ? "Available" : "Full";
                tableModel.addRow(new Object[]{
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        status // Convert boolean to "Available" or "Full"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- UPDATED METHOD ---
    /**
     * Inserts a new room record into the database.
     */
    private void addNewRoom() {
        String roomNumber = roomNumberField.getText().trim();
        String roomType = (String) roomTypeCombo.getSelectedItem();
        String capacityStr = capacityField.getText().trim();
        boolean isAvailable = true; // New rooms are available by default

        if (roomNumber.isEmpty() || capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room Number and Capacity cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int capacity = Integer.parseInt(capacityStr);

            String sql = "INSERT INTO rooms (room_number, type, capacity, is_available) VALUES (?, ?, ?, ?)";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, roomNumber);
                pstmt.setString(2, roomType);
                pstmt.setInt(3, capacity);
                pstmt.setBoolean(4, isAvailable);

                pstmt.executeUpdate();

                // Add to table
                tableModel.addRow(new Object[]{roomNumber, roomType, capacity, "Available"});

                // Clear fields
                roomNumberField.setText("");
                capacityField.setText("");
                roomTypeCombo.setSelectedIndex(0);

                JOptionPane.showMessageDialog(this, "Room " + roomNumber + " added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    JOptionPane.showMessageDialog(this, "Room " + roomNumber + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- UPDATED METHOD ---
    /**
     * Deletes the selected room from the database.
     */
    private void deleteSelectedRoom() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) getComponent(1)).getComponent(0)).getViewport().getView();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);

        // --- Check if room is occupied ---
        String checkSql = "SELECT COUNT(*) FROM students WHERE room_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, roomNumber);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this,
                        "Cannot delete room " + roomNumber + ". It is currently occupied by one or more students.",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);
                return; // Stop deletion
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking room occupancy: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // --- End check ---

        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete room " + roomNumber + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String deleteSql = "DELETE FROM rooms WHERE room_number = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

                pstmt.setString(1, roomNumber);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    tableModel.removeRow(selectedRow); // Remove from table
                    JOptionPane.showMessageDialog(this, "Room " + roomNumber + " deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


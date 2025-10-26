import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomManagementPanel extends JPanel {
    private Hostel hostel;
    private DefaultTableModel tableModel;

    // UI Components for the "Add Room" section
    private JTextField roomNumberField;
    private JComboBox<String> roomTypeCombo;
    private JTextField capacityField;
    private JButton addButton;
    private JButton deleteButton; // New button for deletion

    public RoomManagementPanel(Hostel hostel) {
        this.hostel = hostel;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        // Create and add the New Room Panel (includes the Add button setup)
        add(createNewRoomPanel(), BorderLayout.NORTH);

        // Create and add the Existing Rooms Table (includes the Delete button setup)
        add(createExistingRoomsSection(), BorderLayout.CENTER);

        setupListeners();
    }

    private JPanel createNewRoomPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)), 
            "Add New Room", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14), 
            new Color(44, 62, 80)
        );
        panel.setBorder(titledBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels
        JLabel roomNumberLabel = new JLabel("Room Number:");
        JLabel roomTypeLabel = new JLabel("Room Type:");
        JLabel capacityLabel = new JLabel("Capacity:");

        // Fields
        roomNumberField = new JTextField(10);
        roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple"});
        capacityField = new JTextField(5);
        
        addButton = new JButton("Add Room");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Layout Components
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

    private JPanel createExistingRoomsSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setOpaque(false);
        
        // --- Table Setup ---
        String[] columnNames = {"Room Number", "Type", "Capacity", "Status", "Occupant Count"};
        
        // Initial Sample Data (Matching the image)
        Object[][] initialData = {
            {"A-101", "Single", 1, "Available", 0},
            {"A-102", "Double", 2, "Available", 0},
            {"B-201", "Single", 1, "Occupied", 1},
            {"B-202", "Double", 2, "Occupied", 2},
        };
        
        // Use DefaultTableModel to allow dynamic row addition/deletion
        tableModel = new DefaultTableModel(initialData, columnNames);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Rooms"));
        
        section.add(scrollPane, BorderLayout.CENTER);
        
        // --- Delete Button Panel ---
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteButton = new JButton("Delete Selected Room");
        deleteButton.setBackground(new Color(231, 76, 60)); // Red color
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        deletePanel.add(deleteButton);
        section.add(deletePanel, BorderLayout.SOUTH);

        return section;
    }

    private void setupListeners() {
        // 1. Add Room Listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewRoom();
            }
        });
        
        // 2. Delete Room Listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRoom();
            }
        });
    }

    private void addNewRoom() {
        String roomNumber = roomNumberField.getText().trim();
        String roomType = (String) roomTypeCombo.getSelectedItem();
        String capacityStr = capacityField.getText().trim();

        if (roomNumber.isEmpty() || capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room Number and Capacity cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int capacity = Integer.parseInt(capacityStr);
            
            // Check if room already exists (simple check against table data)
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Room " + roomNumber + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Add the new row to the table model
            tableModel.addRow(new Object[]{
                roomNumber, 
                roomType, 
                capacity, 
                "Available", 
                0 // Occupant Count
            });
            
            // Clear fields after successful addition
            roomNumberField.setText("");
            capacityField.setText("");
            roomTypeCombo.setSelectedIndex(0);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSelectedRoom() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) getComponent(1)).getComponent(0)).getViewport().getView();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete room " + roomNumber + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Remove the row from the table model
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Room " + roomNumber + " deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
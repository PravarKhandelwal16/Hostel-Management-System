import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Import java.sql classes
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentManagementPanel extends JPanel {
    private DefaultTableModel tableModel;

    // UI Components
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField courseField;
    private JComboBox<String> yearCombo;
    private JTextField mobileField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton refreshButton; // --- NEW ---

    public StudentManagementPanel(Hostel hostel) {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        add(createNewStudentPanel(), BorderLayout.NORTH);
        add(createStudentListSection(), BorderLayout.CENTER);

        setupListeners();

        loadStudentsFromDB();
    }

    // createNewStudentPanel() is UNCHANGED from the previous version.
    private JPanel createNewStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Add New Student Record",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(44, 62, 80)
        );
        panel.setBorder(titledBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels
        JLabel nameLabel = new JLabel("Name:");
        JLabel rollNumberLabel = new JLabel("Roll Number:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel yearLabel = new JLabel("Year:");
        JLabel mobileLabel = new JLabel("Mobile Number:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        // Fields
        nameField = new JTextField(15);
        rollNumberField = new JTextField(10);
        courseField = new JTextField(15);
        yearCombo = new JComboBox<>(new String[]{"Ist", "IInd", "IIIrd", "IVth"});
        mobileField = new JTextField(15);
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        saveButton = new JButton("Save New Student");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Layout
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; panel.add(rollNumberLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(rollNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; panel.add(courseLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(courseField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; panel.add(yearLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(yearCombo, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; panel.add(mobileLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(mobileField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; panel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = ++row; gbc.anchor = GridBagConstraints.EAST; panel.add(saveButton, gbc);

        return panel;
    }

    // --- UPDATED METHOD ---
    // Added a 'bottomPanel' to hold both Refresh and Delete buttons
    private JPanel createStudentListSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setOpaque(false);

        String[] columnNames = {"Name", "Roll Number", "Course", "Year", "Mobile", "Status", "Room No.", "Username"};

        tableModel = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Student Records"));
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
        deleteButton = new JButton("Delete Selected Student");
        deleteButton.setBackground(new Color(231, 76, 60));
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
        saveButton.addActionListener(e -> addNewStudent());
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        refreshButton.addActionListener(e -> loadStudentsFromDB()); // --- NEW ---
    }

    /**
     * Loads all students from the database and populates the JTable.
     */
    private void loadStudentsFromDB() {
        tableModel.setRowCount(0); // Clear existing table data

        String sql = "SELECT name, roll_number, course, year, mobile, status, room_number, username FROM students";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("roll_number"),
                        rs.getString("course"),
                        rs.getString("year"),
                        rs.getString("mobile"),
                        rs.getString("status"),
                        rs.getString("room_number"),
                        rs.getString("username")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading student data from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inserts a new student record into the database.
     */
    private void addNewStudent() {
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String course = courseField.getText().trim();
        String year = (String) yearCombo.getSelectedItem();
        String mobile = mobileField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()); // Get password

        String initialStatus = "Active";
        String initialRoom = "Not Allocated";

        if (name.isEmpty() || rollNumber.isEmpty() || course.isEmpty() || mobile.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- SQL INSERT STATEMENT ---
        String sql = "INSERT INTO students "
                + "(roll_number, name, course, year, mobile, status, room_number, username, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rollNumber);
            pstmt.setString(2, name);
            pstmt.setString(3, course);
            pstmt.setString(4, year);
            pstmt.setString(5, mobile);
            pstmt.setString(6, initialStatus);
            pstmt.setString(7, initialRoom);
            pstmt.setString(8, username);
            pstmt.setString(9, password); // Save the password

            pstmt.executeUpdate(); // Execute the insert

            // Add new student to the JTable
            tableModel.addRow(new Object[]{
                    name, rollNumber, course, year, mobile, initialStatus, initialRoom, username
            });

            // Clear fields after successful addition
            nameField.setText("");
            rollNumberField.setText("");
            courseField.setText("");
            mobileField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            yearCombo.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "Student " + name + " saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Error: A student with that Roll Number or Username already exists.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving student to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Deletes the selected student from the database.
     */
    private void deleteSelectedStudent() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) getComponent(1)).getComponent(0)).getViewport().getView();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentName = (String) tableModel.getValueAt(selectedRow, 0);
        String rollNumber = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete the record for " + studentName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM students WHERE roll_number = ?";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, rollNumber);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    tableModel.removeRow(selectedRow); // Remove from table
                    JOptionPane.showMessageDialog(this, "Student record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Could not find student in database (was it already deleted?).", "Delete Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting student from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


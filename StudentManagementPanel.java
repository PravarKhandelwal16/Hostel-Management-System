import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementPanel extends JPanel {
    private Hostel hostel;
    private DefaultTableModel tableModel;

    // UI Components for the "Add Student" section
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField courseField;
    private JComboBox<String> yearCombo;
    private JTextField mobileField;
    // --- NEW UI FIELDS ---
    private JTextField usernameField;
    private JPasswordField passwordField; // Use JPasswordField for password
    
    private JButton saveButton;
    private JButton deleteButton;

    public StudentManagementPanel(Hostel hostel) {
        this.hostel = hostel;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        add(createNewStudentPanel(), BorderLayout.NORTH);
        add(createStudentListSection(), BorderLayout.CENTER);

        setupListeners();
    }

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

        // Labels and Fields
        JLabel nameLabel = new JLabel("Name:");
        JLabel rollNumberLabel = new JLabel("Roll Number:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel yearLabel = new JLabel("Year:");
        JLabel mobileLabel = new JLabel("Mobile Number:");
        // --- NEW LABELS ---
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField(15); 
        rollNumberField = new JTextField(10);
        courseField = new JTextField(15); 
        yearCombo = new JComboBox<>(new String[]{"Ist", "IInd", "IIIrd", "IVth"});
        mobileField = new JTextField(15);
        // --- NEW FIELDS ---
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        
        saveButton = new JButton("Save New Student");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // --- UPDATED LAYOUT ---
        int row = 0;
        // Row 0: Name
        gbc.gridx = 0; gbc.gridy = row; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(nameField, gbc);
        
        // Row 1: Roll Number
        gbc.gridx = 0; gbc.gridy = ++row; panel.add(rollNumberLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(rollNumberField, gbc);

        // Row 2: Course
        gbc.gridx = 0; gbc.gridy = ++row; panel.add(courseLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(courseField, gbc);
        
        // Row 3: Year
        gbc.gridx = 0; gbc.gridy = ++row; panel.add(yearLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(yearCombo, gbc);
        
        // Row 4: Mobile
        gbc.gridx = 0; gbc.gridy = ++row; panel.add(mobileLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(mobileField, gbc);
        
        // Row 5: Username
        gbc.gridx = 0; gbc.gridy = ++row; panel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(usernameField, gbc);
        
        // Row 6: Password
        gbc.gridx = 0; gbc.gridy = ++row; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; panel.add(passwordField, gbc);
        
        // Row 7: Save Button
        gbc.gridx = 1; gbc.gridy = ++row; gbc.anchor = GridBagConstraints.EAST; panel.add(saveButton, gbc);

        return panel;
    }
    
    private JPanel createStudentListSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setOpaque(false);
        
        // --- UPDATED TABLE ---
        String[] columnNames = {"Name", "Roll Number", "Course", "Year", "Mobile", "Status", "Room No.", "Username"};
        
        // Load initial data from the Hostel model
        List<Student> initialStudents = hostel.getAllStudents();
        Object[][] initialData = new Object[initialStudents.size()][columnNames.length];
        
        for (int i = 0; i < initialStudents.size(); i++) {
            Student s = initialStudents.get(i);
            initialData[i][0] = s.getName();
            initialData[i][1] = s.getRollNumber();
            initialData[i][2] = s.getCourse();
            initialData[i][3] = s.getYear();
            initialData[i][4] = s.getMobileNumber();
            initialData[i][5] = s.getStatus();
            initialData[i][6] = s.getRoomNumber();
            initialData[i][7] = s.getUsername(); // Add username
        }
        
        tableModel = new DefaultTableModel(initialData, columnNames);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Student Records"));
        
        section.add(scrollPane, BorderLayout.CENTER);
        
        // --- Delete Button Panel ---
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteButton = new JButton("Delete Selected Student");
        deleteButton.setBackground(new Color(231, 76, 60)); 
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        deletePanel.add(deleteButton);
        section.add(deletePanel, BorderLayout.SOUTH);

        return section;
    }

    private void setupListeners() {
        // 1. Add Student Listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewStudent();
            }
        });
        
        // 2. Delete Student Listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedStudent();
            }
        });
    }

    // --- UPDATED METHOD ---
    private void addNewStudent() {
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String course = courseField.getText().trim();
        String year = (String) yearCombo.getSelectedItem();
        String mobile = mobileField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()); // Get password

        if (name.isEmpty() || rollNumber.isEmpty() || course.isEmpty() || mobile.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if student already exists (in the main Hostel list)
        for (Student s : hostel.getAllStudents()) {
            if (s.getRollNumber().equals(rollNumber) || s.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, "Student with this Roll Number or Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // --- Create and add student to the Hostel model ---
        Student newStudent = new Student(
            name, 
            "Not Allocated", // Default room
            "Active",        // Default status
            course, 
            year, 
            rollNumber, 
            mobile,
            username,
            password
        );
        hostel.getAllStudents().add(newStudent);

        // Add the new student to the table model (DON'T add password)
        tableModel.addRow(new Object[]{
            name, 
            rollNumber, 
            course, 
            year, 
            mobile,
            "Active", 
            "Not Allocated",
            username // Add username to table
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
    }
    
    // --- UPDATED METHOD ---
    private void deleteSelectedStudent() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) getComponent(1)).getComponent(0)).getViewport().getView();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentName = (String) tableModel.getValueAt(selectedRow, 0);
        String rollNumber = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the record for " + studentName + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // --- Remove from Hostel model ---
            hostel.getAllStudents().removeIf(student -> student.getRollNumber().equals(rollNumber));
            
            // Remove the row from the table model
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Student record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
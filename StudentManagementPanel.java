import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * GUI Panel for managing students.
 * Allows adding, viewing, and updating student information.
 */
public class StudentManagementPanel extends JPanel {
    private Hostel hostel;
    private JTextField nameField, rollNumberField, mobileField, parentMobileField, courseField;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public StudentManagementPanel(Hostel hostel) {
        this.hostel = hostel;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Form for adding a new student ---
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(new TitledBorder("Register New Student"));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        formPanel.add(rollNumberField);

        formPanel.add(new JLabel("Mobile Number:"));
        mobileField = new JTextField();
        formPanel.add(mobileField);

        formPanel.add(new JLabel("Parent Mobile No:"));
        parentMobileField = new JTextField();
        formPanel.add(parentMobileField);

        formPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        formPanel.add(courseField);

        JButton addButton = new JButton("Add Student");
        formPanel.add(new JLabel()); // Placeholder
        formPanel.add(addButton);

        // --- Table to display existing students ---
        String[] columnNames = {"Roll Number", "Name", "Course", "Mobile", "Room No."};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new TitledBorder("Registered Students"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Event Listeners ---
        addButton.addActionListener(e -> addStudent());

        refreshStudentTable();
    }

    private void addStudent() {
        String name = nameField.getText();
        String roll = rollNumberField.getText();
        String mobile = mobileField.getText();
        String parentMobile = parentMobileField.getText();
        String course = courseField.getText();

        if (name.isEmpty() || roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Roll Number are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        hostel.addStudent(new Student(name, roll, mobile, parentMobile, course));
        JOptionPane.showMessageDialog(this, "Student added successfully!");

        // Clear fields and refresh
        nameField.setText("");
        rollNumberField.setText("");
        mobileField.setText("");
        parentMobileField.setText("");
        courseField.setText("");
        refreshStudentTable();
    }

    private void refreshStudentTable() {
        tableModel.setRowCount(0);
        List<Student> students = hostel.getAllStudents();
        for (Student student : students) {
            Object[] row = {student.getRollNumber(), student.getName(), student.getCourse(), student.getMobileNumber(), student.getRoomNumber()};
            tableModel.addRow(row);
        }
    }
}

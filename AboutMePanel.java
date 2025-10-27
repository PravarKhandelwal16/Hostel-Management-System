import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
// --- NEW IMPORTS ---
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AboutMePanel extends JPanel {

    private Hostel hostel;
    private Student currentStudent;
    private MainFrame mainFrame;

    // --- NEW: Labels made class members to allow updating ---
    private JLabel nameValueLabel;
    private JLabel roomValueLabel;
    private JLabel courseValueLabel;
    private JLabel yearValueLabel;
    private JLabel rollNumberValueLabel;
    private JLabel mobileValueLabel;

    public AboutMePanel(Hostel hostel, MainFrame mainFrame) {
        this.hostel = hostel;
        this.currentStudent = hostel.getLoggedInStudent();
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(245, 247, 250));

        if (currentStudent != null) {
            add(createDetailsPanel(), BorderLayout.NORTH);
        } else {
            add(new JLabel("No student profile data found.", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        // --- UPDATED: createSouthPanel() now builds the south panel ---
        add(createSouthPanel(), BorderLayout.SOUTH);
    }

    // --- UPDATED METHOD ---
    // Initializes labels with currentStudent data
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)),
                "My Profile Details",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), new Color(44, 62, 80)));

        // 1. Student Name
        panel.add(createLabel("Name:"));
        nameValueLabel = createValueLabel(currentStudent.getName());
        panel.add(nameValueLabel);

        // 2. Allotted Room
        panel.add(createLabel("Allotted Room:"));
        roomValueLabel = createValueLabel(currentStudent.getRoomNumber());
        panel.add(roomValueLabel);

        // 3. Course
        panel.add(createLabel("Course:"));
        courseValueLabel = createValueLabel(currentStudent.getCourse());
        panel.add(courseValueLabel);

        // 4. Year
        panel.add(createLabel("Year:"));
        yearValueLabel = createValueLabel(currentStudent.getYear());
        panel.add(yearValueLabel);

        // 5. Roll Number
        panel.add(createLabel("Roll Number:"));
        rollNumberValueLabel = createValueLabel(currentStudent.getRollNumber());
        panel.add(rollNumberValueLabel);

        // 6. Mobile Number
        panel.add(createLabel("Mobile:"));
        mobileValueLabel = createValueLabel(currentStudent.getMobileNumber());
        panel.add(mobileValueLabel);

        return panel;
    }

    // --- NEW: Panel for buttons ---
    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Added spacing
        southPanel.setOpaque(false);

        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.addActionListener(e -> refreshProfileData());
        southPanel.add(refreshButton);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> mainFrame.showUserSelection());
        southPanel.add(logoutButton);

        return southPanel;
    }

    // --- NEW: Logic for refresh button ---
    private void refreshProfileData() {
        if (currentStudent == null) return; // Should not happen

        String sql = "SELECT * FROM students WHERE roll_number = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentStudent.getRollNumber());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Create a new Student object with the fresh data
                    Student freshStudent = new Student(
                            rs.getString("name"),
                            rs.getString("room_number"),
                            rs.getString("status"),
                            rs.getString("course"),
                            rs.getString("year"),
                            rs.getString("roll_number"),
                            rs.getString("mobile"),
                            rs.getString("username"),
                            // NOTE: Do not fetch password for security, just pass null/empty
                            ""
                    );

                    // Update the session and local reference
                    hostel.setLoggedInStudent(freshStudent);
                    this.currentStudent = freshStudent;

                    // Update the labels on the screen
                    nameValueLabel.setText(currentStudent.getName());
                    roomValueLabel.setText(currentStudent.getRoomNumber());
                    courseValueLabel.setText(currentStudent.getCourse());
                    yearValueLabel.setText(currentStudent.getYear());
                    rollNumberValueLabel.setText(currentStudent.getRollNumber());
                    mobileValueLabel.setText(currentStudent.getMobileNumber());

                    JOptionPane.showMessageDialog(this, "Profile data refreshed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // This is a serious issue, student logged in but is now gone?
                    JOptionPane.showMessageDialog(this, "Could not find your student record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error refreshing data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Unchanged
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    // Unchanged
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }
}


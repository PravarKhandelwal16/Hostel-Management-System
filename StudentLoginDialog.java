import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// --- NEW IMPORTS ---
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentLoginDialog extends JDialog {

    private boolean loginSuccessful = false;
    private JTextField usernameField;
    private JPasswordField passwordField;

    // --- NEW FIELD ---
    private Hostel hostel; // To store the logged-in student in the session

    // --- UPDATED CONSTRUCTOR ---
    public StudentLoginDialog(JFrame parent, Hostel hostel) {
        super(parent, "Student Login", true); // Modal dialog
        this.hostel = hostel; // Store the hostel reference

        setupUI();

        setSize(350, 200);
        setLocationRelativeTo(parent);
    }

    // setupUI() is unchanged.
    private void setupUI() {
        // Use BorderLayout for the main structure
        setLayout(new BorderLayout());

        // --- Input Panel (GridBagLayout for neat alignment) ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // 1. Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(usernameField, gbc);

        // 2. Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(passwordField, gbc);

        add(inputPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        // Add action listeners
        loginButton.addActionListener(e -> attemptLogin());
        cancelButton.addActionListener(e -> dispose());
        passwordField.addActionListener(e -> attemptLogin());

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- COMPLETELY REVISED METHOD ---
    /**
     * Tries to log in by querying the database.
     */
    private void attemptLogin() {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword());

        // Validate against the 'students' table in the database
        Student loggedInStudent = validateLoginFromDB(enteredUsername, enteredPassword);

        if (loggedInStudent != null) {
            loginSuccessful = true;
            hostel.setLoggedInStudent(loggedInStudent); // --- SET THE LOGGED-IN STUDENT IN THE SESSION ---
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            loginSuccessful = false;
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            // Clear password field for security
            passwordField.setText("");
        }
    }

    /**
     * Queries the 'students' table to find a matching user.
     * @return a Student object if found, otherwise null.
     */
    private Student validateLoginFromDB(String username, String password) {
        // Use PreparedStatement to prevent SQL injection
        String sql = "SELECT * FROM students WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // --- Found the student, create a Student object from the data ---
                    return new Student(
                            rs.getString("name"),
                            rs.getString("room_number"),
                            rs.getString("status"),
                            rs.getString("course"),
                            rs.getString("year"),
                            rs.getString("roll_number"),
                            rs.getString("mobile"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                } else {
                    return null; // No match found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentLoginDialog extends JDialog {
    
    // Fixed Credentials
    private static final String VALID_USERNAME = "SRM";
    private static final String VALID_PASSWORD = "0000";
    
    private boolean loginSuccessful = false;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public StudentLoginDialog(JFrame parent) {
        super(parent, "Student Login", true); // Modal dialog
        
        setupUI();
        
        setSize(350, 200);
        setLocationRelativeTo(parent);
    }
    
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
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        // Allow pressing Enter in the password field to submit
        passwordField.addActionListener(e -> attemptLogin());

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attemptLogin() {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword());
        
        if (enteredUsername.equals(VALID_USERNAME) && enteredPassword.equals(VALID_PASSWORD)) {
            loginSuccessful = true;
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            loginSuccessful = false;
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            // Clear password field for security
            passwordField.setText("");
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
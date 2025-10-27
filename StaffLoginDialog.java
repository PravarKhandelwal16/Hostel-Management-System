import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Modal dialog for staff login with a JPasswordField.
 */
public class StaffLoginDialog extends JDialog {

    private JPasswordField passwordField;
    private boolean loginSuccessful = false;
    private static final String VALID_PASSWORD = "admin";

    public StaffLoginDialog(JFrame owner) {
        super(owner, "Staff Login", true);
        
        setupUI();
        pack();
        setLocationRelativeTo(owner);
        
        // Prevent closing the dialog with X button without login attempt
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // Simply close the dialog
            }
        });
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // --- Prompt ---
        JLabel prompt = new JLabel("Enter Staff Password:");
        prompt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        prompt.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(prompt);
        mainPanel.add(Box.createVerticalStrut(15));

        // --- Password Field Panel ---
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordField = new JPasswordField(15);
        
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");
        
        loginButton.addActionListener(e -> attemptLogin());
        cancelButton.addActionListener(e -> dispose());
        
        // Apply simple styling (reusing a utility method or the styling used for other dialog buttons)
        styleButton(loginButton);
        styleButton(cancelButton);

        // Allow pressing Enter in the password field to submit
        passwordField.addActionListener(e -> attemptLogin());

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private void styleButton(JButton button) {
        // Simple styling to match the look of the selection dialog buttons
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private void attemptLogin() {
        // NOTE: getPassword() returns a char array, which should be converted to String for comparison
        String inputPassword = new String(passwordField.getPassword());
        
        if (VALID_PASSWORD.equals(inputPassword)) {
            loginSuccessful = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid Password. Please try again.", 
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
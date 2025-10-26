import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserSelectionDialog extends JDialog { 
    private String userType = null;

    public UserSelectionDialog(Frame parent) {
        super(parent, "Select View Type", true); // Set the title to match the image
        
        // --- 1. Main Layout and Message ---
        setLayout(new BorderLayout(10, 10));
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("Please choose your role for this session:", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        contentPanel.add(messageLabel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // --- 2. Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        JButton studentButton = new JButton("I'm a student");
        JButton staffButton = new JButton("I'm a staff");
        
        // Match button styling
        studentButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        staffButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Set the preferred size to make the buttons uniform and wider
        Dimension buttonSize = new Dimension(120, 30);
        studentButton.setPreferredSize(buttonSize);
        staffButton.setPreferredSize(buttonSize);
        
        // --- 3. Action Listeners ---
        studentButton.addActionListener(e -> { 
            userType = "Student"; 
            dispose(); 
        });
        
        staffButton.addActionListener(e -> { 
            userType = "Staff"; 
            dispose(); 
        });
        
        buttonPanel.add(studentButton);
        buttonPanel.add(staffButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // --- 4. Final Dialog Setup ---
        setResizable(false);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        
        // Display the dialog and wait for selection
        setVisible(true); 
    }

    /**
     * Returns "Student", "Staff", or null if the dialog was closed/cancelled.
     */
    public String getUserType() {
        return userType;
    }
}
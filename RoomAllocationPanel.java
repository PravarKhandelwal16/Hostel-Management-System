import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RoomAllocationPanel extends JPanel {
    private Hostel hostel;

    public RoomAllocationPanel(Hostel hostel) {
        this.hostel = hostel;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        add(createAllocationForm(), BorderLayout.NORTH);
        add(new PlaceholderPanel("Live Allocation Status and History View"), BorderLayout.CENTER);
    }

    private JPanel createAllocationForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // --- FIX: Corrected createTitledBorder usage ---
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)), 
            "Allocate / Reallocate Room", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(44, 62, 80)
        );
        panel.setBorder(titledBorder);
        // --- END FIX ---

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // --- Input Fields ---
        
        // Student Field (Using a text field for simple ID entry)
        JLabel studentLabel = new JLabel("Student Roll No:");
        JTextField studentField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(studentLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(studentField, gbc);

        // Room Field (Using a text field for simple room number entry)
        JLabel roomLabel = new JLabel("Target Room No:");
        JTextField roomField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(roomLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(roomField, gbc);
        
        // --- Buttons ---
        
        // Check Status Button (to confirm if student/room is valid)
        JButton checkButton = new JButton("Check Status");
        checkButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 0; gbc.ipadx = 10; panel.add(checkButton, gbc);

        // Allocate Button
        JButton allocateButton = new JButton("Allocate Room");
        allocateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 1; gbc.ipadx = 10; panel.add(allocateButton, gbc);

        return panel;
    }
}
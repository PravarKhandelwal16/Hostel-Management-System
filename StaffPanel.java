import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StaffPanel extends JPanel {

    // --- UPDATED CONSTRUCTOR ---
    public StaffPanel(Hostel hostel, DashboardPanel dashboardPanel, MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));

        JTabbedPane staffTabbedPane = new JTabbedPane();
        staffTabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Management Tabs
        staffTabbedPane.addTab("Manage Rooms", new RoomManagementPanel(hostel));
        staffTabbedPane.addTab("Manage Students", new StudentManagementPanel(hostel));
        staffTabbedPane.addTab("Room Allocation", new RoomAllocationPanel(hostel));

        // Admin Controls Tab
        JPanel testPanel = new JPanel(new FlowLayout());
        testPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JButton increaseFoodButton = new JButton("Increase Food Wastage by 10kg");
        increaseFoodButton.addActionListener(_ -> {
            hostel.setThisMonthFoodWastage(hostel.getThisMonthFoodWastage() + 10);
            JOptionPane.showMessageDialog(this, "Wastage data updated. Student view requires refresh.", "Data Change", JOptionPane.INFORMATION_MESSAGE);
        });
        testPanel.add(increaseFoodButton);

        JButton decreaseFoodButton = new JButton("Decrease Food Wastage by 10kg");
        decreaseFoodButton.addActionListener(_ -> {
            double newWastage = Math.max(0, hostel.getThisMonthFoodWastage() - 10);
            hostel.setThisMonthFoodWastage(newWastage);
            JOptionPane.showMessageDialog(this, "Wastage data updated.", "Data Change", JOptionPane.INFORMATION_MESSAGE);
        });
        testPanel.add(decreaseFoodButton);

        // --- NEW LOGOUT BUTTON ---
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        // Add action listener to call the MainFrame's reset method
        logoutButton.addActionListener(e -> mainFrame.showUserSelection());
        testPanel.add(Box.createHorizontalStrut(20)); // Add some space
        testPanel.add(logoutButton);
        // --- END NEW ---

        staffTabbedPane.addTab("Admin Controls", testPanel);

        add(staffTabbedPane, BorderLayout.CENTER);
    }
}

// =================================================================
// NESTED UTILITY CLASS DEFINITION
// =================================================================

/**
 * A minimal JPanel to serve as a content placeholder for a tab.
 */
class PlaceholderPanel extends JPanel {
    public PlaceholderPanel(String message) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        label.setForeground(Color.GRAY);

        add(label, BorderLayout.CENTER);
    }
}

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StaffPanel extends JPanel {

    public StaffPanel(Hostel hostel, DashboardPanel dashboardPanel) {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230)); // Set a background color for visual distinction

        JTabbedPane staffTabbedPane = new JTabbedPane();
        staffTabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Management Tabs
        staffTabbedPane.addTab("Manage Rooms", new RoomManagementPanel(hostel));
        staffTabbedPane.addTab("Manage Students", new StudentManagementPanel(hostel));
        staffTabbedPane.addTab("Room Allocation", new RoomAllocationPanel(hostel));
        
        // Admin Controls Tab (Wastage Testing)
        JButton increaseFoodButton = new JButton("Increase Food Wastage by 10kg");
        increaseFoodButton.addActionListener(_ -> {
            hostel.setThisMonthFoodWastage(hostel.getThisMonthFoodWastage() + 10);
            JOptionPane.showMessageDialog(this, "Wastage data updated in model. Student view requires refresh.", "Data Change", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton decreaseFoodButton = new JButton("Decrease Food Wastage by 10kg");
        decreaseFoodButton.addActionListener(_ -> {
            // Added check to prevent negative wastage
            double newWastage = Math.max(0, hostel.getThisMonthFoodWastage() - 10);
            hostel.setThisMonthFoodWastage(newWastage);
            JOptionPane.showMessageDialog(this, "Wastage data updated.", "Data Change", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel testPanel = new JPanel(new FlowLayout());
        testPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        testPanel.add(increaseFoodButton);
        testPanel.add(decreaseFoodButton);
        staffTabbedPane.addTab("Admin Controls", testPanel);
        
        add(staffTabbedPane, BorderLayout.CENTER);
    }
}

// =================================================================
// NESTED UTILITY CLASS DEFINITION (Resolves the compilation error)
// =================================================================

/**
 * A minimal JPanel to serve as a content placeholder for a tab.
 * Defined here to make it accessible to RoomManagementPanel, StudentManagementPanel, etc.
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
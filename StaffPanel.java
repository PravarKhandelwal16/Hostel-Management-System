import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StaffPanel extends JPanel {

    // MainFrame reference for logout
    private MainFrame mainFrame;
    private Hostel hostel;
    // We won't use this, but adding it fixes the constructor error
    private DashboardPanel dashboardPanel;

    // --- CONSTRUCTOR UPDATED ---
    // The order of parameters now matches what MainFrame.java is sending
    public StaffPanel(Hostel hostel, DashboardPanel dashboardPanel, MainFrame mainFrame) {
        this.mainFrame = mainFrame; // Store the MainFrame reference
        this.hostel = hostel;
        this.dashboardPanel = dashboardPanel; // Store this to resolve the error

        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));

        JTabbedPane staffTabbedPane = new JTabbedPane();
        staffTabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Management Tabs
        // We pass 'hostel' which is now correctly initialized
        staffTabbedPane.addTab("Manage Rooms", new RoomManagementPanel(hostel));
        staffTabbedPane.addTab("Manage Students", new StudentManagementPanel(hostel));
        staffTabbedPane.addTab("Room Allocation", new RoomAllocationPanel(hostel));

        // --- NEW TAB ---
        staffTabbedPane.addTab("Daily Status", new DailyStatusPanel());

        // Admin Controls Tab
        JPanel adminPanel = createAdminPanel();
        staffTabbedPane.addTab("Admin Controls", adminPanel);

        add(staffTabbedPane, BorderLayout.CENTER);
    }

    // Helper method to create the admin panel with the logout button
    private JPanel createAdminPanel() {
        JPanel testPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        testPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // --- Food Wastage Simulation ---
        JButton increaseFoodButton = new JButton("Simulate: +10kg Food Wastage");
        increaseFoodButton.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this,
                    "Food wastage simulation. (This is just a test button).",
                    "Data Change", JOptionPane.INFORMATION_MESSAGE);
        });
        testPanel.add(increaseFoodButton);

        // --- Logout Button ---
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(231, 76, 60)); // Red color
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(_ -> {
            int choice = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                mainFrame.showUserSelection(); // Call the public method in MainFrame
            }
        });

        testPanel.add(logoutButton);

        return testPanel;
    }
}


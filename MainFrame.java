import javax.swing.*;
import java.awt.*;

/**
 * The main window for the Hostel Management System application.
 * It uses a JTabbedPane to organize different management functionalities.
 */
public class MainFrame extends JFrame {

    private Hostel hostel;

    public MainFrame() {
        hostel = new Hostel();

        setTitle("Smart Hostel Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // --- Set a modern Look and Feel ---
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- Main Tabbed Pane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // --- Add Panels for each module ---
        tabbedPane.addTab("Dashboard", new DashboardPanel());
        tabbedPane.addTab("Manage Rooms", new RoomManagementPanel(hostel));
        tabbedPane.addTab("Manage Students", new StudentManagementPanel(hostel));
        tabbedPane.addTab("Room Allocation", new RoomAllocationPanel(hostel));
        // Add other panels here as they are created
        // tabbedPane.addTab("Fee Management", new FeePanel(hostel));
        // tabbedPane.addTab("Complaints", new ComplaintPanel(hostel));

        add(tabbedPane);
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}

/**
 * A simple placeholder dashboard panel.
 */
class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Smart Hostel Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }
}

import java.awt.*;
import javax.swing.*;

/**
 * The main window for the Hostel Management System application.
 * It uses a JTabbedPane to organize different management functionalities.
 */
public class MainFrame extends JFrame {

    private Hostel hostel;

    public MainFrame() {
        hostel = new Hostel();

        // --- Set the best built-in Look and Feel: Nimbus ---
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel not found. Using default.");
        }

        setTitle("Smart Hostel Management System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // --- Main Tabbed Pane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // --- Add Panels for each module ---
        tabbedPane.addTab("Dashboard", new DashboardPanel(hostel)); // Uses the NEW Dashboard
        tabbedPane.addTab("Manage Rooms", new RoomManagementPanel(hostel));
        tabbedPane.addTab("Manage Students", new StudentManagementPanel(hostel));
        tabbedPane.addTab("Room Allocation", new RoomAllocationPanel(hostel));
        // You can add more tabs here

        add(tabbedPane);
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
});
}
}
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    private Hostel hostel;
    // --- THIS IS THE FIX ---
    // We declare dashboardPanel but do NOT initialize it here.
    // It will be null until a student logs in.
    private DashboardPanel dashboardPanel;
    private JPanel mainContentPanel;

    public MainFrame() {
        super("Smart Hostel Management System");

        // 1. Initialize Database on startup
        DatabaseManager.initializeDatabase();

        // 2. Initial Data Setup
        hostel = new Hostel();

        // 3. UI Setup
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel not found. Using default.");
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        mainContentPanel = new JPanel(new BorderLayout());

        // --- REMOVED THE BUGGY LINE ---
        // dashboardPanel = new DashboardPanel(hostel); // This line caused the crash

        add(mainContentPanel);

        // 4. Start the Login/Selection Flow
        showUserSelection();

        setVisible(true);
    }

    /**
     * Shows the initial "Student" or "Staff" login choice.
     * This is also used for "Logout".
     */
    public void showUserSelection() {
        // Clear the main panel
        mainContentPanel.removeAll();
        mainContentExample(); // Show a simple welcome
        mainContentPanel.revalidate();
        mainContentPanel.repaint();

        boolean viewEntered = false;

        while (!viewEntered) {
            UserSelectionDialog selectionDialog = new UserSelectionDialog(this);
            String userType = selectionDialog.getUserType();

            if ("Student".equals(userType)) {
                // Pass hostel so the dialog can check the DB
                StudentLoginDialog loginDialog = new StudentLoginDialog(this, hostel);
                loginDialog.setVisible(true);

                if (loginDialog.isLoginSuccessful()) {
                    setTitle("Smart Hostel Management System - Student View");
                    showStudentView(); // This will use the logged-in student
                    viewEntered = true;
                } else {
                    int exitOption = JOptionPane.showConfirmDialog(this,
                            "Login failed or cancelled. Do you want to exit the application?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (exitOption == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            } else if ("Staff".equals(userType)) {
                if (showStaffLogin()) {
                    setTitle("Smart Hostel Management System - Staff View");
                    showStaffView();
                    viewEntered = true;
                } else {
                    int exitOption = JOptionPane.showConfirmDialog(this,
                            "Login failed. Do you want to exit the application?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (exitOption == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            } else {
                // User closed the selection dialog
                System.exit(0);
            }
        }
    }

    // Simple placeholder for the welcome screen
    private void mainContentExample() {
        mainContentPanel.removeAll();
        JLabel welcomeLabel = new JLabel("Welcome to the Hostel Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainContentPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private boolean showStaffLogin() {
        StaffLoginDialog staffLoginDialog = new StaffLoginDialog(this);
        staffLoginDialog.setVisible(true);
        return staffLoginDialog.isLoginSuccessful();
    }

    private void showStudentView() {
        mainContentPanel.removeAll();

        // --- IMPORTANT ---
        // We create a *new* DashboardPanel here, *after* login.
        // hostel.getLoggedInStudent() is now guaranteed to be non-null.
        dashboardPanel = new DashboardPanel(hostel);

        JTabbedPane studentTabbedPane = createStudentViewTabs();
        mainContentPanel.add(studentTabbedPane, BorderLayout.CENTER);

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showStaffView() {
        mainContentPanel.removeAll();

        // This is fine. 'dashboardPanel' will be null if staff logs in first,
        // but the StaffPanel constructor handles that.
        StaffPanel staffPanel = new StaffPanel(hostel, dashboardPanel, this);

        mainContentPanel.add(staffPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JTabbedPane createStudentViewTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // This panel is now created with the correct logged-in student
        tabbedPane.addTab("Dashboard", dashboardPanel);

        // The correct order is (hostel, this) to match the AboutMePanel constructor
        tabbedPane.addTab("About Me", new AboutMePanel(hostel, this));

        return tabbedPane;
    }

    public static void main(String[] args) {
        // Use invokeLater to ensure GUI updates are on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}

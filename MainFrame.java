import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private Hostel hostel;
    private DashboardPanel dashboardPanel;
    private JPanel mainContentPanel;

    public MainFrame() {
        super("Smart Hostel Management System");

        // 1. Initialize Database on startup
        DatabaseManager.initializeDatabase();

        // 2. Create the session object
        hostel = new Hostel();

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel not found. Using default.");
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        mainContentPanel = new JPanel(new BorderLayout());
        dashboardPanel = new DashboardPanel(hostel);

        add(mainContentPanel); // Add the main panel to the frame

        // 3. Start the login flow
        showUserSelection();

        // 4. Show the frame
        setVisible(true);
    }

    /**
     * Displays the initial Student/Staff choice dialog.
     * This method now also acts as the "logout" function.
     */
    public void showUserSelection() {
        // Clear the logged-in student from the session
        if (hostel != null) {
            hostel.setLoggedInStudent(null);
        }

        // Clear the main content panel of any old views (student/staff tabs)
        mainContentPanel.removeAll();

        // --- This is the login loop ---
        boolean viewEntered = false;
        while (!viewEntered) {
            UserSelectionDialog selectionDialog = new UserSelectionDialog(this);
            String userType = selectionDialog.getUserType();

            if ("Student".equals(userType)) {
                // Pass hostel session to login dialog
                StudentLoginDialog loginDialog = new StudentLoginDialog(this, hostel);
                loginDialog.setVisible(true);

                if (loginDialog.isLoginSuccessful()) {
                    setTitle("Smart Hostel Management System - Student View");
                    showStudentView();
                    viewEntered = true;
                } else {
                    // User cancelled login
                    int exitOption = JOptionPane.showConfirmDialog(this,
                            "Login failed or cancelled. Do you want to exit the application?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (exitOption == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    // if NO, the loop repeats and shows UserSelectionDialog again
                }
            } else if ("Staff".equals(userType)) {
                if (showStaffLogin()) {
                    setTitle("Smart Hostel Management System - Staff View");
                    showStaffView();
                    viewEntered = true;
                } else {
                    // User cancelled login
                    int exitOption = JOptionPane.showConfirmDialog(this,
                            "Login failed or cancelled. Do you want to exit the application?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (exitOption == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    // if NO, the loop repeats
                }
            } else {
                // User closed the UserSelectionDialog (userType is null)
                System.exit(0);
            }
        }

        // Refresh the frame after adding new panels
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
        JTabbedPane studentTabbedPane = createStudentViewTabs();
        mainContentPanel.add(studentTabbedPane, BorderLayout.CENTER);
        attachDashboardListeners();
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showStaffView() {
        mainContentPanel.removeAll();
        // --- Pass 'this' (MainFrame) to StaffPanel so it can call showUserSelection() ---
        StaffPanel staffPanel = new StaffPanel(hostel, dashboardPanel, this);
        mainContentPanel.add(staffPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JTabbedPane createStudentViewTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.addTab("Dashboard", dashboardPanel);

        // --- Pass 'this' (MainFrame) to AboutMePanel for logout ---
        tabbedPane.addTab("About Me", new AboutMePanel(hostel, this));
        return tabbedPane;
    }

    // Unchanged
    private void attachDashboardListeners() {
        // Remove existing listeners to prevent duplicates
        for (java.awt.event.ActionListener al : dashboardPanel.getCleaningStatusButton().getActionListeners()) {
            dashboardPanel.getCleaningStatusButton().removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : dashboardPanel.getMessSubmitButton().getActionListeners()) {
            dashboardPanel.getMessSubmitButton().removeActionListener(al);
        }

        dashboardPanel.getCleaningStatusButton().addActionListener(_ -> {
            hostel.setCleaningDoneToday(!hostel.isCleaningDoneToday());
            dashboardPanel.updateCleaningStatus();
        });

        dashboardPanel.getMessSubmitButton().addActionListener(_ -> {
            boolean attending = dashboardPanel.getMessCheckBox().isSelected();
            hostel.setMealPlanConfirmed(true);

            String message = attending ?
                    "Confirmed! You are registered for today's mess meal." :
                    "Confirmed! You are opting out of today's mess meal.";

            JOptionPane.showMessageDialog(this, message, "Meal Plan Submitted", JOptionPane.INFORMATION_MESSAGE);

            dashboardPanel.updateMessSubmitButtonStatus();
        });
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}

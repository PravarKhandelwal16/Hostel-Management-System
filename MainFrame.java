import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private Hostel hostel;
    private DashboardPanel dashboardPanel;
    private JPanel mainContentPanel; // Panel to hold the main view (tabs or staff panel)
    
    // NOTE: Staff password validation is handled within StaffLoginDialog

    public MainFrame() {
        super("Smart Hostel Management System");
        
        // --- 1. Initial Data Setup (simplified) ---
        hostel = new Hostel(); 

        // --- 2. UI Setup ---
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel not found. Using default.");
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); 
        
        mainContentPanel = new JPanel(new BorderLayout());
        dashboardPanel = new DashboardPanel(hostel); // DashboardPanel initialized once
        
        // --- 3. Start the Login/Selection Flow ---
        showUserSelection(); 
        
        add(mainContentPanel);
        setVisible(true);
    }

    private void showUserSelection() {
        boolean viewEntered = false;

        while (!viewEntered) {
            UserSelectionDialog selectionDialog = new UserSelectionDialog(this);
            String userType = selectionDialog.getUserType();
            
            if ("Student".equals(userType)) {
                // --- UPDATED DIALOG CREATION ---
                StudentLoginDialog loginDialog = new StudentLoginDialog(this, hostel);
                loginDialog.setVisible(true); 
                
                if (loginDialog.isLoginSuccessful()) {
                    setTitle("Smart Hostel Management System - Student View");
                    showStudentView();
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
                System.exit(0);
            }
        }
    }
    
    private boolean showStaffLogin() {
        // Uses the custom dialog with JPasswordField
        StaffLoginDialog staffLoginDialog = new StaffLoginDialog(this);
        staffLoginDialog.setVisible(true);
        return staffLoginDialog.isLoginSuccessful();
    }

    private void showStudentView() {
        // Clear previous view and set up student tabs
        mainContentPanel.removeAll();
        JTabbedPane studentTabbedPane = createStudentViewTabs();
        mainContentPanel.add(studentTabbedPane, BorderLayout.CENTER);
        attachDashboardListeners(); 
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showStaffView() {
        // Clear previous view and set up staff panel
        mainContentPanel.removeAll();
        // NOTE: StaffPanel class must be available
        StaffPanel staffPanel = new StaffPanel(hostel, dashboardPanel); 
        mainContentPanel.add(staffPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JTabbedPane createStudentViewTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.addTab("Dashboard", dashboardPanel);
        // NOTE: AboutMePanel class must be available
        // This will now work correctly because AboutMePanel will get the
        // loggedInStudent from the hostel object.
        tabbedPane.addTab("About Me", new AboutMePanel(hostel)); 
        return tabbedPane;
    }
    
    private void attachDashboardListeners() {
        // Remove existing listeners to prevent duplicates if the view is re-shown
        for (java.awt.event.ActionListener al : dashboardPanel.getCleaningStatusButton().getActionListeners()) {
            dashboardPanel.getCleaningStatusButton().removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : dashboardPanel.getMessSubmitButton().getActionListeners()) {
            dashboardPanel.getMessSubmitButton().removeActionListener(al);
        }

        // --- Cleaning Status Listener ---
        dashboardPanel.getCleaningStatusButton().addActionListener(_ -> {
            hostel.setCleaningDoneToday(!hostel.isCleaningDoneToday());
            dashboardPanel.updateCleaningStatus();
        });
        
        // --- Mess Submit Listener ---
        dashboardPanel.getMessSubmitButton().addActionListener(_ -> {
            boolean attending = dashboardPanel.getMessCheckBox().isSelected();
            hostel.setMealPlanConfirmed(true); 
            
            String message = attending ? 
                              "Confirmed! You are registered for today's mess meal." : 
                              "Confirmed! You are opting out of today's mess meal.";
                              
            JOptionPane.showMessageDialog(this, message, "Meal Plan Submitted", JOptionPane.INFORMATION_MESSAGE);
            
            // FIX: Call the dedicated method on DashboardPanel to update its visual state
            dashboardPanel.updateMessSubmitButtonStatus();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
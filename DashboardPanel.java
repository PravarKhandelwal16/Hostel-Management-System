import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * A modern, interactive dashboard panel built using only standard Swing components.
 */
public class DashboardPanel extends JPanel {

    private Hostel hostel;

    public DashboardPanel(Hostel hostel) {
        this.hostel = hostel;
        setLayout(new BorderLayout(20, 20)); // Gaps between components
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding around the whole panel

        // --- 1. Food Wastage Banner (TOP) ---
        JLabel bannerLabel = new JLabel(
            String.format("Heads up! Yesterday's food wastage was %.1f kg. Let's try to reduce it!", hostel.getPreviousDayFoodWastage()),
            SwingConstants.CENTER
        );
        bannerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bannerLabel.setOpaque(true);
        bannerLabel.setBackground(new Color(255, 235, 235)); // Light red background
        bannerLabel.setForeground(new Color(192, 57, 43));    // Dark red text
        // Create a border with padding inside it for better looks
        bannerLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 179, 179), 1),
            new EmptyBorder(10, 10, 10, 10) // 10 pixels of padding
        ));
        add(bannerLabel, BorderLayout.NORTH);


        // --- 2. Main content panel (CENTER) ---
        JPanel mainContent = new JPanel(new GridLayout(2, 2, 20, 20)); // 2x2 grid with 20px gaps
        add(mainContent, BorderLayout.CENTER);

        // --- Mess Food Option ---
        JPanel messPanel = createFeaturePanel("Today's Mess Meal");
        JCheckBox messCheckBox = new JCheckBox("I will be eating in the mess today.");
        messCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messPanel.add(messCheckBox);
        mainContent.add(messPanel);

        // --- Cleaning Status ---
        JPanel cleaningPanel = createFeaturePanel("Daily Cleaning Status");
        String statusText = hostel.isCleaningDoneToday() ? "Completed" : "Pending";
        Color statusColor = hostel.isCleaningDoneToday() ? new Color(39, 174, 96) : new Color(243, 156, 18); // Green for done, Orange for pending
        JLabel cleaningStatusLabel = new JLabel(statusText);
        cleaningStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        cleaningStatusLabel.setForeground(statusColor);
        cleaningPanel.add(cleaningStatusLabel);
        mainContent.add(cleaningPanel);

        // --- Leave e-Permission ---
        JPanel leavePanel = createFeaturePanel("Leave Requests");
        JButton applyLeaveButton = new JButton("Apply for Leave");
        applyLeaveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leavePanel.add(applyLeaveButton);
        mainContent.add(leavePanel);

        // --- Next Year Availability ---
        JPanel availabilityPanel = createFeaturePanel("Future Availability");
        JButton checkAvailButton = new JButton("Check Next Year's Availability");
        checkAvailButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        availabilityPanel.add(checkAvailButton);
        mainContent.add(availabilityPanel);
    }

    // A helper method to create styled panels for a consistent look
    private JPanel createFeaturePanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout()); // Use GridBagLayout to center content
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            title,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 16),
            new Color(52, 73, 94) // A dark blue-gray for the title
        ));
        return panel;
}
}
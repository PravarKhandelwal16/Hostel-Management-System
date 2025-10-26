import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The main student-facing view showing key hostel status and actions.
 * Uses the preferred four-quadrant, card-based layout (image_46c42c.png).
 */
public class DashboardPanel extends JPanel {

    private Hostel hostel;
    
    // UI Components for dynamic updates and listeners
    private JLabel bannerLabel; 
    private JButton cleaningStatusButton; 
    private JCheckBox messCheckBox;
    private JButton messSubmitButton;
    private JButton checkPreviousLeavesButton; 
    private JButton refreshButton;

    // --- Constructor ---
    
    public DashboardPanel(Hostel hostel) {
        this.hostel = hostel;
        
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250)); // Light background

        // 1. Setup the Food Wastage Banner (NORTH)
        bannerLabel = createBannerLabel();
        add(bannerLabel, BorderLayout.NORTH);

        // 2. Setup the Four Quadrant Layout in the CENTER
        JPanel contentAndFooter = new JPanel(new BorderLayout(15, 15));
        contentAndFooter.setOpaque(false);
        
        // Quadrants (Grid 2x2)
        JPanel quadrantPanel = setupFourQuadrantLayout();
        contentAndFooter.add(quadrantPanel, BorderLayout.CENTER);
        
        // Footer (Refresh Button - aligned RIGHT)
        JPanel footerPanel = setupFooter();
        contentAndFooter.add(footerPanel, BorderLayout.SOUTH);
        
        add(contentAndFooter, BorderLayout.CENTER);
        
        // 3. Initial Data Load & Mess Plan check
        refreshDashboard();
    }
    
    // --- Layout Setup Methods ---

    private JLabel createBannerLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }
    
    private JPanel setupFooter() {
        // Use FlowLayout.RIGHT to position the button to the bottom right
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        
        refreshButton = new JButton("Refresh Data");
        styleButton(refreshButton, new Color(236, 240, 241), new Color(52, 73, 94), 13, false); 
        
        refreshButton.addActionListener(e -> {
            refreshDashboard();
            JOptionPane.showMessageDialog(this, "Dashboard data refreshed.", "Data Updated", JOptionPane.INFORMATION_MESSAGE);
        });
        
        footerPanel.add(refreshButton);
        return footerPanel;
    }

    private JPanel setupFourQuadrantLayout() {
        JPanel quadrantPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        quadrantPanel.setOpaque(false);
        
        quadrantPanel.add(createMessPanel());
        quadrantPanel.add(createCleaningStatusPanel());
        quadrantPanel.add(createLeaveRequestsPanel());
        quadrantPanel.add(createFutureAvailabilityPanel());
        
        return quadrantPanel;
    }
    
    // --- Quadrant Content Methods ---

    private JPanel createMessPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        messCheckBox = new JCheckBox("I will eat Dinner in the mess today");
        messCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        messCheckBox.setOpaque(false);

        messSubmitButton = new JButton("Submit"); // Initial text
        styleButton(messSubmitButton, new Color(52, 152, 219), Color.WHITE, 14, true); 
        messSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        messSubmitButton.setMaximumSize(new Dimension(200, 35));

        content.add(Box.createVerticalGlue());
        content.add(messCheckBox);
        content.add(Box.createVerticalStrut(10));
        content.add(messSubmitButton);
        content.add(Box.createVerticalGlue()); 

        return createCardPanel("Today's Dinner", "🍽️", content);
    }
    
    private JPanel createCleaningStatusPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        cleaningStatusButton = new JButton("Status Button"); // Text updated by updateCleaningStatus()
        cleaningStatusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cleaningStatusButton.setMaximumSize(new Dimension(200, 35));
        
        // Initial style is set in updateCleaningStatus() which is called during refreshDashboard()

        content.add(Box.createVerticalGlue());
        content.add(cleaningStatusButton);
        content.add(Box.createVerticalGlue());
        
        return createCardPanel("Daily Cleaning Status", "🧹", content);
    }

    private JPanel createLeaveRequestsPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        JButton applyButton = new JButton("Apply for Leave");
        styleButton(applyButton, new Color(137, 189, 236), Color.WHITE, 14, true); // Lighter Blue for Apply
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyButton.setMaximumSize(new Dimension(200, 35));
        
        checkPreviousLeavesButton = new JButton("Check Previous Leaves");
        styleButton(checkPreviousLeavesButton, new Color(155, 89, 182), Color.WHITE, 14, true); // Purple
        checkPreviousLeavesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkPreviousLeavesButton.setMaximumSize(new Dimension(200, 35));
        
        checkPreviousLeavesButton.addActionListener(e -> {
             JOptionPane.showMessageDialog(this, "Functionality to show previous leaves will be added here.", "Previous Leaves", JOptionPane.INFORMATION_MESSAGE);
        });

        content.add(Box.createVerticalGlue());
        content.add(applyButton);
        content.add(Box.createVerticalStrut(10));
        content.add(checkPreviousLeavesButton);
        content.add(Box.createVerticalGlue());

        return createCardPanel("Leave Requests", "📝", content);
    }
    
    private JPanel createFutureAvailabilityPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JButton checkAvailabilityButton = new JButton("Check Availability");
        styleButton(checkAvailabilityButton, new Color(52, 152, 219), Color.WHITE, 14, true); 
        checkAvailabilityButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkAvailabilityButton.setMaximumSize(new Dimension(200, 35));

        content.add(Box.createVerticalGlue());
        content.add(checkAvailabilityButton);
        content.add(Box.createVerticalGlue());
        
        return createCardPanel("Future Availability", "📅", content);
    }

    // --- Utility Methods ---
    
    private JPanel createCardPanel(String title, String iconText, JPanel contentPanel) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        
        Border line = BorderFactory.createLineBorder(new Color(230, 230, 230));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1), 
            new EmptyBorder(10, 10, 10, 10)));
        
        // Header (Icon and Title)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(iconText, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(44, 62, 80));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // Overloaded styleButton with hover effect option
    private void styleButton(JButton button, Color bgColor, Color fgColor, int fontSize, boolean enableHover) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        if (enableHover) {
             button.addMouseListener(new java.awt.event.MouseAdapter() {
                private Color originalColor = bgColor;
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    // Darken on hover
                    button.setBackground(originalColor.darker());
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    // Restore original color
                    button.setBackground(originalColor);
                }
            });
        }
    }

    // --- Dynamic Update Methods ---
    
    public void refreshDashboard() {
        updateBanner(); 
        updateCleaningStatus();
        updateMessSubmitButtonStatus(); // Initial check for mess status
    }
    
    public void updateMessSubmitButtonStatus() {
        if (hostel.isMealPlanConfirmed()) {
            messSubmitButton.setText("Plan Confirmed ✅");
            messSubmitButton.setEnabled(false);
            // Gray/Confirmed Style
            messSubmitButton.setBackground(new Color(189, 195, 199)); 
            // Remove any hover listeners from the disabled button
            for (java.awt.event.MouseListener ml : messSubmitButton.getMouseListeners()) {
                 messSubmitButton.removeMouseListener(ml);
            }
        } else {
            messSubmitButton.setText("Submit");
            messSubmitButton.setEnabled(true);
            // Blue/Active Style (called during create/refresh, hover enabled)
            styleButton(messSubmitButton, new Color(52, 152, 219), Color.WHITE, 14, true); 
        }
    }

    public void updateBanner() {
        double thisMonth = hostel.getThisMonthFoodWastage(); 
        double percentChange = hostel.getFoodWastageChangePercent();
        
        String trendText;
        Color trendColor;
        Color bgColor;

        if (percentChange > 0) {
            trendText = String.format("↑ %.1f%% more than last month. Let's improve!", percentChange);
            trendColor = new Color(192, 57, 43); 
            bgColor = new Color(255, 240, 240);
        } else if (percentChange < 0) {
            trendText = String.format("↓ %.1f%% less than last month. Let's aim lower!", Math.abs(percentChange));
            trendColor = new Color(39, 174, 96); 
            bgColor = new Color(235, 255, 240);
        } else {
            trendText = "Same as last month.";
            trendColor = new Color(52, 73, 94);
            bgColor = new Color(245, 245, 245);
        }

        bannerLabel.setText(String.format("This month's food wastage: %.1f kg — %s", thisMonth, trendText));
        bannerLabel.setForeground(trendColor);
        bannerLabel.setBackground(bgColor);
        this.revalidate();
    }
    
    public void updateCleaningStatus() {
        if (hostel.isCleaningDoneToday()) {
            cleaningStatusButton.setText("Completed 🗹");
            styleButton(cleaningStatusButton, new Color(46, 204, 113), Color.WHITE, 14, false); // Green (No hover on status button)
        } else {
            cleaningStatusButton.setText("Not Completed");
            styleButton(cleaningStatusButton, new Color(231, 76, 60), Color.WHITE, 14, false); // Red (No hover on status button)
        }
    }
    
    // --- Getters (Required by MainFrame for setting listeners) ---

    public JButton getCleaningStatusButton() {
        return cleaningStatusButton;
    }
    
    public JCheckBox getMessCheckBox() {
        return messCheckBox;
    }
    
    public JButton getMessSubmitButton() {
        return messSubmitButton;
    }
}
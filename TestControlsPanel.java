import javax.swing.*;
import java.awt.*;

public class TestControlsPanel extends JPanel {
    public TestControlsPanel(Hostel hostel) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("TEST CONTROLS PANEL (Admin View)", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
        setBackground(new Color(255, 235, 235));
    }
}
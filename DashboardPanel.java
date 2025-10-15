import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

class DashboardPanel extends JPanel {
   public DashboardPanel() {
      this.setLayout(new BorderLayout());
      JLabel var1 = new JLabel("Welcome to the Smart Hostel Management System", 0);
      var1.setFont(new Font("Segoe UI", 1, 24));
      this.add(var1, "Center");
   }
}
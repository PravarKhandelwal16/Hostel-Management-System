import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AboutMePanel extends JPanel {

    private Hostel hostel;
    private Student currentStudent; 

    public AboutMePanel(Hostel hostel) {
        this.hostel = hostel;
        
        // Assume the first student in the list is the "logged-in" student
        if (!hostel.getAllStudents().isEmpty()) {
            this.currentStudent = hostel.getAllStudents().get(0);
        } else {
            this.currentStudent = null;
        }

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(245, 247, 250)); 

        if (currentStudent != null) {
            add(createDetailsPanel(), BorderLayout.NORTH);
        } else {
            add(new JLabel("No student profile data found.", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)), 
                                        "My Profile Details", 
                                        TitledBorder.LEFT, TitledBorder.TOP, 
                                        new Font("Segoe UI", Font.BOLD, 16), new Color(44, 62, 80)));
        
        // 1. Student Name
        panel.add(createLabel("Name:"));
        panel.add(createValueLabel(currentStudent.getName()));

        // 2. Allotted Room
        panel.add(createLabel("Allotted Room:"));
        panel.add(createValueLabel(currentStudent.getRoomNumber()));
        
        // 3. Course
        panel.add(createLabel("Course:"));
        panel.add(createValueLabel(currentStudent.getCourse()));
        
        // 4. Year
        panel.add(createLabel("Year:"));
        panel.add(createValueLabel(currentStudent.getYear())); 
        
        // 5. Roll Number
        panel.add(createLabel("Roll Number:"));
        panel.add(createValueLabel(currentStudent.getRollNumber()));
        
        // 6. Mobile Number
        panel.add(createLabel("Mobile:"));
        panel.add(createValueLabel(currentStudent.getMobileNumber()));
        
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }
}
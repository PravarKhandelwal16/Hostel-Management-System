import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Staff panel to view all student-submitted daily reports.
 * Contains tabs for Mess, Cleaning, Leave, and Outpass.
 */
public class DailyStatusPanel extends JPanel {

    private DefaultTableModel messModel;
    private DefaultTableModel cleaningModel;
    private DefaultTableModel leaveModel;
    private JComboBox<String> leaveStatusCombo;
    private JTable leaveTable;

    // --- NEW: Outpass Components ---
    private DefaultTableModel outpassModel;
    private JComboBox<String> outpassStatusCombo;
    private JTable outpassTable;

    public DailyStatusPanel() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabbedPane.addTab("Today's Mess Attendance", createMessPanel());
        tabbedPane.addTab("Today's Cleaning Reports", createCleaningPanel());
        tabbedPane.addTab("Leave Requests", createLeavePanel());
        // --- NEW TAB ---
        tabbedPane.addTab("Outpass Requests", createOutpassPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Load data when panel is created
        loadMessData();
        loadCleaningData();
        loadLeaveData("Pending");
        loadOutpassData("Pending"); // Load pending outpasses by default
    }

    // --- Mess Panel ---
    private JPanel createMessPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Student Name", "Roll Number", "Room Number", "Attending"};
        messModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(messModel);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadMessData());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void loadMessData() {
        messModel.setRowCount(0); // Clear
        String sql = "SELECT s.name, s.roll_number, s.room_number, m.is_attending " +
                "FROM mess_attendance m " +
                "JOIN students s ON m.student_roll = s.roll_number " +
                "WHERE m.attendance_date = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messModel.addRow(new Object[]{
                            rs.getString("name"),
                            rs.getString("roll_number"),
                            rs.getString("room_number"),
                            rs.getBoolean("is_attending") ? "YES" : "NO"
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading mess data: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Cleaning Panel ---
    private JPanel createCleaningPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Student Name", "Roll Number", "Room Number", "Status"};
        cleaningModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(cleaningModel);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadCleaningData());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void loadCleaningData() {
        cleaningModel.setRowCount(0); // Clear
        String sql = "SELECT s.name, s.roll_number, s.room_number, c.is_cleaned " +
                "FROM cleaning_reports c " +
                "JOIN students s ON c.student_roll = s.roll_number " +
                "WHERE c.report_date = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cleaningModel.addRow(new Object[]{
                            rs.getString("name"),
                            rs.getString("roll_number"),
                            rs.getString("room_number"),
                            rs.getBoolean("is_cleaned") ? "Cleaned" : "Not Cleaned"
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading cleaning data: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Leave Panel ---
    private JPanel createLeavePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top filter bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Show requests with status:"));
        leaveStatusCombo = new JComboBox<>(new String[]{"Pending", "Approved", "Denied", "All"});
        leaveStatusCombo.addActionListener(e -> loadLeaveData((String) leaveStatusCombo.getSelectedItem()));
        filterPanel.add(leaveStatusCombo);

        // Table
        String[] columnNames = {"ID", "Student", "Roll Number", "Start", "End", "Reason", "Status"};
        leaveModel = new DefaultTableModel(columnNames, 0);
        leaveTable = new JTable(leaveModel);

        // Bottom button bar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton approveButton = new JButton("Approve Selected");
        JButton denyButton = new JButton("Deny Selected");

        approveButton.addActionListener(e -> updateLeaveStatus("Approved"));
        denyButton.addActionListener(e -> updateLeaveStatus("Denied"));

        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(leaveTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void loadLeaveData(String statusFilter) {
        leaveModel.setRowCount(0); // Clear
        String sql = "SELECT l.id, s.name, l.student_roll, l.start_date, l.end_date, l.reason, l.status " +
                "FROM leave_requests l " +
                "JOIN students s ON l.student_roll = s.roll_number ";

        if (!"All".equals(statusFilter)) {
            sql += "WHERE l.status = ? ";
        }
        sql += "ORDER BY l.start_date DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!"All".equals(statusFilter)) {
                pstmt.setString(1, statusFilter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    leaveModel.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("student_roll"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getString("reason"),
                            rs.getString("status")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading leave data: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLeaveStatus(String newStatus) {
        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a leave request from the table.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the ID from the first column of the selected row
        int leaveId = (int) leaveModel.getValueAt(selectedRow, 0);

        String sql = "UPDATE leave_requests SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, leaveId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Request status updated to " + newStatus + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the list
                loadLeaveData((String) leaveStatusCombo.getSelectedItem());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- NEW: Outpass Panel ---

    private JPanel createOutpassPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top filter bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Show requests with status:"));
        outpassStatusCombo = new JComboBox<>(new String[]{"Pending", "Approved", "Denied", "All"});
        outpassStatusCombo.addActionListener(e -> loadOutpassData((String) outpassStatusCombo.getSelectedItem()));
        filterPanel.add(outpassStatusCombo);

        // Table
        String[] columnNames = {"ID", "Student", "Roll", "Date", "Time Out", "Time In", "Reason", "Status"};
        outpassModel = new DefaultTableModel(columnNames, 0);
        outpassTable = new JTable(outpassModel);

        // Bottom button bar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton approveButton = new JButton("Approve Selected");
        JButton denyButton = new JButton("Deny Selected");

        approveButton.addActionListener(e -> updateOutpassStatus("Approved"));
        denyButton.addActionListener(e -> updateOutpassStatus("Denied"));

        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(outpassTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void loadOutpassData(String statusFilter) {
        outpassModel.setRowCount(0); // Clear
        String sql = "SELECT o.id, s.name, o.student_roll, o.outpass_date, o.time_out, o.time_in, o.reason, o.status " +
                "FROM outpass_requests o " +
                "JOIN students s ON o.student_roll = s.roll_number ";

        if (!"All".equals(statusFilter)) {
            sql += "WHERE o.status = ? ";
        }
        sql += "ORDER BY o.outpass_date DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!"All".equals(statusFilter)) {
                pstmt.setString(1, statusFilter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    outpassModel.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("student_roll"),
                            rs.getDate("outpass_date").toLocalDate(),
                            rs.getString("time_out"),
                            rs.getString("time_in"),
                            rs.getString("reason"),
                            rs.getString("status")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading outpass data: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOutpassStatus(String newStatus) {
        int selectedRow = outpassTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an outpass request from the table.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the ID from the first column
        int outpassId = (int) outpassModel.getValueAt(selectedRow, 0);

        String sql = "UPDATE outpass_requests SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, outpassId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Request status updated to " + newStatus + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the list
                loadOutpassData((String) outpassStatusCombo.getSelectedItem());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * Manages the connection to the MySQL database and initializes tables.
 */
public class DatabaseManager {

    // --- IMPORTANT: CHANGE THESE TO MATCH YOUR MYSQL SERVER ---
    // Using the freesqldatabase.com credentials you provided
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "hostel_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Pravar@161106";

    // The full JDBC URL
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    /**
     * Attempts to get a connection to the database.
     * @return A Connection object.
     * @throws SQLException if the connection fails.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Initializes the database.
     * Creates all necessary tables if they don't already exist.
     */
    public static void initializeDatabase() {
        String[] createTableStatements = {
                // students table
                "CREATE TABLE IF NOT EXISTS students (" +
                        "roll_number VARCHAR(50) PRIMARY KEY," +
                        "name VARCHAR(100) NOT NULL," +
                        "course VARCHAR(100)," +
                        "year VARCHAR(20)," +
                        "mobile VARCHAR(15)," +
                        "status VARCHAR(50) DEFAULT 'Active'," +
                        "room_number VARCHAR(20) DEFAULT 'Not Allocated'," +
                        "username VARCHAR(50) NOT NULL UNIQUE," +
                        "password VARCHAR(100) NOT NULL" +
                        ")",

                // rooms table
                "CREATE TABLE IF NOT EXISTS rooms (" +
                        "room_number VARCHAR(20) PRIMARY KEY," +
                        "type VARCHAR(50) NOT NULL," +
                        "capacity INT NOT NULL," +
                        "is_available BOOLEAN DEFAULT true" +
                        ")",

                // mess_attendance table
                "CREATE TABLE IF NOT EXISTS mess_attendance (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "student_roll VARCHAR(50) NOT NULL," +
                        "attendance_date DATE NOT NULL," +
                        "is_attending BOOLEAN NOT NULL," +
                        "UNIQUE KEY unique_attendance (student_roll, attendance_date)," +
                        "FOREIGN KEY (student_roll) REFERENCES students(roll_number) ON DELETE CASCADE" +
                        ")",

                // cleaning_reports table
                "CREATE TABLE IF NOT EXISTS cleaning_reports (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "student_roll VARCHAR(50) NOT NULL," +
                        "report_date DATE NOT NULL," +
                        "is_cleaned BOOLEAN NOT NULL," +
                        "UNIQUE KEY unique_report (student_roll, report_date)," +
                        "FOREIGN KEY (student_roll) REFERENCES students(roll_number) ON DELETE CASCADE" +
                        ")",

                // leave_requests table
                "CREATE TABLE IF NOT EXISTS leave_requests (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "student_roll VARCHAR(50) NOT NULL," +
                        "start_date DATE NOT NULL," +
                        "end_date DATE NOT NULL," +
                        "reason VARCHAR(255) NOT NULL," +
                        "status VARCHAR(20) DEFAULT 'Pending'," + // Pending, Approved, Denied
                        "FOREIGN KEY (student_roll) REFERENCES students(roll_number) ON DELETE CASCADE" +
                        ")",

                // --- NEW TABLE ---
                // outpass_requests table
                "CREATE TABLE IF NOT EXISTS outpass_requests (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "student_roll VARCHAR(50) NOT NULL," +
                        "outpass_date DATE NOT NULL," +
                        "time_out VARCHAR(10) NOT NULL," + // e.g., "14:30"
                        "time_in VARCHAR(10) NOT NULL," +  // e.g., "18:00"
                        "reason VARCHAR(255) NOT NULL," +
                        "status VARCHAR(20) DEFAULT 'Pending'," + // Pending, Approved, Denied
                        "FOREIGN KEY (student_roll) REFERENCES students(roll_number) ON DELETE CASCADE" +
                        ")"
        };

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Connecting to database and creating tables if not exist...");
            for (String sql : createTableStatements) {
                stmt.executeUpdate(sql);
            }
            System.out.println("Database tables are ready.");

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database Connection Failed!\n" + e.getMessage() +
                            "\nPlease ensure the MySQL server is running and credentials in DatabaseManager.java are correct.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if DB connection fails on startup
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "MySQL JDBC Driver not found!\n" +
                            "Please add the 'mysql-connector-j-X.X.X.jar' to your project's libraries.",
                    "Driver Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}


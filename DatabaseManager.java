import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane; // --- FIX: Added the missing import ---

/**
 * Manages the connection to the MySQL database and initializes tables.
 */
public class DatabaseManager {

    // --- !!! YOU MUST EDIT THESE VALUES !!! ---
    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12804690"; // Your database URL
    private static final String DB_USER = "sql12804690";    // Your MySQL username
    private static final String DB_PASS = "13DgzEj4Gd";        // Your MySQL password
    // -------------------------------------------

    /**
     * Gets a new connection to the database.
     * @return A database Connection object.
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        // This connection will have auto-commit ON by default, which is fine
        // for our simple, single-statement operations.
        // For the complex allocation, we will manage transactions manually in that panel.
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Initializes the database by creating tables if they don't exist.
     * This is called once when MainFrame starts.
     */
    public static void initializeDatabase() {
        // This SQL creates the 'students' table
        String createStudentsTableSQL = "CREATE TABLE IF NOT EXISTS students ("
                + "roll_number VARCHAR(50) PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "course VARCHAR(100),"
                + "year VARCHAR(20),"
                + "mobile VARCHAR(20),"
                + "status VARCHAR(50) DEFAULT 'Active',"
                + "room_number VARCHAR(20) DEFAULT 'Not Allocated',"
                + "username VARCHAR(50) NOT NULL UNIQUE,"
                + "password VARCHAR(100) NOT NULL"
                + ");";

        // This SQL creates the 'rooms' table
        String createRoomsTableSQL = "CREATE TABLE IF NOT EXISTS rooms ("
                + "room_number VARCHAR(20) PRIMARY KEY,"
                + "type VARCHAR(50) NOT NULL,"
                + "capacity INT NOT NULL,"
                + "is_available BOOLEAN NOT NULL DEFAULT 1" // 1 = true (Available), 0 = false (Full)
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Connecting to database and initializing tables...");
            stmt.execute(createStudentsTableSQL);
            stmt.execute(createRoomsTableSQL);
            System.out.println("Tables 'students' and 'rooms' are ready.");

        } catch (SQLException e) {
            e.printStackTrace();
            // --- FIX: Corrected JOptionPane ---
            JOptionPane.showMessageDialog(null,
                    "Database connection failed!\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE); // --- FIX: Corrected JOptionPane ---
            System.exit(1); // Exit if we can't connect
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // --- FIX: Corrected JOptionPane ---
            JOptionPane.showMessageDialog(null,
                    "MySQL JDBC Driver not found!\n" + e.getMessage(),
                    "Driver Error",
                    JOptionPane.ERROR_MESSAGE); // --- FIX: Corrected JOptionPane ---
            System.exit(1);
        }
    }
}


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteRow {
    static final String DB_URL = "jdbc:mysql://localhost:3306/jobapps";
    static final String USER = "root";
    static final String PASS = "xxxx";
    public void delete(String compName) { 
        String deleteSQL = "DELETE FROM jobapps WHERE comp_name = ?";  
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, compName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Row deleted successfully.");
            } else {
                System.out.println("No rows deleted. Check your condition.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception
        }
    }
}

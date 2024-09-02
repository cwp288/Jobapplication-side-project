import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindRow {
    static final String DB_URL = "jdbc:mysql://localhost:3306/jobapps";
    static final String USER = "root";
    static final String PASS = "xxxx";
    public void findName(String compName) {
        String selectSQL = "SELECT * FROM jobapps WHERE comp_name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, compName); 
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("comp_name"); //Finding in the database and assigning values 
                String pos = rs.getString("position");
                String date = rs.getString("date");
                String loc = rs.getString("location");
                int urgency = rs.getInt("urgency");
                String site = rs.getString("site");

                
                //Printing the values out.
                System.out.println("Company Name : " + name + " \nPosition : " + pos + "\nDate Applied : " + date + "\nLocation : " + loc + "\nUrgency : " + urgency + "\nSite Found : " + site);
            } else {
                System.out.println("No row found with the specified value.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception
        }
    }
}

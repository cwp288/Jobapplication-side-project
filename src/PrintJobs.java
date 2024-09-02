
import java.sql.*;

public class PrintJobs {
   static final String DB_URL = "jdbc:mysql://localhost:3306/jobapps";
   static final String USER = "root";
   static final String PASS = "xxxx";
   static final String QUERY = "SELECT * FROM jobapps";

   public void print() {
      try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(QUERY);) {
         while (rs.next()) {
            System.out.print("Company Name : " + rs.getString("comp_name"));
            System.out.print(", Position: " + rs.getString("position"));
            System.out.println(", Date: " + rs.getString("date"));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } 
   }
}
 
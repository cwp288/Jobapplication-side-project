import java.sql.*;

public class AddRow {
   static final String DB_URL = "jdbc:mysql://localhost:3306/jobapps";
   static final String USER = "root";
   static final String PASS = "xxxx";

   public void add(String compName, String position, String date, String location, int urgency, String site){
      // Open a connection
      try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO jobapps (comp_name, position, date, location, urgency, site) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, compName); 
            pstmt.setString(2, position);
            pstmt.setString(3,date);
            pstmt.setString(4,location);
            pstmt.setInt(5,urgency);
            pstmt.setString(6,site);
            pstmt.executeUpdate();
            System.out.println("Complete!");
      } catch (SQLException e) {
         e.printStackTrace();
      } 
   }
}



//Before edits
/*
 * public class App {
   static final String DB_URL = "jdbc:mysql://localhost:3306/jobapps";
   static final String USER = "root";
   static final String PASS = "xxxx";
   static final String QUERY = "SELECT * FROM jobapps";

   public static void main(String[] args) {
      // Open a connection
      try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(QUERY);) {
         // Extract data from result set
         while (rs.next()) {
            // Retrieve by column name
            System.out.print("Roll: " + rs.getString("roll"));
            System.out.print(", Name: " + rs.getString("name"));
            System.out.println(", CGPA: " + rs.getFloat("cgpa"));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } 
   }
}
 */
import java.sql.*;
public class RemoveEmployee {
    public void remove_emp(String remove_emp) throws Exception{
        
        String dburl = "jdbc:mysql://localhost:3306/hotel_db";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.cj.jdbc.Driver"; 
         Connection con = null;

        try {
            Class.forName(drivername);
            con = DriverManager.getConnection(dburl, dbuser, dbpass);
            if (con != null) {
            } else {
                System.out.println("Failed to connect to database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM employee_details WHERE emp_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
            ps = con.prepareStatement(query);
            ps.setString(1, remove_emp);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee removed successfully");
            } else {
                System.out.println("Employee not found");
            }
    } 
}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddRoom{
    int room_no;
    String room_type;
    int room_price;
    String room_status;
    String room_cleaning_status; 
    Connection con; 
    public AddRoom(String room_type, int room_price, String room_status, String room_cleaning_status) {
        // this.room_no = room_no;
        this.room_type = room_type;
        this.room_price = room_price;
        this.room_status = room_status;
        this.room_cleaning_status = room_cleaning_status;

        // Initialize database connection
        String dburl = "jdbc:mysql://localhost:3306/hotel_db";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.cj.jdbc.Driver"; 

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
    }

    public void insert_room() {
        String insert_room_sql = "INSERT INTO room_details(room_type,room_price,room_status,cleaning_status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insert_room_sql)) {
            ps.setString(1, room_type);
            ps.setInt(2, room_price);
            ps.setString(3, room_status);
            ps.setString(4, room_cleaning_status);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Room added successfully");
            } else {
                System.out.println("Failed to add room");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "[room_no=" + room_no + ", room_type=" + room_type + ", room_price=" + room_price
                + ", room_status=" + room_status + ", room_cleaning_status=" + room_cleaning_status + "]";
    }

    public static void print_roomList(ArrayList<AddRoom> roomList) {
        for (AddRoom room : roomList) {
            System.out.println(room);
        }
    }
}

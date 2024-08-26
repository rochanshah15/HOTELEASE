import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchRooms {
    Connection con;
    public SearchRooms() {
        String dburl = "jdbc:mysql://localhost:3306/hotel_db";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.jdbc.Driver";

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
    
    public void search_rooms(String bed_type, int maxPrice) throws SQLException {
        String query = "SELECT * FROM room_details";
        ArrayList<Object[]> roomList = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] room = new Object[5];
                room[0] = rs.getString("room_no");         
                room[1] = rs.getDouble("room_price");         
                room[2] = rs.getString("room_type");          
                room[3] = rs.getString("room_status");       
                room[4] = rs.getString("cleaning_status");
                roomList.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.sort(roomList, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] r1, Object[] r2) {
                double price1 = (double) r1[1];
                double price2 = (double) r2[1];
                return Double.compare(price1, price2);
            }
        });
        int index = binarySearch(roomList, bed_type, maxPrice);

        if (index != -1) {
            Object[] room = roomList.get(index);
            System.out.println("RoomNo : " + room[0]);
            System.out.println("RoomPrice : " + room[1]);
            System.out.println("RoomType : " + room[2]);
            System.out.println("RoomStatus : " + room[3]);
            System.out.println("RoomCleaningStatus : " + room[4]);
        } else {
            System.out.println("No rooms found matching criteria.");
        }
    }

    private int binarySearch(ArrayList<Object[]> rooms, String bedType, int maxPrice) {
        int low = 0;
        int high = rooms.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Object[] midRoom = rooms.get(mid);

            double price = (double) midRoom[1];
            String type = (String) midRoom[2];

            if (price <= maxPrice && type.equalsIgnoreCase(bedType)) {
                return mid; 
            } else if (price > maxPrice) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }
}


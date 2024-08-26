import java.sql.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class DisplayAll {
    Connection con;

    public DisplayAll() {
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

    public void DisplayCustomers() throws SQLException {
        String callProcedure = "{CALL GetCustomerDetails()}";
        try (CallableStatement cs = con.prepareCall(callProcedure);
                ResultSet rs = cs.executeQuery()) {
            System.out.println("DETAILS : ");
            while (rs.next()) {
                System.out.println("CUSTOMER ID : " + rs.getString("customer_id")
                        + "\nCUSTOMER NAME : " + rs.getString("customer_name")
                        + "\nCUSTOMER PHONE : " + rs.getString("customer_ph")
                        + "\nCUSTOMER AGE : " + rs.getString("customer_age")
                        + "\nCUSTOMER GENDER : " + rs.getString("gender")
                        + "\nCUSTOMER ROOM : " + rs.getString("allocated_roomNo")
                        + "\nCHECK IN TIME : " + rs.getString("checkIn")
                        + "\nPAYMENT DONE : " + rs.getString("payment_status"));
                System.out.println("--------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void displayRoomsByPrice() throws SQLException {
        String query1 = "SELECT COUNT(*) AS room_count FROM room_details";
        int roomcount = 0;

        try (PreparedStatement pst = con.prepareStatement(query1)) {
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                roomcount = rs.getInt("room_count");
            }
            System.out.println("TOTAL ROOMS IN HOTEL : " + roomcount);
        }
        String display = "SELECT * FROM room_details";
        ArrayList<Object[]> roomList = new ArrayList<>();

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(display)) {
            while (rs.next()) {
                Object[] room = new Object[3];
                room[0] = rs.getString("room_price");
                room[1] = rs.getString("room_no");
                room[2] = rs.getString("room_type");
                roomList.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < roomList.size() - 1; i++) {
            for (int j = 0; j < roomList.size() - i - 1; j++) {
                if (Double.parseDouble((String) roomList.get(j)[0]) < Double
                        .parseDouble((String) roomList.get(j + 1)[0])) {
                    Object[] temp = roomList.get(j);
                    roomList.set(j, roomList.get(j + 1));
                    roomList.set(j + 1, temp);
                }
            }
        }
        for (Object[] room : roomList) {
            System.out.println("ROOM DETAILS : ");
            System.out.println("ROOM NO : " + room[1]);
            System.out.println("ROOM PRICE : " + room[0]);
            System.out.println("ROOM TYPE : " + room[2]);
            System.out.println("---------------------------------");
        }
    }

    public void DisplayEmployees() {
        String procedureCall = "{CALL GetEmployeeDetails()}";
        try (CallableStatement cstmt = con.prepareCall(procedureCall);
                ResultSet rs = cstmt.executeQuery()) {

            System.out.println("DETAILS : ");
            while (rs.next()) {
                System.out.println("EMPLOYEE ID : " + rs.getString("emp_id")
                        + "\nEMPLOYEE NAME : " + rs.getString("emp_name")
                        + "\nEMPLOYEE SALARY : " + rs.getString("emp_salary")
                        + "\nEMPLOYEE DESIGNATION : " + rs.getString("emp_designation")
                        + "\nEMPLOYEE PHONE : " + rs.getString("emp_phoneno")
                        + "\nEMPLOYEE MAIL : " + rs.getString("emp_email")
                        + "\nEMPLOYEE AGE : " + rs.getString("emp_age")
                        + "\nEMPLOYEE GENDER : " + rs.getString("emp_gender"));
                System.out.println("---------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

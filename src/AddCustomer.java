import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class AddCustomer {
    String customer_name;
    String phone_number;
    int customer_age;
    String gender;
    int customer_room;
    String checkin;
    boolean payment;
    boolean checkOut;

    public AddCustomer(String customer_name, String phone_number, int customer_age, String gender, int customer_room,
                       String checkin, boolean payment, boolean checkOut) throws SQLException {
        this.customer_name = customer_name;
        this.phone_number = phone_number;
        this.customer_age = customer_age;
        this.gender = gender;
        this.customer_room = customer_room;
        this.checkin = checkin;
        this.payment = payment;
        this.checkOut = checkOut;
    }
    @Override
    public String toString() {
        return "AddCustomer [customer_name=" + customer_name + ", phone_number=" + phone_number + ", customer_age="
                + customer_age + ", gender=" + gender + ", customer_room=" + customer_room + ", checkin=" + checkin
                + ", payment=" + payment + ", checkOut=" + checkOut + "]";
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getCustomer_age() {
        return customer_age;
    }

    public String getGender() {
        return gender;
    }

    public int getCustomer_room() {
        return customer_room;
    }

    public String getCheckin() {
        return checkin;
    }

    public boolean isPayment() {
        return payment;
    }

    public boolean isCheckOut() {
        return checkOut;
    }
  
}

class SinglyList {
    class Node {
        AddCustomer cust;
        Node next;

        Node(AddCustomer cust) {
            this.cust = cust;
            this.next = null;
        }
    }

    Node head = null;
    Connection con;

    public SinglyList() {
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

    // Add customer to linked list
    public void add(AddCustomer cust) {
        Node newNode = new Node(cust);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void displayCustomers() {
        Node current = head;
        while (current != null) {
            System.out.println(current.cust);
            current = current.next;
        }
    }

    public void insertAllCustomersToDB(int customer_room) throws SQLException {
        String changeRoomStatus = "update room_details set room_status = ? where room_no = ?";
        PreparedStatement pst = con.prepareStatement(changeRoomStatus);
        pst.setString(1, "booked");
        pst.setInt(2, customer_room);
        pst.execute();
        String insert_customer = "INSERT INTO customer_details(customer_name, customer_ph, customer_age, gender, allocated_roomNo, checkIn, payment_status, checkOut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insert_customer)) {
            Node current = head;
            while (current != null) {
                AddCustomer cust = current.cust;
                ps.setString(1, cust.getCustomer_name());
                ps.setString(2, cust.getPhone_number());
                ps.setInt(3, cust.getCustomer_age());
                ps.setString(4, cust.getGender());
                ps.setInt(5, cust.getCustomer_room());
                ps.setString(6, cust.getCheckin());
                ps.setBoolean(7, cust.isPayment());
                ps.setBoolean(8, cust.isCheckOut());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Customer added successfully: " + cust.getCustomer_name());
                } else {
                    System.out.println("Failed to add customer: " + cust.getCustomer_name());
                }
                current = current.next;
            }
          
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void bookroom(int customerRoom) throws SQLException{
      
    }
}

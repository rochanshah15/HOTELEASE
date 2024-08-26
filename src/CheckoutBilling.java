
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class CustomStack<T> {
    private Object[] stack;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    public CustomStack() {
        stack = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    public void push(T item) {
        if (size == stack.length) {
            ensureCapacity();
        }
        stack[size++] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        T item = (T) stack[--size];
        stack[size] = null; 
        return item;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    private void ensureCapacity() {
        int newCapacity = stack.length * 2;
        Object[] newStack = new Object[newCapacity];
        System.arraycopy(stack, 0, newStack, 0, stack.length);
        stack = newStack;
    }

    public int size() {
        return size;
    }
}
class BillingDetails {
    String customerName;
    int roomNumber;
    int roomPrice;

    public BillingDetails(String customerName, int roomNumber, int roomPrice) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
    }

    @Override
    public String toString() {
        return "Customer Name: " + customerName + ", Room Number: " + roomNumber + ", Room Price: " + roomPrice;
    }
}

public class CheckoutBilling {
    private Connection con;
    private CustomStack<BillingDetails> billingStack;

    public CheckoutBilling() {
        billingStack = new CustomStack<>();

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

    public void checkout(int roomNo) throws InterruptedException {
        String changeStatus = "UPDATE room_details SET room_status = ? WHERE room_no = ?";
        PreparedStatement ps = null;
        String changeCHECKOUT = "UPDATE customer_details SET checkOut = ? WHERE allocated_roomNo = ?";
        try {
            ps = con.prepareStatement(changeStatus);
            ps.setString(1, "available");
            ps.setInt(2, roomNo);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Room " + roomNo + " room status updated to available.");
            } else {
                System.out.println("Room " + roomNo + " not found.");
            }
            ps = con.prepareStatement(changeCHECKOUT);
            ps.setBoolean(1, true);
            ps.setInt(2, roomNo);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Room service is cleaning the room...");
        Thread.sleep(1000);
        System.out.println("Please wait...");
        Thread.sleep(3000);
        System.out.println("Room cleaned successfully");
    }

    public void billFile(String cust_name, int room_no) throws IOException, SQLException {
        String get = "SELECT room_price FROM room_details WHERE room_no = ?";
        PreparedStatement ps = con.prepareStatement(get);
        ps.setInt(1, room_no);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int roomPrice = rs.getInt("room_price");

            BillingDetails billingDetails = new BillingDetails(cust_name, room_no, roomPrice);
            billingStack.push(billingDetails);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("d:\\BillHistory.txt", true))) {
                bw.write("BILL ");
                while (!billingStack.isEmpty()) {
                    BillingDetails details = billingStack.pop();
                    bw.write("\n" + details.toString());
                }
            }
            System.out.println("Billing details have been written to d:\\BillHistory.txt");
        }
    }
}


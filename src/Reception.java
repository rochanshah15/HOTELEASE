import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reception {
       public static void receptionist_login() {
        Scanner sc = new Scanner(System.in);
        boolean check = true;
        do {
            System.out.println("1.DISPLAY ROOMS : ");
            System.out.println("2. NEW CUSTOMER FORM");
            System.out.println("3.SEARCH FOR ROOMS");
            System.out.println("4.ALL EMPLOYEE DETAILS");
            System.out.println("5.ALL CUSTOMER DETAILS");
            System.out.println("6.CHECK OUT AND BILLING");
            System.out.println("7.LOG OUT");
            try {
                System.out.println("SELECT CHOICE : ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1 :
                    DisplayAll display = new DisplayAll();
                    display.displayRoomsByPrice();
                    break;
                    case 2 :
                    NewCustomer();
                    break;
                    case 3 :
                    RoomSearch();
                    break;
                    case 4 : 
                    DisplayAll emp = new DisplayAll();
                    emp.DisplayEmployees();
                    break;
                    case 5 : 
                    DisplayAll cust = new DisplayAll();
                    cust.DisplayCustomers();
                    break;
                    case 6 :
                    Billing();
                    break;
                    case 7 :
                    System.out.println("RETURNING TO MAIN SCREEN...");
                    Thread.sleep(2000);
                    check = false;
                    break;
                    default : System.out.println("ENTER VALID INPUT");
                    break;
                }
            } catch (Exception e) {
                System.out.println("ENTER VALID INPUT ");
            }
        } while (check);
}

private static void Billing() {
    Scanner sc = new Scanner(System.in);
    boolean validInput = false;
    boolean chk;
    while (!validInput) {
        try {
            String cust_name = "";
            int room_no = 0;
    
            while (true) {
                System.out.println("ENTER CUSTOMER NAME: ");
                cust_name = sc.nextLine().trim();
                if (!cust_name.isEmpty() && cust_name.matches("[a-zA-Z\\s]+")) {
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter a valid name.");
                    sc.next();
                }
            }
    
            while (true) {
                System.out.println("ENTER ROOM NO: ");
                if (sc.hasNextInt()) {
                    room_no = sc.nextInt();
                    if (room_no > 0) {
                        break; 
                    } else {
                        System.out.println("Invalid input. Room number must be a positive integer.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    sc.next(); 
                }
            }
             chk = name_check(cust_name, room_no);
            if (chk != true) {
                System.out.println("INVALID NAME OR ROOM NO.");
                validInput = false;
                
            } else {
                CheckoutBilling billing = new CheckoutBilling();
                billing.checkout(room_no);
                billing.billFile(cust_name, room_no);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            sc.nextLine(); 
        }
    }
    }

private static void RoomSearch() throws SQLException {
    Scanner sc = new Scanner(System.in);
    String bed_type = "";
        int room_price = 0;

        while (true) {
            System.out.println("BED TYPE (SINGLE/DOUBLE): ");
            bed_type = sc.next();
            if (bed_type.equalsIgnoreCase("SINGLE") || bed_type.equalsIgnoreCase("DOUBLE")) {
                break; 
            } else {
                System.out.println("Invalid input. Please enter 'SINGLE' or 'DOUBLE'.");
            }
        }
        while (true) {
            System.out.println("PRICE: ");
            if (sc.hasNextInt()) {
                room_price = sc.nextInt();
                if (room_price > 0) {
                    break; 
                } else {
                    System.out.println("Invalid input. Price must be a positive integer.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.next(); 
            }
        }

    SearchRooms rooms = new SearchRooms();
    rooms.search_rooms(bed_type,room_price);
    }

private static void NewCustomer() throws SQLException {
    Scanner sc = new Scanner(System.in);
    String phoneNumber = "";
    String gender = "";
   
    String customer_name = "";
    while (true) {
        System.out.print("CUSTOMER NAME: ");
        customer_name = sc.nextLine().trim();
        if (customer_name.isEmpty()) {
            System.out.println("Invalid input.Please try again.");
        } else if (!customer_name.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid input: Name must contain only letters. Please try again.");
        } else {
            break;  
        }
    }

    boolean ph_check = true;
    while (ph_check) {
        System.out.println("PHONE NO: ");
        phoneNumber = sc.next();
        try {
            if (phoneNumber.length() != 10) {
                throw new InvalidPhoneNumberException("INVALID PHONE NUMBER!.");
            }
            for (int i = 0; i < phoneNumber.length(); i++) {
                if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') {
                    throw new InvalidPhoneNumberException("Phone number contains invalid characters.");
                }
            }
            ph_check = false;
        } catch (InvalidPhoneNumberException e) {
            System.out.println(e.getMessage());
        }
    }

    boolean age_check = true;
    int customer_age = 0;
    while (age_check) {
        System.out.print("Enter your age: ");
        try {
            customer_age = sc.nextInt();
            if (customer_age >= 1 && customer_age <= 100) {
                age_check = false;
            } else {
                System.out.println("Invalid age. Please enter an age between 1 and 100.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a numeric value for age.");
            sc.next(); 
        }
    }

    boolean gender_check = false;
    while (!gender_check) {
        System.out.println("ENTER GENDER (Male, Female, Other): ");
        gender = sc.next();
        if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Other")) {
            gender_check = true;
        } else {
            System.out.println("Invalid gender input. Please try again.");
        }
    }
    boolean roomCheck = false;
    int customerRoom = 0;

    while (!roomCheck) {
        System.out.print("ALLOCATE ROOM NO: ");
        try {
            System.out.println("TYPE 'YES' TO SEE ROOM DETAILS OR ELSE NO: ");
            String ans = sc.next();
            if (ans.equalsIgnoreCase("YES")) {
                DisplayAll display = new DisplayAll();
                display.displayRoomsByPrice();
            }
            System.out.println("NOW ALLOCATE ROOM NO TO CUSTOMER : ");
            customerRoom = sc.nextInt();
            boolean status = roomAvailable(customerRoom);
            if (status) {
                roomCheck = true;
            } else {
                System.out.println("Room is not available, please enter another room number.");
            }
        } catch (Exception e) {
            System.out.println("ENTER VALID NUMBER");
            sc.next();
        }
    }
    boolean validTime = false;
    String checkin = "";
    while (!validTime) {
        System.out.print("Enter check-in time (HH:mm): ");
        checkin = sc.next();

        if (checkin.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            validTime = true;
            System.out.println("check-in time entered: " + checkin);
        } else {
            System.out.println("Invalid time format or input. Please enter time in HH:mm format.");
        }
    }

    System.out.println("PAYMENT STATUS : ");
    boolean ans = isPaid();
    boolean payment = ans;
    AddCustomer customer = new AddCustomer(customer_name, phoneNumber, customer_age , gender, customerRoom, checkin, payment,false);
   // customer.bookroom();
    SinglyList dll = new SinglyList();
    dll.add(customer);
    dll.insertAllCustomersToDB(customerRoom);
    }

private static boolean isPaid() {
    Scanner scanner = new Scanner(System.in);
    boolean paymentSuccessful = false;

        String payMode;
        while (true) {
            System.out.print("PLEASE ENTER PAYMENT MODE (ONLINE/OFFLINE): ");
            payMode = scanner.nextLine().trim();

            if (payMode.equalsIgnoreCase("offline")) {
                System.out.println("Offline payment selected. Marking payment as successful.");
                paymentSuccessful = true;
                break;
            } else if (payMode.equalsIgnoreCase("online")) {
                System.out.println("Online payment selected. Proceeding to payment details.");
                break;
            } else {
                System.out.println("Invalid input: Please enter either 'ONLINE' or 'OFFLINE'.");
            }
        }

        if (payMode.equalsIgnoreCase("online")) {
            String cardNumber, expiryDate, cvv;

            while (true) {
                System.out.print("Enter card number (16 digits): ");
                cardNumber = scanner.nextLine().trim();

                if (cardNumber.matches("\\d{16}")) {
                    break;
                } else {
                    System.out.println("Invalid input: Card number must be exactly 16 digits.");
                }
            }

            while (true) {
                System.out.print("Enter expiration date (dd/mm/yyyy): ");
                expiryDate = scanner.nextLine().trim();
    
                if (expiryDate.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
                    int year = Integer.parseInt(expiryDate.substring(6, 10));
                    if (year > 2024) {
                        System.out.println("Valid expiration date entered: " + expiryDate);
                        break;
                    } else {
                        System.out.println("The year must be above 2024.");
                    }
                } else {
                    System.out.println("Please enter the date in dd/mm/yyyy format.");
                }
            }

            while (true) {
                System.out.print("Enter CVV (3 digits): ");
                cvv = scanner.nextLine().trim();

                if (cvv.matches("\\d{3}")) {
                    break;
                } else {
                    System.out.println("Invalid input: CVV must be exactly 3 digits.");
                }
            }

        if (!cardNumber.isEmpty() && !expiryDate.isEmpty() && !cvv.isEmpty()) {
            System.out.println("Processing payment...");
            System.out.println("PLEASE WAIT...");
            try {
                Thread.sleep(2000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Payment processed successfully.");
            paymentSuccessful = true; 
        } else {
            System.out.println("Payment failed. Invalid payment details.");
            paymentSuccessful = false; 
        }
    } 
    return paymentSuccessful;
}

private static boolean name_check(String cust_name,int room_no) throws SQLException {
    String dbUrl = "jdbc:mysql://localhost:3306/hotel_db";
        String dbUser = "root";
        String dbPass = "";
        String driverName = "com.mysql.cj.jdbc.Driver";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = "";
        int roomStatus = 0;
        boolean correct = true;
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            String check = "select customer_name, allocated_roomNo from customer_details where customer_name = ? and allocated_roomNo = ?";
            ps = con.prepareStatement(check);
            ps.setString(1, cust_name);
            ps.setInt(2, room_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                status = rs.getString("customer_name");
                roomStatus = rs.getInt("allocated_roomNo");
                correct = true;
            } else {
                System.out.println("Customer not found.");
                correct = false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(correct == true){
            return true;
        }
        else{
            return false;
        }
}

private static boolean roomAvailable(int roomNo) throws SQLException {
    String dbUrl = "jdbc:mysql://localhost:3306/hotel_db";
    String dbUser = "root";
    String dbPass = "";
    String driverName = "com.mysql.cj.jdbc.Driver";

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String status = "";
    try {
        Class.forName(driverName);
        con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        if (con != null) {
        } else {
            System.out.println("Failed to connect to database");
            return false;
        }
        String roomSearch = "SELECT room_status FROM room_details WHERE room_no = ?";
        ps = con.prepareStatement(roomSearch);
        ps.setInt(1, roomNo);
        rs = ps.executeQuery();

        if (rs.next()) {
         status = rs.getString("room_status");
            
        } 
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }
    if(status.equalsIgnoreCase("available")){
        return true;
    }
    else{
        return false;
    }
}
}
class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}

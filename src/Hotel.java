//ADMIN ID = rochan15 and PASS = 2005
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Hotel {
    static Connection con = null;
    public static void main(String[] args) throws Exception {
        String dburl = "jdbc:mysql://localhost:3306/hotel_db";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.jdbc.Driver";
        Class.forName(drivername);
        con = DriverManager.getConnection(dburl, dbuser, dbpass);
        if (con != null) {
        } else {
            System.out.println("not connected");
        }
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        System.out.println("WELCOME TO HOTELEASE : Your Gateway to Seamless Service");
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        Scanner sc = new Scanner(System.in);
        boolean check = true;
        do {
            System.out.println("1.ADMIN LOGIN");
            System.out.println("2.RECEPTIONIST LOGIN");
            System.out.println("3.EXIT!");
            try {
                System.out.println("Enter your choice: ");
                int login_choice = sc.nextInt();
                switch (login_choice) {
                    case 1:
                        AdminLogin();
                        break;

                    case 2:
                        ReceptionLogin();
                        break;

                    case 3:
                    System.out.println("THANK YOU FOR VISITING!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next();
            }
        } while (check);
    }

    private static void ReceptionLogin() {
        Scanner sc = new Scanner(System.in);
        boolean select = true;
        do {
            System.out.println("1.LOGIN");
            System.out.println("2.SIGN UP(NEW)");
            System.out.println("3.RETURN TO MAIN SCREEN");
            try {
                System.out.println("ENTER YOUR CHOICE : ");
                int receptionist_choice = sc.nextInt();
                switch (receptionist_choice) {
                    case 1:
                        System.out.println("ENTER ID : ");
                        String reception_id = sc.next();
                        System.out.println("ENTER PASSWORD : ");
                        String password = sc.next();
                        String search_user = "select * from receptionist_login where id = ? and password = ?";
                        PreparedStatement ps = con.prepareStatement(search_user);
                        ps.setString(1, reception_id);
                        ps.setString(2, password);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            System.out.println("**********WELCOME RECEPTIONIST**********");
                            Reception receptionistlog = new Reception();
                            receptionistlog.receptionist_login();
                        } else {
                            System.out.println("USER NOT REGISTERED!");
                            System.out.println("PLEASE SIGNUP!");
                        }
                        break;
                    case 2:
                        System.out.println("************WELCOME TO SIGN UP PAGE***********");
                        String reception_id1;

                        while (true) {
                            System.out.println("ENTER ID: ");
                            reception_id1 = sc.next().trim();

                            if (reception_id1.isEmpty()) {
                                System.out.println("ID cannot be empty. Please try again.");
                            } else if (!reception_id1.matches("[a-zA-Z0-9]+")) {
                                System.out.println("ID can only contain letters and numbers. Please try again.");
                            } else {
                                break; 
                            }
                        }
                        String password1;
                        while (true) {
                            System.out.println("ENTER PASSWORD: ");
                            password1 = sc.next();

                            if (password1.length() >= 8 && password1.matches(".*[A-Z].*")
                                    && password1.matches(".*[a-z].*") && password1.matches(".*\\d.*")
                                    && password1.matches(".*[!@#$%^&*()].*")) {
                                System.out.println("Strong password set.");
                                break;
                            } else {
                                System.out.println(
                                        "Password must be at least 8 characters long and include uppercase letters, lowercase letters, digits, and special characters.");
                            }
                        }
                        String insert_user = "insert into receptionist_login values(?,?)";
                        PreparedStatement ps1 = con.prepareStatement(insert_user);
                        ps1.setString(1, reception_id1);
                        ps1.setString(2, password1);
                        int rs1 = ps1.executeUpdate();
                        System.out.println("USER REGISTERED!");
                        System.out.println("PLEASE LOGIN!");
                        break;
                    case 3:
                    System.out.println("RETURNING TO MAIN SCREEN...");
                    Thread.sleep(2000);
                        select = false;
                        break;
                    default:
                        System.out.println("enter valid choice !");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter valid.");
                sc.next();
            }
        } while (select);
    }

    private static void AdminLogin() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("ENTER ID : ");
        String id = sc.next();
        if (id.equalsIgnoreCase("rochan15")) {
            System.out.println("ENTER PASSWORD : ");
            int password = sc.nextInt();
            if (password == 2005) {
                System.out.println("**********WELCOME ADMIN**********");
                Admin adminlog = new Admin();
                adminlog.admin_login();
            } else {
                System.out.println("INVALID PASSWORD!");
            }
        } else {
            System.out.println("INVALID ID!");
        }
    }
}

//inner join 
// SELECT 
//     customer_name,room_no,checkOut,payment_status 
// FROM 
//     Customer_details
// INNER JOIN 
//     room_details
// ON 
//     Customer_details.Allocatedroom_no = room_details.room_no;

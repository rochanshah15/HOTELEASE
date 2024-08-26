import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
    static ArrayList<AddRoom> roomlist = new ArrayList<>();

    public static void admin_login() throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean admin_choice = true;
        do {
            System.out.println("SELECT : ");
            System.out.println("1. ADD NEW EMPLOYEE");
            System.out.println("2. ADD NEW ROOM");
            System.out.println("3. REMOVE EMPLOYEE");
            System.out.println("4.RETURN TO MAIN MENU");
            System.out.println("ENTER CHOICE : ");
            int admin_choice1 = sc.nextInt();
            switch (admin_choice1) {
                case 1:
                    AddNewEmployee();
                    break;

                case 2:
                    AddNewRoom();
                    break;

                case 3:
                System.out.println("Select from list :");
                DisplayAll emp = new DisplayAll();
                emp.DisplayEmployees();
                    String remove_emp;
                    while (true) {
                        System.out.println("ENTER EMPLOYEE ID OF EMPLOYEE YOU WANT TO REMOVE: ");
                        remove_emp = sc.next();
                        if (remove_emp.matches("\\d+")) {
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter numbers only.");
                        }
                    }
                    RemoveEmployee remove = new RemoveEmployee();
                    remove.remove_emp(remove_emp);
                    break;
                case 4:
                    System.out.println("RETURNING TO MAIN SCREEN...");
                    Thread.sleep(2000);
                    admin_choice = false;
                    break;
                default:
                    System.out.println("PLEASE ENTER VALID!");
                    break;
            }
        } while (admin_choice);
    }

    private static void AddNewRoom() {
        Scanner sc = new Scanner(System.in);

        String room_type = "";
        while (true) {
            System.out.println("ENTER ROOM TYPE (SINGLE/DOUBLE BED): ");
            room_type = sc.next().trim().toUpperCase();
            if (room_type.equals("SINGLE") || room_type.equals("DOUBLE")) {
                break;
            } else {
                System.out.println("Invalid input: Please enter 'SINGLE' or 'DOUBLE BED'.");
            }
        }

        int room_price = 0;
        while (true) {
            System.out.print("ENTER ROOM PRICE: ");
            try {
                room_price = sc.nextInt();
                if (room_price > 0) {
                    break;
                } else {
                    System.out.println("Invalid input: Room price must be a positive number. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter a valid integer.");
                sc.next();
            }
        }
        String room_status = "AVAILABLE";
        System.out.println("cleaning status : cleaned");
        // String room_cleaning_status;
        // while (true) {
        //     System.out.println("ROOM CLEANING STATUS (cleaned/dirty): ");
        //     room_cleaning_status = sc.next();
        //     if (room_cleaning_status.equalsIgnoreCase("cleaned") || room_cleaning_status.equalsIgnoreCase("dirty")) {
        //         break;
        //     } else {
        //         System.out.println("Invalid input: Please enter 'cleaned' or 'dirty'.");
        //     }
        // }

        AddRoom room = new AddRoom(room_type, room_price, room_status,"cleaned");
        roomlist.add(room);
        room.insert_room();
    }

    private static void AddNewEmployee() throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean addhar_check = true;
        String emp_id = "";
        String phoneNumber = "";
        String emp_email = "";
        String gender = "";

        while (addhar_check) {
            System.out.println("ENTER EMPLOYEE ID (AADHAAR NO.): ");
            emp_id = sc.next();

            if (emp_id.length() == 12) {
                boolean isValid = true;

                for (int i = 0; i < emp_id.length(); i++) {
                    if (emp_id.charAt(i) < '0' || emp_id.charAt(i) > '9') {
                        isValid = false;
                        break;
                    }
                }

                if (isValid) {
                    try {
                        if (isDuplicateEmpId(emp_id)) {
                            System.out.println("DUPLICATE AADHAAR NO. This ID already exists.");
                        } else {
                            addhar_check = false;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("INCORRECT AADHAAR!! Please enter numeric digits only.");
                }
            } else {
                System.out.println("INCORRECT AADHAAR NO. It should be 12 digits long.");
            }
        }
        String emp_name = "";
        while (true) {
            System.out.print("ENTER EMPLOYEE NAME: ");
            emp_name = sc.next();
            if (emp_name.isEmpty()) {
                System.out.println("Invalid input: Name cannot be empty. Please try again.");
            } else if (!emp_name.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid input: Name must contain only letters. Please try again.");
            } else {
                break;
            }
        }

        double emp_salary = 0;
        while (true) {
            try {
                System.out.print("ENTER EMPLOYEE SALARY: ");
                emp_salary = sc.nextDouble();

                if (emp_salary < 0) {
                    System.out.println("Invalid input: Salary cannot be negative. Please enter a positive number.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter a valid number.");
                sc.next();
            }
        }

        String emp_designation = "";

        while (true) {
            System.out.print("ENTER EMPLOYEE DESIGNATION (Chef/Cleaner/roomService): ");
            emp_designation = sc.next().trim();

            if (emp_designation.equalsIgnoreCase("Chef") ||
                    emp_designation.equalsIgnoreCase("Cleaner") ||
                    emp_designation.equalsIgnoreCase("roomService")) {
                break;
            } else {
                System.out.println("Invalid input: Please enter a valid designation (Chef, Cleaner, or roomService).");
            }
        }

        boolean ph_check = true;
        while (ph_check) {
            try {
                System.out.println("ENTER PHONE NUMBER : ");
                phoneNumber = sc.next();
                if (phoneNumber.length() != 10) {
                    ph_check = true;
                    throw new InvalidPhoneNumberException("INVALID PHONE NUMBER.");
                }
                for (int i = 0; i < phoneNumber.length(); i++) {
                    if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') {
                        ph_check = true;
                        throw new InvalidPhoneNumberException("Phone number contains invalid characters.");
                    }
                }
                ph_check = false;
                break;
            } catch (InvalidPhoneNumberException e) {
                System.out.println(e.getMessage());
            }
        }

        boolean b = true;
        while (b) {
            System.out.println("ENTER EMPLOYEE EMAIL : ");
            emp_email = sc.next();
            if (emp_email.endsWith("@gmail.com")) {
                b = false;
                break;
            } else {
                System.out.println("ENTER VALID MAIL!");
                b = true;
            }
        }
        int emp_age = 0;
        // while (true) {
        // try {
        // System.out.println("ENTER EMPLOYEE AGE: ");
        // emp_age = sc.nextInt();
        // if (emp_age < 18 || emp_age > 65) {
        // System.out.println("Invalid age! Please enter a valid age between 18 and
        // 65.");
        // } else {
        // break;
        // }
        // } catch (InputMismatchException e) {
        // System.out.println("Invalid input! Please enter a valid integer for age.");
        // sc.next();
        // }
        // }
        System.out.println("ENTER EMPLOYEE AGE: ");
        emp_age = sc.nextInt();

        boolean gender_check = false;
        while (!gender_check) {
            System.out.println("ENTER GENDER (Male, Female, Other): ");
            gender = sc.next();

            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")
                    || gender.equalsIgnoreCase("Other")) {
                gender_check = true;
            } else {
                System.out.println("Invalid gender input. Please try again.");
            }
        }
        try {
            EMPLOYEE employee = new EMPLOYEE(emp_id, emp_name, emp_salary, emp_designation, phoneNumber, emp_email,
                    emp_age, gender);
            List l1 = new List();
            l1.add(employee);
            employee.insertEmployee();
        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean isDuplicateEmpId(String emp_id) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isDuplicate = false;

        String dburl = "jdbc:mysql://localhost:3306/hotel_db";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(drivername);
            con = DriverManager.getConnection(dburl, dbuser, dbpass);

            if (con != null) {
                String query = "SELECT 1 FROM employee_details WHERE emp_id = ?";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, emp_id);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    isDuplicate = true;
                }
            } else {
                System.out.println("Failed to connect to database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Database connection or query execution failed");
        }
        return isDuplicate;
    }
}

class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}

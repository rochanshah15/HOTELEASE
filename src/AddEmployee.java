import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class EMPLOYEE {
    String emp_id;
    String emp_name;
    double emp_salary;
    String emp_designation;
    String emp_phone;
    String emp_email;
    int emp_age;
    String emp_gender;
    Connection con; 
    public EMPLOYEE(String emp_id, String emp_name, double emp_salary, String emp_designation,
                    String emp_phone, String emp_email, int emp_age, String emp_gender) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.emp_salary = emp_salary;
        this.emp_designation = emp_designation;
        this.emp_phone = emp_phone;
        this.emp_email = emp_email;
        this.emp_age = emp_age;
        this.emp_gender = emp_gender;

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

    public void insertEmployee() throws SQLException {
        insertEmployee(this);
    }

    public void insertEmployee(EMPLOYEE employee) throws SQLException {
        String insert_emp = "INSERT INTO employee_details VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(insert_emp);
        ps.setString(1, employee.getEmp_id());
        ps.setString(2, employee.getEmp_name());
        ps.setDouble(3, employee.getEmp_salary());
        ps.setString(4, employee.getEmp_designation());
        ps.setString(5, employee.getEmp_phone());
        ps.setString(6, employee.getEmp_email());
        ps.setInt(7, employee.getEmp_age());
        ps.setString(8, employee.getEmp_gender());

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Employee added successfully");
        } else {
            System.out.println("Failed to insert employee");
        }

        // Close the prepared statement
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public double getEmp_salary() {
        return emp_salary;
    }

    public String getEmp_designation() {
        return emp_designation;
    }

    public String getEmp_phone() {
        return emp_phone;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public int getEmp_age() {
        return emp_age;
    }

    public String getEmp_gender() {
        return emp_gender;
    }

    public Connection getCon() {
        return con;
    }

    @Override
    public String toString() {
        return "EMPLOYEE [emp_id=" + emp_id + ", emp_name=" + emp_name + ", emp_salary=" + emp_salary
                + ", emp_designation=" + emp_designation + ", emp_phone=" + emp_phone + ", emp_email=" + emp_email
                + ", emp_age=" + emp_age + ", emp_gender=" + emp_gender + "]";
    }
}

class List {
    class Node {
        EMPLOYEE employee;
        Node next;

        Node(EMPLOYEE employee) {
            this.employee = employee;
            this.next = null;
        }
    }

    Node head = null;

    public void add(EMPLOYEE employee) {
        Node newNode = new Node(employee);
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

    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.println(current.employee);
            current = current.next;
        }
    }

    public void insertAllEmployeesToDB() {
        Node current = head;
        while (current != null) {
            try {
                current.employee.insertEmployee(current.employee);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            current = current.next;
        }
    }
}

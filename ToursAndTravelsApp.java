import java.sql.*;
import java.util.Scanner;


class DBConnection {
    protected Connection con;
    protected Statement st;

    DBConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/tours_travel";
        String user = "root";
        String password = "Amri2a@2";
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, user, password);
        st = con.createStatement();
    }
}


interface TravelOperations {
    void addCustomer();
    void createBooking();
    void displayRecords();
    void deleteRecord();
}


class TravelService extends DBConnection implements TravelOperations {

    Scanner sc = new Scanner(System.in);

    TravelService() throws Exception {
        super();
    }


    public void addCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Age: ");
            int age = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            st.executeUpdate("INSERT INTO customers(customer_id, name, age, phone) VALUES (" + customerId + ",'" + name + "'," + age + ",'" + phone + "')");
            System.out.println("Customer Added");

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createBooking() {
        try {
            System.out.print("Enter Booking ID: ");
            int bookingId = sc.nextInt();

            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            ResultSet rs = st.executeQuery(
                "SELECT package_id, destination FROM packages"
            );

            System.out.println("Available Packages:");
            while (rs.next()) {
                System.out.println(rs.getInt("package_id") + ". " + rs.getString("destination")
                );
            }

            System.out.print("Choose Package ID: ");
            int packageId = sc.nextInt();
            sc.nextLine();

            ResultSet r2 = st.executeQuery("SELECT price, duration FROM packages WHERE package_id=" + packageId);
            r2.next();

            int price = r2.getInt("price");
            int duration = r2.getInt("duration");

            System.out.println("Price: " + price);
            System.out.println("Duration: " + duration + " days");
            System.out.print("Enter Travel Date (YYYY-MM-DD): ");
            String travelDate = sc.nextLine();

            st.executeUpdate( "INSERT INTO bookings(booking_id, customer_id, package_id, travel_date, status) VALUES (" +
            bookingId + "," + customerId + "," + packageId + ",'" + travelDate + "','CONFIRMED')");

            System.out.print("Enter Payment ID: ");
            int paymentId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Payment Mode (Cash/Card/UPI): ");
            String mode = sc.nextLine();

            st.executeUpdate("INSERT INTO payments(payment_id, booking_id, amount, payment_mode) VALUES (" + paymentId + "," + bookingId + "," + price + ",'" + mode + "')");

            System.out.println("Booking Completed");

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

 public void displayRecords() {
    try {
        ResultSet rs = st.executeQuery(
            "SELECT c.customer_id, c.name, b.travel_date, p.destination, p.duration, pay.amount, pay.payment_mode " +
            "FROM customers c " + "INNER JOIN bookings b ON c.customer_id=b.customer_id " +"INNER JOIN packages p ON b.package_id=p.package_id " +
            "INNER JOIN payments pay ON b.booking_id=pay.booking_id");

        System.out.println("Customer_ID | Name | Travel_Date | Destination | Days | Amount | Mode");

        while (rs.next()) {
            System.out.println(
                rs.getInt(1) + " | " + rs.getString(2) + " | " +
                rs.getDate(3) + " | " + rs.getString(4) + " | " +
                rs.getInt(5) + " | " + rs.getInt(6) + " | " +
                rs.getString(7)
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}



    public void deleteRecord() {
        try {
            System.out.print("Enter Customer ID to delete: ");
            int id = sc.nextInt();

            st.executeUpdate("DELETE FROM payments WHERE booking_id IN " + "(SELECT booking_id FROM bookings WHERE customer_id=" + id + ")");
            st.executeUpdate("DELETE FROM bookings WHERE customer_id=" + id);
            st.executeUpdate("DELETE FROM customers WHERE customer_id=" + id);

            System.out.println("Record Deleted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class ToursAndTravelsApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            TravelOperations service = new TravelService();
            while (true) {
                System.out.println("\n    TOURS & TRAVEL MENU    ");
                System.out.println("1. Add Customer");
                System.out.println("2. Create Booking");
                System.out.println("3. Display Records");
                System.out.println("4. Delete Record");
                System.out.println("5. Exit");
                System.out.print("Choice: ");

                int ch = sc.nextInt();

                switch (ch) {
                            case 1:
                                service.addCustomer();
                                break;

                             case 2:
                                service.createBooking();
                                break;

                            case 3:
                                service.displayRecords();
                                break;

                            case 4:
                                service.deleteRecord();
                                break;

                            case 5:
                                System.exit(0);

                            default:
                                System.out.println("Invalid Choice");
                            }

            }

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            sc.close();
        }
    }
}
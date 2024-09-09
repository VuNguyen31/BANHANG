import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        // URL kết nối đến SQL Server
        String url = "jdbc:sqlserver://BIN:1433;databaseName=Product;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "123";

        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Ket noi thanh cong");

            // Thực hiện các thao tác với cơ sở dữ liệu

            
            conn.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }
}
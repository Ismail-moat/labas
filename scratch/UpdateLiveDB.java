import java.sql.*;

public class UpdateLiveDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/labas_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String pass = "Shadow=0hacker";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement stmt = con.createStatement()) {
            
            System.out.println("Adding avatar_url column to clients table...");
            try {
                stmt.execute("ALTER TABLE clients ADD COLUMN avatar_url VARCHAR(255) DEFAULT NULL AFTER zip_code");
                System.out.println("Success!");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1060) {
                    System.out.println("Column already exists.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

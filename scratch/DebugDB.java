import java.sql.*;

public class DebugDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/labas_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String pass = "Shadow=0hacker"; 

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connection successful!");
            
            String sql = "SELECT count(*) FROM users";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Total users: " + rs.getInt(1));
                }
            }

            sql = "SELECT * FROM users";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("User: " + rs.getString("email") + " (ID: " + rs.getInt("id") + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.labas;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdatePasswordTest {
    
    @Test
    public void testUpdatePassword() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/labas_db";
        String dbUser = "root";
        String dbPassword = "Shadow=0hacker";
        String hash = "$2a$10$7EqJtq98hPqEX7fNZaFWoO9P1p2.R2hNnUv1w8i4lQ8M4mF9l8O3q";
        
        System.out.println("Connecting to database...");
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "UPDATE users SET password = ? WHERE email = 'admin@shop.com'";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, hash);
                int rows = pstmt.executeUpdate();
                System.out.println("Rows updated: " + rows);
                if (rows > 0) {
                    System.out.println("SUCCESS: Admin password updated to BCrypt hash of '123456'.");
                } else {
                    System.out.println("WARNING: No rows updated. Is admin@shop.com in the database?");
                }
            }
        }
    }
}

package com.labas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateAdminPassword {
    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/labas_db";
        String dbUser = "root";
        String dbPassword = "Shadow=0hacker";

        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String hash = "$2a$10$7EqJtq98hPqEX7fNZaFWoO9P1p2.R2hNnUv1w8i4lQ8M4mF9l8O3q";
            String query = "UPDATE users SET password = ? WHERE email = 'admin@shop.com'";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, hash);
            int rows = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rows);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

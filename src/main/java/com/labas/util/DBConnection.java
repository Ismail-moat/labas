package com.labas.util;

import jakarta.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    protected static String dbUrl;
    protected static String dbUser;
    protected static String dbPassword;

    public static void init(ServletContext context) {
        try {
            Class.forName(context.getInitParameter("dbDriver"));

            dbUrl = context.getInitParameter("dbUrl");
            dbUser = context.getInitParameter("dbUser");
            dbPassword = context.getInitParameter("dbPassword");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {

        if (dbUrl == null) {
            throw new IllegalStateException("DBConnection not initialized. Call init() first.");
        }

        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}

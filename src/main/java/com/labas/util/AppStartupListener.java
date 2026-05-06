package com.labas.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.labas.dao.UserDAO;

@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBConnection.init(sce.getServletContext());
            System.out.println("✅ DBConnection initialized successfully.");

            migrateAdminPassword();

        } catch (Exception e) {
            System.err.println("❌ DBConnection failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void migrateAdminPassword() {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.migratePasswordIfPlaintext("admin@shop.com", "admin123");
            System.out.println("✅ Admin password BCrypt check complete.");
        } catch (Exception e) {
            System.err.println("⚠️  Could not migrate admin password: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}

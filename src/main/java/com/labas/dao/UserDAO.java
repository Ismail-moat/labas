package com.labas.dao;

import com.labas.model.User;
import com.labas.util.DBConnection;
import com.labas.util.PasswordUtil;

import java.sql.*;

public class UserDAO {

    public boolean emailExiste(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("UserDAO.emailExiste: " + e.getMessage());
        }
        return false;
    }

    public int saveUser(User user) {
        String sql = "INSERT INTO users(email, password, role) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("UserDAO.saveUser: " + e.getMessage());
        }
        return -1;
    }

    public int countClients() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'client'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("UserDAO.countClients: " + e.getMessage());
        }
        return 0;
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.findByEmail: " + e.getMessage());
        }
        return null;
    }

    public boolean update(User user) {
        String sql = "UPDATE users SET email=?, password=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getIdUser());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO.update: " + e.getMessage());
        }
        return false;
    }

    public void migratePasswordIfPlaintext(String email, String expectedPlaintext) {
        User user = findByEmail(email);
        if (user == null) return;
        if (!PasswordUtil.isBCryptHash(user.getPassword())) {
            String hashed = PasswordUtil.hashPassword(expectedPlaintext);
            user.setPassword(hashed);
            update(user);
            System.out.println("✅ Migrated password for: " + email);
        }
    }
}

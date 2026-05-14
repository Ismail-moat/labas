package com.labas.dao;

import com.labas.model.Client;
import com.labas.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public boolean usernameExiste(String username) {
        String sql = "SELECT id FROM clients WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("ClientDAO.usernameExiste: " + e.getMessage());
        }
        return false;
    }

    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY last_name, first_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clients.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("ClientDAO.findAll: " + e.getMessage());
        }
        return clients;
    }

    public int saveClient(Client client) {
        String sql = "INSERT INTO clients(user_id, first_name, last_name, username, phone, address, city, zip_code, avatar_url) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, client.getIdUser());
            ps.setString(2, client.getFirstName());
            ps.setString(3, client.getLastName());
            ps.setString(4, client.getUsername());
            ps.setString(5, client.getPhone());
            ps.setString(6, client.getAddress());
            ps.setString(7, client.getCity());
            ps.setString(8, client.getZipCode());
            ps.setString(9, client.getAvatarUrl());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("ClientDAO.saveClient ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public Client findByUserId(int userId) {
        String sql = "SELECT * FROM clients WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("ClientDAO.findByUserId: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Client client) {
        String sql = "UPDATE clients SET first_name=?, last_name=?, username=?, phone=?, address=?, city=?, zip_code=? "
                   + ", avatar_url=COALESCE(?, avatar_url) "
                   + "WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getUsername());
            ps.setString(4, client.getPhone());
            ps.setString(5, client.getAddress());
            ps.setString(6, client.getCity());
            ps.setString(7, client.getZipCode());
            ps.setString(8, client.getAvatarUrl()); 
            ps.setInt(9, client.getIdUser());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClientDAO.update: " + e.getMessage());
        }
        return false;
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getInt("id"));           
        c.setIdUser(rs.getInt("user_id"));  
        c.setFirstName(rs.getString("first_name"));
        c.setLastName(rs.getString("last_name"));
        c.setUsername(rs.getString("username"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setCity(rs.getString("city"));
        c.setZipCode(rs.getString("zip_code"));
        try { c.setAvatarUrl(rs.getString("avatar_url")); } catch (SQLException ignored) {}
        return c;
    }
}

package com.labas.dao;

import com.labas.model.Client;
import com.labas.util.DBConnection;

import java.sql.*;


public class ClientDAO {

    public int saveClient(Client client) {

        String sql = "INSERT INTO clients(user_id, first_name, last_name, username, phone, address, city, zip_code) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, client.getIdUser());
            ps.setString(2, client.getFirstName());
            ps.setString(3, client.getLastName());
            ps.setString(4, client.getUsername());   // CORRECTION : ajout de username
            ps.setString(5, client.getPhone());
            ps.setString(6, client.getAddress());
            ps.setString(7, client.getCity());
            ps.setString(8, client.getZipCode());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur saveClient : " + e.getMessage());
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

                Client c = new Client();
                c.setIdUser(rs.getInt("user_id"));
                c.setFirstName(rs.getString("first_name"));
                c.setLastName(rs.getString("last_name"));
                c.setUsername(rs.getString("username"));
                c.setPhone(rs.getString("phone"));
                c.setAddress(rs.getString("address"));
                c.setCity(rs.getString("city"));
                c.setZipCode(rs.getString("zip_code"));
                return c;
            }

        } catch (SQLException e) {
            System.err.println("Erreur findByUserId : " + e.getMessage());
        }

        return null;
    }


    public boolean update(Client client) {

        String sql = "UPDATE clients SET first_name=?, last_name=?, username=?, phone=?, address=?, city=?, zip_code=? "
                   + "WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getUsername());   // CORRECTION : mise à jour du username
            ps.setString(4, client.getPhone());
            ps.setString(5, client.getAddress());
            ps.setString(6, client.getCity());
            ps.setString(7, client.getZipCode());
            ps.setInt(8, client.getIdUser());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update client : " + e.getMessage());
        }

        return false;
    }
}
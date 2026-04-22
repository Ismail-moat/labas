package com.labas.dao;

import com.labas.model.OrderDTO;
import com.labas.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int count() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur OrderDAO.count : " + e.getMessage());
        }
        return 0;
    }

    public double getTotalSales() {
        String sql = "SELECT SUM(total_incl) FROM orders WHERE status != 'cancelled'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Erreur OrderDAO.getTotalSales : " + e.getMessage());
        }
        return 0.0;
    }

    public List<OrderDTO> findRecent(int limit) {
        return fetchOrders("SELECT o.id, c.first_name, c.last_name, o.created_at, o.total_incl, o.status FROM orders o LEFT JOIN clients c ON o.client_id = c.id ORDER BY o.created_at DESC LIMIT ?", limit);
    }

    public List<OrderDTO> findAllWithCustomerName() {
        return fetchOrders("SELECT o.id, c.first_name, c.last_name, o.created_at, o.total_incl, o.status FROM orders o LEFT JOIN clients c ON o.client_id = c.id ORDER BY o.created_at DESC", null);
    }

    private List<OrderDTO> fetchOrders(String sql, Integer limit) {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            if (limit != null) {
                ps.setInt(1, limit);
            }
             
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDTO dto = new OrderDTO();
                    dto.setId(rs.getInt("id"));

                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String customerName = "Inconnu";
                    if (firstName != null && lastName != null) {
                        customerName = firstName + " " + lastName;
                    }
                    dto.setCustomerName(customerName);
                    
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        dto.setCreatedAt(ts);
                    }
                    
                    dto.setTotalIncl(rs.getBigDecimal("total_incl"));
                    dto.setStatus(rs.getString("status"));
                    
                    orders.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur OrderDAO.fetchOrders : " + e.getMessage());
        }
        return orders;
    }
}

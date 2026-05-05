package com.labas.dao;

import com.labas.dto.OrderDTO;
import com.labas.model.Order;
import com.labas.model.OrderItem;
import com.labas.model.OrderStatus;
import com.labas.model.Product;
import com.labas.util.DBConnection;

import java.math.BigDecimal;
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
        return fetchOrders("SELECT o.id, c.first_name, c.last_name, u.email, o.created_at, o.total_excl, o.total_incl, o.status " +
                           "FROM orders o " +
                           "LEFT JOIN clients c ON o.client_id = c.id " +
                           "LEFT JOIN users u ON c.user_id = u.id " +
                           "ORDER BY o.created_at DESC LIMIT ?", limit);
    }

    public List<OrderDTO> findAllWithCustomerName() {
        return fetchOrders("SELECT o.id, c.first_name, c.last_name, u.email, o.created_at, o.total_excl, o.total_incl, o.status " +
                           "FROM orders o " +
                           "LEFT JOIN clients c ON o.client_id = c.id " +
                           "LEFT JOIN users u ON c.user_id = u.id " +
                           "ORDER BY o.created_at DESC", null);
    }

    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur OrderDAO.updateStatus : " + e.getMessage());
        }
        return false;
    }

  
    
    
    //salma

    /** Create a new order and return its generated ID.
     * clientId = the clients.id (not users.id)
     */
    
    
    public int createOrder(int clientId, BigDecimal totalExcl, BigDecimal totalIncl) {
        String sql = "INSERT INTO orders (client_id, total_excl, total_incl, status) VALUES (?, ?, ?, 'pending')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clientId);
            ps.setBigDecimal(2, totalExcl);
            ps.setBigDecimal(3, totalIncl);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("OrderDAO.createOrder: " + e.getMessage());
        }
        return -1;
    }

    //Add a single item line to an order.
     
    public boolean addOrderItem(int orderId, int productId, int quantity,
                                 BigDecimal amountExcl, BigDecimal amountIncl) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount_excl, amount_incl) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, amountExcl);
            ps.setBigDecimal(5, amountIncl);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("OrderDAO.addOrderItem: " + e.getMessage());
        }
        return false;
    }

  //reate a delivery record linked to an order
    
    public boolean createDelivery(int orderId, String address, String addressExtra,
                                   String zipCode, String city) {
        String sql = "INSERT INTO delivery (order_id, address, address_extra, zip_code, city, status) " +
                     "VALUES (?, ?, ?, ?, ?, 'preparing')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setString(2, address);
            ps.setString(3, addressExtra);
            ps.setString(4, zipCode);
            ps.setString(5, city);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("OrderDAO.createDelivery: " + e.getMessage());
        }
        return false;
    }
    
    
    

    /**Get all orders for a specific client (order history).
 clientId = clients.id
  */
    
    
    public List<Order> findByClientId(int clientId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE client_id = ? ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setTotalExcl(rs.getBigDecimal("total_excl"));
                o.setTotalIncl(rs.getBigDecimal("total_incl"));
                o.setStatus(OrderStatus.valueOf(rs.getString("status").toUpperCase()));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) o.setCreatedAt(ts.toLocalDateTime());
                o.setItems(findOrderItems(rs.getInt("id"), con));
                orders.add(o);
            }
        } catch (SQLException e) {
            System.err.println("OrderDAO.findByClientId: " + e.getMessage());
        }
        return orders;
    }

   //Get the clients.id (not users.id) for a given users.id.
     
    public int getClientIdByUserId(int userId) {
        String sql = "SELECT id FROM clients WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("OrderDAO.getClientIdByUserId: " + e.getMessage());
        }
        return -1;
    }

    
    
    
    //PRIVATE HELPERS

    private List<OrderItem> findOrderItems(int orderId, Connection con) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name, p.image_url FROM order_item oi " +
                     "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setAmountExcl(rs.getBigDecimal("amount_excl"));
                item.setAmountIncl(rs.getBigDecimal("amount_incl"));
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setImageUrl(rs.getString("image_url"));
                item.setProduct(p);
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("OrderDAO.findOrderItems: " + e.getMessage());
        }
        return items;
    }

    private List<OrderDTO> fetchOrders(String sql, Integer limit) {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (limit != null) ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDTO dto = new OrderDTO();
                    dto.setId(rs.getInt("id"));
                    String firstName = rs.getString("first_name");
                    String lastName  = rs.getString("last_name");
                    dto.setCustomerName(firstName != null ? firstName + " " + lastName : "Inconnu");
                    dto.setCustomerEmail(rs.getString("email"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) dto.setCreatedAt(ts);
                    dto.setTotalExcl(rs.getBigDecimal("total_excl"));
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
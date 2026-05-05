package com.labas.dao;

import com.labas.model.Cart;
import com.labas.model.CartItem;
import com.labas.model.Product;
import com.labas.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // get or create the cart of a client
    public Cart getOrCreateCart(int clientId) {
        String sql = "SELECT * FROM cart WHERE client_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setItems(getCartItems(cart.getId()));
                return cart;
            }
        } catch (SQLException e) {
            System.err.println("CartDAO.getOrCreateCart select: " + e.getMessage());
        }

        // new cart(create)
        String insert = "INSERT INTO cart (client_id) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clientId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt(1));
                cart.setItems(new ArrayList<>());
                return cart;
            }
        } catch (SQLException e) {
            System.err.println("CartDAO.getOrCreateCart insert: " + e.getMessage());
        }
        return null;
    }

    
    
    
    
    // get all items in a cart
    public List<CartItem> getCartItems(int cartId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT ci.*, p.name, p.price, p.image_url, p.size, p.stock_qty " +
                     "FROM cart_item ci JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.cart_id = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));

                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setImageUrl(rs.getString("image_url"));
                p.setSize(rs.getString("size"));
                p.setStockQty(rs.getInt("stock_qty"));
                item.setProduct(p);

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("CartDAO.getCartItems: " + e.getMessage());
        }
        return items;
    }

    // Add item to cart (or increase qty if alr exists)
    public boolean addItem(int cartId, int productId, int quantity) {
        // Check if item already in cart
        String check = "SELECT id, quantity FROM cart_item WHERE cart_id = ? AND product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(check)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	
            	
                // Update quantity
            	
                int newQty = rs.getInt("quantity") + quantity;
                String update = "UPDATE cart_item SET quantity = ? WHERE id = ?";
                try (PreparedStatement upd = con.prepareStatement(update)) {
                    upd.setInt(1, newQty);
                    upd.setInt(2, rs.getInt("id"));
                    return upd.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("CartDAO.addItem check: " + e.getMessage());
        }

        
        // Insert new item
        String sql = "INSERT INTO cart_item (cart_id, product_id, quantity, unit_price) " +
                     "VALUES (?, ?, ?, (SELECT price FROM products WHERE id = ?))";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CartDAO.addItem insert: " + e.getMessage());
        }
        return false;
    }

    
    
    
    
    
    
    
    
    //uppdatee quantity of an item
    public boolean updateItemQuantity(int cartId, int productId, int quantity) {
        if (quantity <= 0) {
            return removeItem(cartId, productId);
        }
        String sql = "UPDATE cart_item SET quantity = ? WHERE cart_id = ? AND product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CartDAO.updateItemQuantity: " + e.getMessage());
        }
        return false;
    }

    
    //remove a single item
    public boolean removeItem(int cartId, int productId) {
        String sql = "DELETE FROM cart_item WHERE cart_id = ? AND product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CartDAO.removeItem: " + e.getMessage());
        }
        return false;
    }

    //clear all items from cart      (after checkout)
    
    
    public boolean clearCart(int cartId) {
        String sql = "DELETE FROM cart_item WHERE cart_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("CartDAO.clearCart: " + e.getMessage());
        }
        return false;
    }

    
    // Get the client id from cart_id
    public int getClientIdByCartId(int cartId) {
        String sql = "SELECT client_id FROM cart WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) return rs.getInt("client_id");
            
        } catch (SQLException e) {
        	
            System.err.println("CartDAO.getClientIdByCartId: " + e.getMessage());
        }
        return -1;
    }
}
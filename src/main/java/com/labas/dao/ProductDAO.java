package com.labas.dao;

import com.labas.model.Product;
import com.labas.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setVatRate(rs.getBigDecimal("vat_rate"));
                p.setImageUrl(rs.getString("image_url"));
                p.setStockQty(rs.getInt("stock_qty"));
                p.setSize(rs.getString("size"));
                p.setCategoryId(rs.getObject("category_id") != null ? rs.getInt("category_id") : null);
                p.setSubcategoryId(rs.getObject("subcategory_id") != null ? rs.getInt("subcategory_id") : null);
                products.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur ProductDAO.findAll : " + e.getMessage());
        }
        return products;
    }

    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setVatRate(rs.getBigDecimal("vat_rate"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setStockQty(rs.getInt("stock_qty"));
                    p.setSize(rs.getString("size"));
                    p.setCategoryId(rs.getObject("category_id") != null ? rs.getInt("category_id") : null);
                    p.setSubcategoryId(rs.getObject("subcategory_id") != null ? rs.getInt("subcategory_id") : null);
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ProductDAO.findById : " + e.getMessage());
        }
        return null;
    }

    public boolean save(Product product) {
        String sql = "INSERT INTO products (name, description, price, vat_rate, image_url, stock_qty, size, category_id, subcategory_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setBigDecimal(4, product.getVatRate());
            ps.setString(5, product.getImageUrl());
            ps.setInt(6, product.getStockQty());
            ps.setString(7, product.getSize());
            if (product.getCategoryId() != null) ps.setInt(8, product.getCategoryId()); else ps.setNull(8, Types.INTEGER);
            if (product.getSubcategoryId() != null) ps.setInt(9, product.getSubcategoryId()); else ps.setNull(9, Types.INTEGER);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ProductDAO.save : " + e.getMessage());
        }
        return false;
    }

    public boolean update(Product product) {
        String sql = "UPDATE products SET name=?, description=?, price=?, vat_rate=?, image_url=?, stock_qty=?, size=?, category_id=?, subcategory_id=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setBigDecimal(4, product.getVatRate());
            ps.setString(5, product.getImageUrl());
            ps.setInt(6, product.getStockQty());
            ps.setString(7, product.getSize());
            if (product.getCategoryId() != null) ps.setInt(8, product.getCategoryId()); else ps.setNull(8, Types.INTEGER);
            if (product.getSubcategoryId() != null) ps.setInt(9, product.getSubcategoryId()); else ps.setNull(9, Types.INTEGER);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ProductDAO.update : " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ProductDAO.delete : " + e.getMessage());
        }
        return false;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur ProductDAO.count : " + e.getMessage());
        }
        return 0;
    }
}

package com.labas.dao;

import com.labas.model.Category;
import com.labas.model.Subcategory;
import com.labas.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category ORDER BY name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                categories.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur CategoryDAO.findAll : " + e.getMessage());
        }
        return categories;
    }

    public List<Category> findAllWithSubcategories() {
        List<Category> categories = findAll();
        for (Category c : categories) {
            c.setSubcategories(findSubcategoriesByCategoryId(c.getId()));
        }
        return categories;
    }

    public List<Subcategory> findSubcategoriesByCategoryId(int categoryId) {
        List<Subcategory> subs = new ArrayList<>();
        String sql = "SELECT * FROM subcategory WHERE category_id = ? ORDER BY name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subcategory s = new Subcategory();
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
                    s.setCategoryId(rs.getInt("category_id"));
                    subs.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur CategoryDAO.findSubcategoriesByCategoryId : " + e.getMessage());
        }
        return subs;
    }

    public boolean save(Category category) {
        String sql = "INSERT INTO category (name) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur CategoryDAO.save : " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM category WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur CategoryDAO.delete : " + e.getMessage());
        }
        return false;
    }
}


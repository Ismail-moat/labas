package com.labas.service;

import com.labas.dao.CategoryDAO;
import com.labas.model.Category;
import com.labas.model.Subcategory;

import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public List<Category> getAllCategoriesWithSubcategories() {
        return categoryDAO.findAllWithSubcategories();
    }

    public boolean addCategory(String name) {
        Category c = new Category();
        c.setName(name);
        return categoryDAO.save(c);
    }

    public boolean deleteCategory(int id) {
        return categoryDAO.delete(id);
    }
}


package com.labas.service;

import com.labas.dao.ProductDAO;
import com.labas.model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    public boolean addProduct(Product product) {
        return productDAO.save(product);
    }

    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }

    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }

    public int getProductCount() {
        return productDAO.count();
    }

    public List<Product> getTopSellingProducts(int limit) {
        return productDAO.getTopSellingProducts(limit);
    }
}


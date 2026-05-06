package com.labas.servlet;

import com.labas.model.Category;
import com.labas.model.Product;
import com.labas.service.CategoryService;
import com.labas.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CatalogServlet", urlPatterns = {"/catalog"})
public class CatalogServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String searchQuery = request.getParameter("q");

        List<Product> products;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            products = productService.searchProducts(searchQuery);
            request.setAttribute("title", "Search Results for: " + searchQuery);
        } else if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                products = productService.getProductsByCategory(categoryId);

                request.setAttribute("title", "Category Products");
            } catch (NumberFormatException e) {
                products = productService.getAllProducts();
                request.setAttribute("title", "All Products");
            }
        } else {
            products = productService.getAllProducts();
            request.setAttribute("title", "Shop All");
        }

        List<Category> categories = categoryService.getAllCategories();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/pages/catalog.jsp").forward(request, response);
    }
}

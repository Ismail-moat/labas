package com.labas.servlet;

import com.labas.model.Product;
import com.labas.service.CategoryService;
import com.labas.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/products")
public class AdminProductsServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("products", productService.getAllProducts());
        request.setAttribute("categories", categoryService.getAllCategoriesWithSubcategories());

        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            productService.deleteProduct(id);
        } else if ("save".equals(action)) {
            String idStr = request.getParameter("id");
            Product p = new Product();
            if (idStr != null && !idStr.isEmpty()) {
                p.setId(Integer.parseInt(idStr));
            }
            p.setName(request.getParameter("name"));
            p.setDescription(request.getParameter("description"));
            p.setPrice(new BigDecimal(request.getParameter("price")));
            p.setVatRate(new BigDecimal(request.getParameter("vatRate")));
            p.setStockQty(Integer.parseInt(request.getParameter("stockQty")));
            p.setSize(request.getParameter("size"));
            p.setImageUrl(request.getParameter("imageUrl"));
            p.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));

            String subIdStr = request.getParameter("subcategoryId");
            if (subIdStr != null && !subIdStr.isEmpty()) {
                p.setSubcategoryId(Integer.parseInt(subIdStr));
            }

            if (p.getId() == null) {
                productService.addProduct(p);
            } else {
                productService.updateProduct(p);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}


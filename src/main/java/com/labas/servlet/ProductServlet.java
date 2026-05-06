package com.labas.servlet;

import com.labas.model.Product;
import com.labas.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ProductServlet", urlPatterns = {"/product"})
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/catalog");
            return;
        }
        
        try {
            int productId = Integer.parseInt(idStr);
            Product product = productService.getProductById(productId);
            
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/catalog");
                return;
            }
            
            request.setAttribute("product", product);
            request.getRequestDispatcher("/pages/product.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/catalog");
        }
    }
}

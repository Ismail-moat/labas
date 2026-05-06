package com.labas.servlet;

import com.labas.model.Product;
import com.labas.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Product> featuredProducts = productService.getTopSellingProducts(5);
        request.setAttribute("featuredProducts", featuredProducts);
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}

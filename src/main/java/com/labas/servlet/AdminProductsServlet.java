package com.labas.servlet;

import com.labas.model.Product;
import com.labas.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/products")
public class AdminProductsServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        
        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }
}

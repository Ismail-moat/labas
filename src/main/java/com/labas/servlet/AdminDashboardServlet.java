package com.labas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.labas.service.OrderService;
import com.labas.service.ProductService;
import com.labas.service.UserService;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs.");
            return;
        }

        request.setAttribute("adminName",     session.getAttribute("email"));
        request.setAttribute("totalSales",    orderService.getTotalSales());
        request.setAttribute("orderCount",    orderService.getOrderCount());
        request.setAttribute("productCount",  productService.getProductCount());
        request.setAttribute("customerCount", userService.getCustomerCount());
        request.setAttribute("recentOrders",  orderService.getRecentOrders(5));

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}

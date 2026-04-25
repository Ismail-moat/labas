package com.labas.servlet;

import com.labas.service.DashboardService;
import com.labas.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;


@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(false);

        Map<String, Object> stats = dashboardService.loadStatistics();

        request.setAttribute("adminName",     session.getAttribute("email"));
        request.setAttribute("totalSales",    stats.get("totalSales"));
        request.setAttribute("orderCount",    stats.get("totalOrders"));
        request.setAttribute("productCount",  stats.get("totalProducts"));
        request.setAttribute("customerCount", stats.get("totalClients"));
        request.setAttribute("recentOrders",  orderService.getRecentOrders(5));

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}

package com.labas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.labas.dto.OrderDTO;
import com.labas.service.DashboardService;
import com.labas.service.OrderService;

import java.io.IOException;
import java.util.Map;

/**
 * AdminDashboardServlet - Affiche le tableau de bord administrateur.
 * Délègue le chargement des statistiques à DashboardService.
 * La protection d'accès admin est gérée par AdminFilter.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // La vérification du rôle admin est gérée par AdminFilter

        HttpSession session = request.getSession(false);

        // Chargement des statistiques via DashboardService
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

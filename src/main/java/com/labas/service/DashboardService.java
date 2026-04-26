package com.labas.service;

import com.labas.dao.OrderDAO;
import com.labas.dao.ProductDAO;
import com.labas.dao.UserDAO;
import java.util.HashMap;
import java.util.Map;

public class DashboardService {

    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;

    public DashboardService() {
        this.orderDAO = new OrderDAO();
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
    }

    public Map<String, Object> loadStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalOrders", orderDAO.count());
        stats.put("totalProducts", productDAO.count());
        stats.put("totalClients", userDAO.countClients());
        stats.put("totalSales", orderDAO.getTotalSales());
        stats.put("topProducts", productDAO.getTopSellingProducts(5));

        return stats;
    }
}


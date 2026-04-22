package com.labas.service;

import com.labas.dao.OrderDAO;
import com.labas.model.OrderDTO;

import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    public int getOrderCount() {
        return orderDAO.count();
    }

    public double getTotalSales() {
        return orderDAO.getTotalSales();
    }

    public List<OrderDTO> getRecentOrders(int limit) {
        return orderDAO.findRecent(limit);
    }

    public List<OrderDTO> getAllOrders() {
        return orderDAO.findAllWithCustomerName();
    }
}

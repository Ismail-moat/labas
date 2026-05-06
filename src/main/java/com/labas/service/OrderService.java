package com.labas.service;

import com.labas.dao.CartDAO;
import com.labas.dao.OrderDAO;
import com.labas.dto.OrderDTO;
import com.labas.model.Cart;
import com.labas.model.CartItem;
import com.labas.model.Order;
import com.labas.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO  cartDAO  = new CartDAO();

    public int getOrderCount()              { return orderDAO.count(); }
    public double getTotalSales()           { return orderDAO.getTotalSales(); }
    public List<OrderDTO> getRecentOrders(int limit) { return orderDAO.findRecent(limit); }
    public List<OrderDTO> getAllOrders()    { return orderDAO.findAllWithCustomerName(); }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateStatus(orderId, status);
    }

    public int checkout(int userId, String address, String addressExtra, String zipCode, String city) {

        int clientId = orderDAO.getClientIdByUserId(userId);
        if (clientId == -1) return -1;

        Cart cart = cartDAO.getOrCreateCart(clientId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) return -1;

        BigDecimal vatRate   = new BigDecimal("0.20");
        BigDecimal totalExcl = BigDecimal.ZERO;
        BigDecimal totalIncl = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            BigDecimal lineIncl = item.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineExcl = lineIncl
                    .divide(BigDecimal.ONE.add(vatRate), 2, RoundingMode.HALF_UP);
            totalIncl = totalIncl.add(lineIncl);
            totalExcl = totalExcl.add(lineExcl);
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int orderId = orderDAO.createOrder(con, clientId, totalExcl, totalIncl);
            if (orderId == -1) { con.rollback(); return -1; }

            for (CartItem item : cart.getItems()) {
                BigDecimal lineIncl = item.getUnitPrice()
                        .multiply(new BigDecimal(item.getQuantity()))
                        .setScale(2, RoundingMode.HALF_UP);
                BigDecimal lineExcl = lineIncl
                        .divide(BigDecimal.ONE.add(vatRate), 2, RoundingMode.HALF_UP);
                orderDAO.addOrderItem(con, orderId, item.getProduct().getId(),
                        item.getQuantity(), lineExcl, lineIncl);
            }

            orderDAO.createDelivery(con, orderId, address, addressExtra, zipCode, city);

            cartDAO.clearCart(cart.getId(), con);

            con.commit();
            return orderId;

        } catch (Exception e) {
            System.err.println("OrderService.checkout transaction failed: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (Exception ex) {  }
            return -1;
        } finally {
            try { if (con != null) con.close(); } catch (Exception ex) {  }
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        int clientId = orderDAO.getClientIdByUserId(userId);
        if (clientId == -1) return List.of();
        return orderDAO.findByClientId(clientId);
    }
}
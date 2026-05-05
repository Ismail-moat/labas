package com.labas.service;

import com.labas.dao.CartDAO;
import com.labas.dao.OrderDAO;
import com.labas.dto.OrderDTO;
import com.labas.model.Cart;
import com.labas.model.CartItem;
import com.labas.model.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO  cartDAO  = new CartDAO();


    public int getOrderCount() { return orderDAO.count(); }

    public double getTotalSales() { return orderDAO.getTotalSales(); }

    public List<OrderDTO> getRecentOrders(int limit) { return orderDAO.findRecent(limit); }

    public List<OrderDTO> getAllOrders() { return orderDAO.findAllWithCustomerName(); }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateStatus(orderId, status);
    }

    // me CHECKOUTTTT 
    /**  Convert the client's cart into an order.
     
  Returns the new order ID, or -1 on failure.
     * userId = users.id (from session)
     */
    
    public int checkout(int userId, String address, String addressExtra, String zipCode, String city) {
    	
        // Get the clients.id from users.id
        int clientId = orderDAO.getClientIdByUserId(userId);
        if (clientId == -1) return -1;

        // Load the cart
        Cart cart = cartDAO.getOrCreateCart(clientId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) return -1;

        // Calculate totals (VAT is stored per-product but for simplicity we use 20%)
        
        BigDecimal totalExcl = BigDecimal.ZERO;
        BigDecimal totalIncl = BigDecimal.ZERO;
        BigDecimal vatRate   = new BigDecimal("0.20");

        
        for (CartItem item : cart.getItems()) {
            BigDecimal lineExcl = item.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()))
                    .divide(BigDecimal.ONE.add(vatRate), 2, RoundingMode.HALF_UP);
            BigDecimal lineIncl = item.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            totalExcl = totalExcl.add(lineExcl);
            totalIncl = totalIncl.add(lineIncl);
        }
        

        // Create the order
        int orderId = orderDAO.createOrder(clientId, totalExcl, totalIncl);
        if (orderId == -1) return -1;

        // Add order items
        for (CartItem item : cart.getItems()) {
            BigDecimal lineIncl = item.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineExcl = lineIncl
                    .divide(BigDecimal.ONE.add(vatRate), 2, RoundingMode.HALF_UP);
            orderDAO.addOrderItem(orderId, item.getProduct().getId(),
                    item.getQuantity(), lineExcl, lineIncl);
        }
        

        // Create delivery record
        orderDAO.createDelivery(orderId, address, addressExtra, zipCode, city);

        // Clear the cart
        cartDAO.clearCart(cart.getId());

        return orderId;
    }

    //ORDER HISTORY
    
    

   //Get all orders for the logged-in client  userId = users.id (from session)
    
    public List<Order> getOrdersByUserId(int userId) {
    	
        int clientId = orderDAO.getClientIdByUserId(userId);
        if (clientId == -1) return List.of();
        return orderDAO.findByClientId(clientId);
    }
}
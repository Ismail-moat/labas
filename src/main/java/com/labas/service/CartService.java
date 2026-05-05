package com.labas.service;

import com.labas.dao.CartDAO;
import com.labas.dao.OrderDAO;
import com.labas.model.Cart;
import com.labas.model.CartItem;

import java.math.BigDecimal;
import java.util.List;


public class CartService {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public Cart getOrCreateCart(int clientId) {
        return cartDAO.getOrCreateCart(clientId);
    }

    public boolean addToCart(int clientId, int productId, int quantity) {
        Cart cart = cartDAO.getOrCreateCart(clientId);
        if (cart == null) return false;
        return cartDAO.addItem(cart.getId(), productId, quantity);
    }

    public boolean removeFromCart(int clientId, int productId) {
        Cart cart = cartDAO.getOrCreateCart(clientId);
        if (cart == null) return false;
        return cartDAO.removeItem(cart.getId(), productId);
    }

    public boolean updateQuantity(int clientId, int productId, int quantity) {
        Cart cart = cartDAO.getOrCreateCart(clientId);
        if (cart == null) return false;
        return cartDAO.updateItemQuantity(cart.getId(), productId, quantity);
    }

    public boolean clearCart(int clientId) {
        Cart cart = cartDAO.getOrCreateCart(clientId);
        if (cart == null) return false;
        return cartDAO.clearCart(cart.getId());
    }

    public int getCartItemCount(int clientId) {
        Cart cart = cartDAO.getOrCreateCart(clientId);
        if (cart == null || cart.getItems() == null) return 0;
        return cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getCartTotal(Cart cart) {
        if (cart == null || cart.getItems() == null) return BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            total = total.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }
}
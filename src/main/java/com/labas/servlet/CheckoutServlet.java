package com.labas.servlet;

import com.labas.model.Cart;
import com.labas.service.CartService;
import com.labas.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService  cartService  = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
            
            
            
            
            
            
            
            
            
        }

        int clientId = getClientId(session);
        Cart cart = cartService.getOrCreateCart(clientId);

        
        
        
        
        // If cart is empty, redirect to cart page
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        request.setAttribute("cart", cart);
        request.setAttribute("cartTotal", cartService.getCartTotal(cart));
        request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        String address      = request.getParameter("address");
        String addressExtra = request.getParameter("addressExtra");
        String zipCode      = request.getParameter("zipCode");
        String city         = request.getParameter("city");

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        // Basic validation
        if (address == null || address.isBlank() || city == null || city.isBlank()) {
            request.setAttribute("error", "Please fill in all required shipping fields.");
            int clientId = getClientId(session);
            Cart cart = cartService.getOrCreateCart(clientId);
            request.setAttribute("cart", cart);
            request.setAttribute("cartTotal", cartService.getCartTotal(cart));
            request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
            return;
        }

        int orderId = orderService.checkout(userId, address, addressExtra, zipCode, city);

        if (orderId == -1) {
            request.setAttribute("error", "Something went wrong placing your order. Please try again.");
            int clientId = getClientId(session);
            Cart cart = cartService.getOrCreateCart(clientId);
            request.setAttribute("cart", cart);
            request.setAttribute("cartTotal", cartService.getCartTotal(cart));
            request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
            return;
        }

        // Clear cart count in session
        
        session.setAttribute("cartCount", 0);
        session.setAttribute("lastOrderId", orderId);

        response.sendRedirect(request.getContextPath() + "/confirmation");
    }

    private int getClientId(HttpSession session) {
        Object clientId = session.getAttribute("clientId");
        if (clientId instanceof Integer) return (Integer) clientId;
        return -1;
    }
}
package com.labas.servlet;

import com.labas.model.Cart;
import com.labas.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        int clientId = getClientId(session);

        if (clientId == -1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Cart cart = cartService.getOrCreateCart(clientId);
        request.setAttribute("cart", cart);
        request.setAttribute("cartTotal", cartService.getCartTotal(cart));
        request.getRequestDispatcher("/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int clientId = getClientId(session);
        if (clientId == -1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action    = request.getParameter("action");
        String productIdStr = request.getParameter("productId");
        if (productIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        int productId = Integer.parseInt(productIdStr);

        switch (action == null ? "" : action) {
            case "add":
                int qty = 1;
                try { qty = Integer.parseInt(request.getParameter("quantity")); } catch (Exception ignored) {}
                cartService.addToCart(clientId, productId, qty);
                // Update session cart count
                session.setAttribute("cartCount", cartService.getCartItemCount(clientId));
                // Redirect back to wherever they came from
                String referer = request.getHeader("Referer");
                response.sendRedirect(referer != null ? referer : request.getContextPath() + "/cart");
                return;

            case "remove":
                cartService.removeFromCart(clientId, productId);
                break;

            case "update":
                int newQty = Integer.parseInt(request.getParameter("quantity"));
                cartService.updateQuantity(clientId, productId, newQty);
                break;
        }

        session.setAttribute("cartCount", cartService.getCartItemCount(clientId));
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    // Helper: get clients.id from session (stored as clientId after login)
    
    
    
    
    private int getClientId(HttpSession session) {
        Object clientId = session.getAttribute("clientId");
        if (clientId instanceof Integer) return (Integer) clientId;
        
        
        
        return -1;
    }
}
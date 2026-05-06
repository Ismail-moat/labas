package com.labas.servlet;

import com.labas.model.Cart;
import com.labas.model.Order;
import com.labas.service.CartService;
import com.labas.service.OrderService;
import com.labas.util.AuditLogger;
import com.labas.util.CsrfUtil;
import com.labas.util.EmailService;
import com.labas.util.ServletUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService  cartService  = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        int clientId = ServletUtil.getClientId(session);
        if (clientId == -1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Cart cart = cartService.getOrCreateCart(clientId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        CsrfUtil.getToken(session);
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

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Requête invalide. Veuillez réessayer.");
            Cart cart = cartService.getOrCreateCart(ServletUtil.getClientId(session));
            request.setAttribute("cart", cart);
            request.setAttribute("cartTotal", cartService.getCartTotal(cart));
            request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
            return;
        }

        int userId   = (int) session.getAttribute("userId");
        int clientId = ServletUtil.getClientId(session);

        String address      = request.getParameter("address");
        String addressExtra = request.getParameter("addressExtra");
        String zipCode      = request.getParameter("zipCode");
        String city         = request.getParameter("city");

        if (address == null || address.isBlank() || city == null || city.isBlank()) {
            request.setAttribute("error", "Veuillez remplir tous les champs obligatoires.");
            Cart cart = cartService.getOrCreateCart(clientId);
            request.setAttribute("cart", cart);
            request.setAttribute("cartTotal", cartService.getCartTotal(cart));
            request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
            return;
        }

        int orderId = orderService.checkout(userId, address, addressExtra, zipCode, city);

        if (orderId == -1) {
            request.setAttribute("error", "Une erreur est survenue lors de la commande. Veuillez réessayer.");
            Cart cart = cartService.getOrCreateCart(clientId);
            request.setAttribute("cart", cart);
            request.setAttribute("cartTotal", cartService.getCartTotal(cart));
            request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
            return;
        }

        AuditLogger.logOrder(orderId, userId, 0.0); 

        try {
            String clientEmail = (String) session.getAttribute("email");
            String firstName   = (String) session.getAttribute("firstName");
            String lastName    = (String) session.getAttribute("lastName");
            String clientName  = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");

            List<Order> orders = orderService.getOrdersByUserId(userId);
            Order placed = orders.stream().filter(o -> o.getId() == orderId).findFirst().orElse(null);
            if (placed != null && clientEmail != null) {
                EmailService.getInstance().sendOrderConfirmation(
                    clientEmail, clientName.trim(), orderId, placed.getItems(), placed.getTotalIncl()
                );
            }
        } catch (Exception e) {

            System.err.println("Email send error (non-fatal): " + e.getMessage());
        }

        session.setAttribute("cartCount", 0);
        session.setAttribute("lastOrderId", orderId);
        CsrfUtil.rotate(session);
        response.sendRedirect(request.getContextPath() + "/confirmation");
    }
}
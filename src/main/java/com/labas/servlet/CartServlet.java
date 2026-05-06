package com.labas.servlet;

import com.labas.model.Cart;
import com.labas.service.CartService;
import com.labas.util.ServletUtil;
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
        int clientId = ServletUtil.getClientId(session);
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
        int clientId = ServletUtil.getClientId(session);
        if (clientId == -1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        int productId = ServletUtil.parseIntParam(request.getParameter("productId"), -1);
        if (productId == -1) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        switch (action == null ? "" : action) {
            case "add": {
                int qty = ServletUtil.parseIntParam(request.getParameter("quantity"), 1);
                if (qty < 1) qty = 1;
                boolean added = cartService.addToCart(clientId, productId, qty);
                if (!added) {

                    String referer = request.getHeader("Referer");
                    String target = (referer != null ? referer : request.getContextPath() + "/catalog")
                                    + (referer != null && referer.contains("?") ? "&" : "?") + "stockError=1";
                    response.sendRedirect(target);
                    return;
                }
                session.setAttribute("cartCount", cartService.getCartItemCount(clientId));
                String referer = request.getHeader("Referer");
                response.sendRedirect(referer != null ? referer : request.getContextPath() + "/cart");
                return;
            }
            case "remove":
                cartService.removeFromCart(clientId, productId);
                break;

            case "update": {
                int newQty = ServletUtil.parseIntParam(request.getParameter("quantity"), 0);
                cartService.updateQuantity(clientId, productId, newQty);
                break;
            }
            default:

                break;
        }

        session.setAttribute("cartCount", cartService.getCartItemCount(clientId));
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
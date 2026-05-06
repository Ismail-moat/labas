package com.labas.servlet;

import com.labas.dto.OrderDTO;
import com.labas.service.OrderService;
import com.labas.util.AuditLogger;
import com.labas.util.CsrfUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet("/admin/orders")
public class AdminOrdersServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    private static final Set<String> VALID_STATUSES = Set.of(
        "pending", "confirmed", "shipped", "delivered", "cancelled"
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<OrderDTO> orders = orderService.getAllOrders();
        request.setAttribute("orders", orders);
        CsrfUtil.getToken(request.getSession(true));
        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!CsrfUtil.isValid(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        HttpSession session  = request.getSession(false);
        String adminEmail    = session != null ? (String) session.getAttribute("email") : "unknown";
        String action        = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            String orderIdStr = request.getParameter("orderId");
            String status     = request.getParameter("status");

            if (orderIdStr == null || !orderIdStr.matches("\\d+")) {
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }
            if (status == null || !VALID_STATUSES.contains(status.toLowerCase())) {
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            boolean updated = orderService.updateOrderStatus(orderId, status.toLowerCase());
            if (updated) {
                AuditLogger.logAdminAction(adminEmail, "UPDATE_ORDER_STATUS",
                    "orderId=" + orderId + " status=" + status);
            }
        }

        CsrfUtil.rotate(request.getSession(true));
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}

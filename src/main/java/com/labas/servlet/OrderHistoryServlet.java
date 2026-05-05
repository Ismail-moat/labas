package com.labas.servlet;

import com.labas.model.Order;
import com.labas.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders")


public class OrderHistoryServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    
    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login") ;
            
            return;
        }

        int userId = (int) session.getAttribute("userId");
        
        List<Order> orders = orderService.getOrdersByUserId(userId);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/pages/orders.jsp").forward(request, response);

    
    }
}

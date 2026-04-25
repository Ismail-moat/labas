package com.labas.servlet;

import com.labas.dto.OrderDTO;
import com.labas.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/orders")
public class AdminOrdersServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        List<OrderDTO> orders = orderService.getAllOrders();
        request.setAttribute("orders", orders);
        
        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }
}

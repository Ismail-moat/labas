package com.labas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/confirmation")
public class ConfirmationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer orderId = (Integer) session.getAttribute("lastOrderId");
        if (orderId == null) {
        	
        	
        	
        	
        	
            // No order was just placed, redirect to home
        	
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        

        request.setAttribute("orderId", orderId);
        
        // clear so refreshing doesn't re-show confirmation
        
        session.removeAttribute("lastOrderId");
        request.getRequestDispatcher("/pages/confirmation.jsp").forward(request, response);
    }
}
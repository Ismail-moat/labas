package com.labas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/settings")
public class AdminSettingsServlet extends HttpServlet {

    private final com.labas.dao.UserDAO userDAO = new com.labas.dao.UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin/settings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if ("security".equals(action)) {
            String newEmail = request.getParameter("email");
            String newPass  = request.getParameter("password");
            int userId      = (int) session.getAttribute("userId");

            com.labas.model.User user = new com.labas.model.User();
            user.setIdUser(userId);
            user.setEmail(newEmail);
            
            if (newPass != null && !newPass.isBlank()) {
                user.setPassword(com.labas.util.PasswordUtil.hashPassword(newPass));
            } else {
                com.labas.model.User current = userDAO.findByEmail((String) session.getAttribute("email"));
                user.setPassword(current.getPassword());
            }

            if (userDAO.update(user)) {
                session.setAttribute("email", newEmail);
                request.setAttribute("success", "Profil mis à jour avec succès.");
            } else {
                request.setAttribute("error", "Erreur lors de la mise à jour.");
            }
        }

        doGet(request, response);
    }
}

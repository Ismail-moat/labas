package com.labas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * LogoutServlet - Gère la déconnexion de l'utilisateur.
 * Invalide la session actuelle et redirige vers la page de connexion.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        // Redirection vers l'URL mapping du login
        response.sendRedirect(request.getContextPath() + "/login");
    }
}

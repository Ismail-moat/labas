package com.labas.servlet;

import com.labas.model.Client;
import com.labas.model.User;
import com.labas.service.AuthService;
import com.labas.service.ClientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * LoginServlet - Gère l'authentification des utilisateurs.
 * Délègue la vérification des identifiants à AuthService.
 * Redirige vers le dashboard admin ou la page profil selon le rôle.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private ClientService clientService = new ClientService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        // Authentification via AuthService (respecte le diagramme de séquence login)
        User user = authService.authenticate(email, password);

        if (user == null) {
            request.setAttribute("erreur", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        // Création de la session HTTP avec userId et role
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getIdUser());
        session.setAttribute("email",  user.getEmail());
        session.setAttribute("role",   user.getRole());

        // Redirection selon le rôle
        if ("client".equalsIgnoreCase(user.getRole())) {
            // Charger les infos du client dans la session
            Client client = clientService.trouverParUser(user);

            if (client != null) {
                session.setAttribute("firstName", client.getFirstName());
                session.setAttribute("lastName",  client.getLastName());
                session.setAttribute("username",  client.getUsername());
                session.setAttribute("phone",     client.getPhone());
                session.setAttribute("address",   client.getAddress());
                session.setAttribute("city",      client.getCity());
                session.setAttribute("zipCode",   client.getZipCode());
            }

            response.sendRedirect(request.getContextPath() + "/pages/profile.jsp");

        } else {
            // L'admin est redirigé vers le dashboard (protégé par AdminFilter)
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }
}
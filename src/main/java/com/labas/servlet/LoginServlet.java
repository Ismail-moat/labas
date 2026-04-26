package com.labas.servlet;

import com.labas.model.Client;
import com.labas.model.User;
import com.labas.service.AuthService;
import com.labas.service.ClientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private ClientService clientService = new ClientService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        User user = authService.authenticate(email, password);

        if (user == null) {
            request.setAttribute("erreur", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getIdUser());
        session.setAttribute("email",  user.getEmail());
        session.setAttribute("role",   user.getRole());

        if ("client".equalsIgnoreCase(user.getRole())) {
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
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }
}

package com.labas.servlet;

import com.labas.model.Client;
import com.labas.model.User;
import com.labas.service.ProfileService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * ProfileServlet - Gère l'affichage et la mise à jour du profil utilisateur.
 * Délègue la logique métier à ProfileService (qui met à jour users ET clients).
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private ProfileService profileService = new ProfileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int    userId   = (int)    session.getAttribute("userId");
        String email    = (String) session.getAttribute("email");

        // Récupération des données du formulaire
        String firstName = request.getParameter("firstName");
        String lastName  = request.getParameter("lastName");
        String username  = request.getParameter("username");
        String phone     = request.getParameter("phone");
        String address   = request.getParameter("address");
        String city      = request.getParameter("city");
        String zipCode   = request.getParameter("zipCode");

        // Objet User (pour update email si fourni)
        String newEmail = request.getParameter("email");
        User user = new User();
        user.setIdUser(userId);
        user.setEmail(newEmail != null && !newEmail.isBlank() ? newEmail : email);
        user.setPassword((String) session.getAttribute("password")); // conserve le mot de passe actuel

        // Objet Client
        Client client = new Client();
        client.setIdUser(userId);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setUsername(username);
        client.setPhone(phone);
        client.setAddress(address);
        client.setCity(city);
        client.setZipCode(zipCode);

        // Délégation à ProfileService (met à jour users ET clients)
        boolean succes = profileService.updateProfile(user, client);

        if (succes) {
            // Mise à jour de la session
            session.setAttribute("email",     user.getEmail());
            session.setAttribute("firstName", firstName);
            session.setAttribute("lastName",  lastName);
            session.setAttribute("username",  username);
            session.setAttribute("phone",     phone);
            session.setAttribute("address",   address);
            session.setAttribute("city",      city);
            session.setAttribute("zipCode",   zipCode);

            response.sendRedirect(request.getContextPath() + "/pages/profile.jsp?succes=1");
        } else {
            request.setAttribute("erreur", "Une erreur est survenue. Veuillez réessayer.");
            request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
        }
    }
}

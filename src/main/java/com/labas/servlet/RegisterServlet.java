package com.labas.servlet;

import com.labas.model.Client;
import com.labas.service.ClientService;
import com.labas.util.AuditLogger;
import com.labas.util.CsrfUtil;
import com.labas.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("erreur", "Requête invalide. Veuillez réessayer.");
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            return;
        }

        String email     = request.getParameter("email");
        String password  = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName  = request.getParameter("lastName");
        String username  = request.getParameter("username");
        String phone     = request.getParameter("phone");
        String address   = request.getParameter("address");
        String city      = request.getParameter("city");
        String zipCode   = request.getParameter("zipCode");

        if (email == null || email.isBlank() || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            request.setAttribute("erreur", "Adresse email invalide.");
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            return;
        }

        String policyError = PasswordUtil.validatePolicy(password);
        if (policyError != null) {
            request.setAttribute("erreur", policyError);
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            return;
        }

        Client client = new Client();
        client.setEmail(email.trim().toLowerCase());
        client.setPassword(password);
        client.setRole("client");
        client.setFirstName(firstName != null ? firstName.trim() : "");
        client.setLastName(lastName  != null ? lastName.trim()  : "");
        client.setUsername(username  != null ? username.trim()  : "");
        client.setPhone(phone        != null ? phone.trim()     : "");
        client.setAddress(address    != null ? address.trim()   : "");
        client.setCity(city          != null ? city.trim()      : "");
        client.setZipCode(zipCode    != null ? zipCode.trim()   : "");

        int resultat = clientService.inscrireClient(client);

        if (resultat > 0) {
            AuditLogger.logRegistration(email, getClientIp(request));

            CsrfUtil.rotate(request.getSession(true));
            response.sendRedirect(request.getContextPath() + "/login?succes=1");
        } else if (resultat == -1) {
            request.setAttribute("erreur", "Cet email est déjà utilisé. Veuillez vous connecter.");
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
        } else {
            request.setAttribute("erreur", "Une erreur est survenue. Veuillez réessayer.");
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        CsrfUtil.getToken(request.getSession(true));
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) return ip.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}

package com.labas.servlet;

import com.labas.model.Client;
import com.labas.service.ClientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private ClientService clientService = new ClientService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        String email     = request.getParameter("email");
        String password  = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName  = request.getParameter("lastName");
        String username  = request.getParameter("username");
        String phone     = request.getParameter("phone");
        String address   = request.getParameter("address");
        String city      = request.getParameter("city");
        String zipCode   = request.getParameter("zipCode");

        Client client = new Client();
        client.setEmail(email);
        client.setPassword(password);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setUsername(username);
        client.setPhone(phone);
        client.setAddress(address);
        client.setCity(city);
        client.setZipCode(zipCode);

        int resultat = clientService.inscrireClient(client);

        if (resultat > 0) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp?succes=1");
        } else {
            request.setAttribute("erreur", "Cet email est déjà utilisé. Veuillez vous connecter.");
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }
}
package com.labas.servlet;

import com.labas.model.Client;
import com.labas.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminClientsServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Client> clients = clientService.getAllClients();
        request.setAttribute("clients", clients);

        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }
}


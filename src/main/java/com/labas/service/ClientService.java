package com.labas.service;

import com.labas.dao.ClientDAO;
import com.labas.dao.UserDAO;
import com.labas.model.Client;
import com.labas.model.User;


public class ClientService {

    private UserDAO userDAO = new UserDAO();
    private ClientDAO clientDAO = new ClientDAO();


    public int inscrireClient(Client client) {

        if (userDAO.emailExiste(client.getEmail())) {
            return -1;
        }

        User user = new User();
        user.setEmail(client.getEmail());
        user.setPassword(client.getPassword());
        user.setRole("client");

        int userId = userDAO.saveUser(user);

        if (userId > 0) {
            client.setIdUser(userId);
            return clientDAO.saveClient(client);
        }

        return -1;
    }


    public Client trouverParUser(User user) {
        return clientDAO.findByUserId(user.getIdUser());
    }


    public boolean mettreAJour(Client client) {
        return clientDAO.update(client);
    }
}

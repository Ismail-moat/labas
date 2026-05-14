package com.labas.service;

import com.labas.dao.ClientDAO;
import com.labas.dao.UserDAO;
import com.labas.model.Client;
import com.labas.model.User;
import com.labas.util.PasswordUtil;

import java.util.List;

public class ClientService {

    private final ClientDAO clientDAO = new ClientDAO();
    private final UserDAO userDAO = new UserDAO();

    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    public Client trouverParUser(User user) {
        if (user == null) return null;
        return clientDAO.findByUserId(user.getIdUser());
    }

    public int inscrireClient(Client client) {
        if (userDAO.emailExiste(client.getEmail())) {
            return -1; // Email existe déjà
        }
        if (clientDAO.usernameExiste(client.getUsername())) {
            return -2; // Username existe déjà
        }

        client.setPassword(PasswordUtil.hashPassword(client.getPassword()));

        int userId = userDAO.saveUser(client);
        if (userId > 0) {
            client.setIdUser(userId);
            int clientResult = clientDAO.saveClient(client);
            return (clientResult > 0) ? 1 : -3; // 1 = Succès, -3 = Erreur DB Client
        }
        return -3; // Erreur DB User
    }
}

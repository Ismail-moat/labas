package com.labas.service;

import com.labas.dao.ClientDAO;
import com.labas.dao.UserDAO;
import com.labas.model.Client;
import com.labas.model.User;

public class ProfileService {

    private UserDAO userDAO;
    private ClientDAO clientDAO;

    public ProfileService() {
        this.userDAO = new UserDAO();
        this.clientDAO = new ClientDAO();
    }

    public boolean updateProfile(User user, Client client) {
        boolean userUpdated = userDAO.update(user);

        boolean clientUpdated = false;
        if (userUpdated) {
            clientUpdated = clientDAO.update(client);
        }

        return userUpdated && clientUpdated;
    }
}


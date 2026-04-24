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
        // Update user info (email, password)
        boolean userUpdated = userDAO.update(user);
        
        // Update client info
        boolean clientUpdated = false;
        if (userUpdated) {
            clientUpdated = clientDAO.update(client);
        }
        
        return userUpdated && clientUpdated;
    }
}

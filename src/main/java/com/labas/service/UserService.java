package com.labas.service;

import com.labas.dao.UserDAO;
import com.labas.model.User;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public User connecter(String email, String password) {

        return userDAO.login(email, password);
    }

    public int getCustomerCount() {
        return userDAO.countClients();
    }
}


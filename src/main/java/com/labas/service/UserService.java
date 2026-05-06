package com.labas.service;

import com.labas.dao.UserDAO;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public int getCustomerCount() {
        return userDAO.countClients();
    }
}

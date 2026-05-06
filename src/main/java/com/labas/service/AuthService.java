package com.labas.service;

import com.labas.dao.UserDAO;
import com.labas.model.User;
import com.labas.util.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String email, String password) {
        if (email == null || password == null) return null;
        User user = userDAO.findByEmail(email);
        if (user == null) return null;
        if (!PasswordUtil.checkPassword(password, user.getPassword())) return null;
        return user;
    }
}

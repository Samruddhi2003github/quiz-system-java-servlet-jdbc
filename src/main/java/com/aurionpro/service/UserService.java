package com.aurionpro.service;

import com.aurionpro.dao.UserDao;
import com.aurionpro.model.User;

public class UserService {
    private UserDao userDao = new UserDao();

    public boolean login(String username, String password) {
        return userDao.validateUser(username, password);
    }

    public boolean register(String username, String password) {
        User user = new User(username, password);
        return userDao.registerUser(user);
    }
}
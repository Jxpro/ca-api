package com.jokerxin.x509ca.service;

import com.jokerxin.x509ca.entity.User;

public interface UserService {
    void saveUser(User user);

    User getByUsername(String username);

    User getById(int userId);
}

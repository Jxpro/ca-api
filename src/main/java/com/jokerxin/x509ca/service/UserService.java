package com.jokerxin.x509ca.service;

import com.jokerxin.x509ca.entity.User;

public interface UserService {
    int saveUser(User user);

    boolean existUser(String username);
}

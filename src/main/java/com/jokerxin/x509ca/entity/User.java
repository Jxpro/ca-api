package com.jokerxin.x509ca.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private byte authority;
}

package com.jokerxin.x509ca.bean;

import lombok.Data;

@Data
public class User {
    private long id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private byte authority;
}

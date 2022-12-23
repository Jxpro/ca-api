package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private Byte authority;
}

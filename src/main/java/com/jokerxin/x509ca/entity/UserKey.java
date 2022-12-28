package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserKey {
    @TableId(type = IdType.AUTO)
    private Integer id;
    // RSA-2048 或 ECC-prime256v1
    private String algorithm;
    // 十六进制，n或x
    private String param1;
    // 十六进制，e或y
    private String param2;
}

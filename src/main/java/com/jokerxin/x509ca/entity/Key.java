package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Key {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer requestID;
    // ECC 或 RSA
    private String algorithm;
    // 暂限三选一：secp256k1，sm2p256v1，prime256v1
    private String curveName;
    // 2048 或 4096
    private Integer keySize;
    private String rsaN;
    private String rsaE;
    private String eccX;
    private String eccY;
}

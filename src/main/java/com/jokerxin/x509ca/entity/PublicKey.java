package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class PublicKey {
    @TableId(type = IdType.AUTO)
    private Integer id;
    // ECC 或 RSA
    private String algorithm;
    // 暂限三选一：secp256k1，sm2p256v1，prime256v1
    private String curveName;
    // 2048 或 4096
    private Integer keySize;
    private String param1;
    private String param2;
}

package com.jokerxin.x509ca.utils;

import io.jsonwebtoken.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class JWTUtil {
    // 密钥种子，用于生成固定的密钥
    private static final String seed = "jwt12138";
    // 签名秘钥，使用HmacSHA256算法
    private static final SecretKey secretKey;
    // 过期时间，单位毫秒
    private static final long oneDay = 24 * 60 * 60 * 1000;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256, new SecureRandom(seed.getBytes()));
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 生成token
    // 根据用户id和用户名生成token
    public static String createToken(int userId, String userName) {
        return Jwts.builder()
                .setSubject("CA-USER")
                .setExpiration(new Date(System.currentTimeMillis() + oneDay))
                .claim("uid", userId)
                .claim("usr", userName)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析token
    // 返回用户id,如果token无效或过期，返回-1
    public static int getUserId(String token) {
        int id;
        try {
            id = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("uid", Integer.class);
        } catch (JwtException e) {
            id = -1;
        }
        return id;
    }
}

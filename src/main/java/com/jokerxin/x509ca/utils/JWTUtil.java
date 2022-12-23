package com.jokerxin.x509ca.utils;

import io.jsonwebtoken.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class JWTUtil {
    //签名秘钥，使用HmacSHA256算法
    private static final SecretKey secretKey;
    //过期时间，单位毫秒
    private static final long oneDay = 24 * 60 * 60 * 1000;

    static {
        try {
            // 秘钥种子
            int seed = 12138;
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            // 使用固定种子，使每次生成的秘钥相同
            keyGenerator.init(256, new SecureRandom(new byte[seed]));
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

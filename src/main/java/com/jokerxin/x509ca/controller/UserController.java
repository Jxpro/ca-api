package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.entity.User;
import com.jokerxin.x509ca.service.UserService;
import com.jokerxin.x509ca.utils.HashUtil;
import com.jokerxin.x509ca.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public Map<String, Object> register(User user, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        // 401 这里代表用户已经存在
        map.put("code", 401);
        // 判断用户名是否已存在，如果存在则返回错误信息，否则将用户信息存入数据库
        if (!userService.existUser(user.getUsername())) {
            user.setPassword(HashUtil.sha256(user.getPassword()));
            userService.saveUser(user);
            map.put("code", 200);
            map.put("name", user.getNickname());
            map.put("role", user.getAuthority() == 1 ? "admin" : "user");
            response.setHeader("Authorization", JWTUtil.createToken(user.getId(), user.getUsername()));
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
        }
        return map;
    }

    @PostMapping("/user/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password,
                                     HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 401);
        // TODO: 判断用户名和密码是否正确，如果正确则返回用户信息，否则返回错误信息
        if (username.equals("admin") && password.equals(HashUtil.sha256("adminadmin"))) {
            map.put("code", 200);
            map.put("name", "admin");
            response.setHeader("Authorization", "token");
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
        }
        return map;
    }

    @GetMapping("/user/info")
    public Map<String, Object> userInfo(@RequestHeader(value = "Authorization") String token,
                                        HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        // TODO: 判断token是否正确，如果正确则返回用户信息，否则返回错误信息
        map.put("name", "admin");
        map.put("role", "admin");
        return map;
    }
}

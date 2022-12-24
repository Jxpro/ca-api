package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.annotation.PassLogin;
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

    @PassLogin
    @PostMapping("/user/register")
    public Map<String, Object> register(User user, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        // 401 这里代表用户已经存在
        map.put("code", 401);
        // 判断用户名是否已存在，如果存在则返回错误信息，否则将用户信息存入数据库
        if (userService.getByUsername(user.getUsername()) != null) {
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

    @PassLogin
    @PostMapping("/user/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password,
                                     HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        // 401 这里代表用户名或密码错误
        map.put("code", 401);
        // 获取用户信息
        User user = userService.getByUsername(username);
        if (HashUtil.sha256(password).equals(user.getPassword())) {
            map.put("code", 200);
            map.put("name", username);
            map.put("role", user.getAuthority() == 1 ? "admin" : "user");
            response.setHeader("Authorization", JWTUtil.createToken(user.getId(), username));
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
        }
        return map;
    }

    @GetMapping("/user/info")
    public Map<String, Object> userInfo(@RequestAttribute int userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.getById(userId);
        map.put("name", user.getUsername());
        map.put("role", user.getAuthority() == 1 ? "admin" : "user");
        return map;
    }
}

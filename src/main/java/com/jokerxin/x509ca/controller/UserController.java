package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.annotation.PassLogin;
import com.jokerxin.x509ca.entity.User;
import com.jokerxin.x509ca.http.Result;
import com.jokerxin.x509ca.service.UserService;
import com.jokerxin.x509ca.utils.HashUtil;
import com.jokerxin.x509ca.utils.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
public class UserController {
    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PassLogin
    @PostMapping("/user/register")
    public Result<Object> register(User user, HttpServletResponse response) {
        if (userService.getByUsername(user.getUsername()) != null) {
            return Result.unauthorized();
        } else {
            Map<String, Object> map = new HashMap<>();
            user.setPassword(HashUtil.sha256(user.getPassword().getBytes()));
            userService.saveUser(user);
            map.put("name", user.getNickname());
            map.put("role", user.getRole());
            response.setHeader("Authorization", JWTUtil.createToken(user.getId(), user.getUsername()));
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            return Result.ok(map);
        }
    }

    @PassLogin
    @PostMapping("/user/login")
    public Result<Object> login(@RequestParam String username,
                                @RequestParam String password,
                                HttpServletResponse response) {
        User user = userService.getByUsername(username);
        if (!Objects.equals(HashUtil.sha256(password.getBytes()), user.getPassword())) {
            return Result.unauthorized();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("name", username);
            map.put("role", user.getRole());
            response.setHeader("Authorization", JWTUtil.createToken(user.getId(), username));
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            return Result.ok(map);
        }
    }

    @GetMapping("/user/info")
    public Result<Object> userInfo(@RequestAttribute int userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.getById(userId);
        map.put("name", user.getUsername());
        map.put("role", user.getRole());
        return Result.ok(map);
    }
}

package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.utils.Hash;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {
    @PostMapping("/user/register")
    public Map<String, Object> register(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam String nickname,
                                        HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 401);
        // TODO: 判断用户名是否已存在，如果存在则返回错误信息，否则将用户信息存入数据库
        if (!username.equals("admin")) {
            map.put("code", 200);
            map.put("name", nickname);
            response.setHeader("Authorization", "token");
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
        if (username.equals("admin") && password.equals(Hash.sha256("adminadmin"))) {
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

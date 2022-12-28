package com.jokerxin.x509ca.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jokerxin.x509ca.entity.User;
import com.jokerxin.x509ca.mapper.UserMapper;
import com.jokerxin.x509ca.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void saveUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 使用 LambdaQueryWrapper 可以避免 wrapper.eq("username", username) 中的username写错
        // last("limit 1") 仅查询一条数据，提高效率
        wrapper.eq(User::getUsername, username).last("limit 1");
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getById(int userId) {
        return userMapper.selectById(userId);
    }
}

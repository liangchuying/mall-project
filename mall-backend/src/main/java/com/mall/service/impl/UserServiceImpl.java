package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import com.mall.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public void register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (baseMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("用户名已存在");
        }

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, user.getPhone());
        if (baseMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("手机号已注册");
        }

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, user.getEmail());
        if (baseMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("邮箱已注册");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        baseMapper.insert(user);
    }

    @Override
    public String login(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return jwtUtil.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public User getUserById(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return baseMapper.selectOne(wrapper);
    }
}

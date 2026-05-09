package com.mall.service;

import com.mall.entity.User;

public interface UserService {

    void register(User user);

    String login(String username, String password);

    User getUserById(Long userId);

    User getUserByUsername(String username);
}

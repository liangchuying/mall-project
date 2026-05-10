package com.mall.service;

import com.mall.entity.User;

public interface UserService {

    void register(User user);

    String login(String username, String password);

    User getUserById(Long userId);

    User getUserByUsername(String username);

    User getUserByPhone(String phone);

    void logout(String token);

    void updateUserWithLock(User user);

    void sendResetCode(String phone);

    void resetPassword(String phone, String code, String newPassword);

    void changePassword(Long userId, String oldPassword, String newPassword);
}

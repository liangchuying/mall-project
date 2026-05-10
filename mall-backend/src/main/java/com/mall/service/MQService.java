package com.mall.service;

public interface MQService {

    void sendUserRegisterMessage(Long userId, String username, String nickname, String phone, String email);

    void sendUserLoginMessage(Long userId, String username, String loginIp);
}

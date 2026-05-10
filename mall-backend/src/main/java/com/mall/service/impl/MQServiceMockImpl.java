package com.mall.service.impl;

import com.mall.service.MQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingBean(MQServiceImpl.class)
public class MQServiceMockImpl implements MQService {

    private static final Logger log = LoggerFactory.getLogger(MQServiceMockImpl.class);

    @Override
    public void sendUserRegisterMessage(Long userId, String username, String nickname, String phone, String email) {
        log.info("Mock: 用户注册消息 - userId={}, username={}", userId, username);
    }

    @Override
    public void sendUserLoginMessage(Long userId, String username, String loginIp) {
        log.info("Mock: 用户登录消息 - userId={}, username={}, loginIp={}", userId, username, loginIp);
    }
}

package com.mall.service.impl;

import com.mall.mq.message.UserLoginMessage;
import com.mall.mq.message.UserRegisterMessage;
import com.mall.mq.producer.UserMQProducer;
import com.mall.service.MQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@ConditionalOnProperty(prefix = "rocketmq", name = "name-server", matchIfMissing = false)
public class MQServiceImpl implements MQService {

    @Autowired
    private UserMQProducer userMQProducer;

    @Override
    public void sendUserRegisterMessage(Long userId, String username, String nickname, String phone, String email) {
        UserRegisterMessage message = new UserRegisterMessage();
        message.setUserId(userId);
        message.setUsername(username);
        message.setNickname(nickname);
        message.setPhone(phone);
        message.setEmail(email);
        message.setRegisterTime(LocalDateTime.now());
        userMQProducer.sendUserRegisterMessage(message);
    }

    @Override
    public void sendUserLoginMessage(Long userId, String username, String loginIp) {
        UserLoginMessage message = new UserLoginMessage();
        message.setUserId(userId);
        message.setUsername(username);
        message.setLoginIp(loginIp);
        message.setLoginTime(LocalDateTime.now());
        userMQProducer.sendUserLoginMessage(message);
    }
}

package com.mall.mq.producer;

import com.mall.config.RocketMQConfig;
import com.mall.mq.message.UserRegisterMessage;
import com.mall.mq.message.UserLoginMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "rocketmq", name = "name-server")
public class UserMQProducer {

    private static final Logger log = LoggerFactory.getLogger(UserMQProducer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendUserRegisterMessage(UserRegisterMessage message) {
        try {
            rocketMQTemplate.syncSend(RocketMQConfig.USER_REGISTER_TOPIC, message);
            log.info("用户注册消息发送成功: userId={}", message.getUserId());
        } catch (Exception e) {
            log.error("用户注册消息发送失败: userId={}", message.getUserId(), e);
        }
    }

    public void sendUserLoginMessage(UserLoginMessage message) {
        try {
            rocketMQTemplate.syncSend(RocketMQConfig.USER_LOGIN_TOPIC, message);
            log.info("用户登录消息发送成功: userId={}", message.getUserId());
        } catch (Exception e) {
            log.error("用户登录消息发送失败: userId={}", message.getUserId(), e);
        }
    }
}

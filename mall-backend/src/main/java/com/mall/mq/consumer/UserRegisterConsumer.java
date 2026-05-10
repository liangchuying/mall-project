package com.mall.mq.consumer;

import com.mall.mq.message.UserRegisterMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.user-register:user-register-topic}",
        consumerGroup = "${rocketmq.consumer.user-register-group:user-register-consumer-group}"
)
public class UserRegisterConsumer implements RocketMQListener<UserRegisterMessage> {

    private static final Logger log = LoggerFactory.getLogger(UserRegisterConsumer.class);

    @Override
    public void onMessage(UserRegisterMessage message) {
        try {
            log.info("收到用户注册消息: userId={}, username={}, nickname={}",
                    message.getUserId(), message.getUsername(), message.getNickname());

            processUserRegister(message);

            log.info("用户注册消息处理完成: userId={}", message.getUserId());
        } catch (Exception e) {
            log.error("处理用户注册消息失败: userId={}", message.getUserId(), e);
            throw new RuntimeException("处理用户注册消息失败", e);
        }
    }

    private void processUserRegister(UserRegisterMessage message) {
        log.info("处理用户注册业务逻辑: 发送欢迎邮件、初始化用户数据等");
    }
}

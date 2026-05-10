package com.mall.mq.consumer;

import com.mall.mq.message.UserLoginMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.user-login:user-login-topic}",
        consumerGroup = "${rocketmq.consumer.user-login-group:user-login-consumer-group}"
)
public class UserLoginConsumer implements RocketMQListener<UserLoginMessage> {

    private static final Logger log = LoggerFactory.getLogger(UserLoginConsumer.class);

    @Override
    public void onMessage(UserLoginMessage message) {
        try {
            log.info("收到用户登录消息: userId={}, username={}, loginIp={}",
                    message.getUserId(), message.getUsername(), message.getLoginIp());

            processUserLogin(message);

            log.info("用户登录消息处理完成: userId={}", message.getUserId());
        } catch (Exception e) {
            log.error("处理用户登录消息失败: userId={}", message.getUserId(), e);
            throw new RuntimeException("处理用户登录消息失败", e);
        }
    }

    private void processUserLogin(UserLoginMessage message) {
        log.info("处理用户登录业务逻辑: 更新最后登录时间、记录登录日志等");
    }
}

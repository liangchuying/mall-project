package com.mall.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "rocketmq", name = "name-server", matchIfMissing = false)
public class RocketMQConfig {

    public static final String USER_REGISTER_TOPIC = "user-register-topic";
    public static final String USER_LOGIN_TOPIC = "user-login-topic";
    public static final String ORDER_CREATED_TOPIC = "order-created-topic";
    public static final String ORDER_PAID_TOPIC = "order-paid-topic";
}

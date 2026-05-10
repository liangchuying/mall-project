package com.mall.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class IdempotentUtil {

    @Autowired
    private RedisUtil redisUtil;

    private static final String TOKEN_PREFIX = "idempotent:token:";

    public String createToken() {
        String token = UUID.randomUUID().toString().replace("-", "");
        redisUtil.set(TOKEN_PREFIX + token, "1", 10, TimeUnit.MINUTES);
        return token;
    }

    public boolean checkAndSet(String token, int expireSeconds) {
        String key = "idempotent:" + token;
        return redisUtil.setIfAbsent(key, "1", expireSeconds, TimeUnit.SECONDS);
    }

    public void deleteToken(String token) {
        redisUtil.delete(TOKEN_PREFIX + token);
    }
}

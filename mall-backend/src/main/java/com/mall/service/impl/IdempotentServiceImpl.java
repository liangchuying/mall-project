package com.mall.service.impl;

import com.mall.service.IdempotentService;
import com.mall.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IdempotentServiceImpl implements IdempotentService {

    @Autowired
    private RedisUtil redisUtil;

    private static final String TOKEN_PREFIX = "idempotent:token:";

    @Override
    public String createToken() {
        String token = UUID.randomUUID().toString().replace("-", "");
        redisUtil.set(TOKEN_PREFIX + token, "1", 10, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public boolean validateToken(String token) {
        String key = TOKEN_PREFIX + token;
        Boolean exists = redisUtil.hasKey(key);
        if (exists) {
            redisUtil.delete(key);
            return true;
        }
        return false;
    }
}

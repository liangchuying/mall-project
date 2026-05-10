package com.mall.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitUtil {

    @Autowired
    private RedisUtil redisUtil;

    private static final String LUA_SCRIPT = "local key = KEYS[1]\n" +
            "local limit = tonumber(ARGV[1])\n" +
            "local expire = tonumber(ARGV[2])\n" +
            "local current = tonumber(redis.call('GET', key) or '0')\n" +
            "if current + 1 > limit then\n" +
            "  return 0\n" +
            "else\n" +
            "  redis.call('INCR', key)\n" +
            "  if current == 0 then\n" +
            "    redis.call('EXPIRE', key, expire)\n" +
            "  end\n" +
            "  return 1\n" +
            "end";

    public boolean allowRequest(String key, int limit, int expireSeconds) {
        Object result = redisUtil.redisTemplate().execute(
                RedisScript.of(LUA_SCRIPT, Long.class),
                Collections.singletonList(key),
                String.valueOf(limit),
                String.valueOf(expireSeconds)
        );
        return result != null && ((Long) result) == 1;
    }
}

package com.mall.aspect;

import com.mall.annotation.Idempotent;
import com.mall.exception.IdempotentException;
import com.mall.utils.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class IdempotentAspect {

    @Autowired
    private RedisUtil redisUtil;

    private static final String IDEMPOTENT_PREFIX = "idempotent:";

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Idempotent-Token");

        if (token == null || token.isEmpty()) {
            throw new IdempotentException("请先获取幂等性 Token");
        }

        String key = IDEMPOTENT_PREFIX + token;
        int expireTime = idempotent.expireTime();
        String info = idempotent.info();

        Boolean success = redisUtil.setIfAbsent(key, "1", expireTime, TimeUnit.SECONDS);
        if (!success) {
            throw new IdempotentException(info);
        }

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            redisUtil.delete(key);
            throw e;
        }
    }
}

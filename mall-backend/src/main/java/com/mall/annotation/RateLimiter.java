package com.mall.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    int count() default 100;

    int time() default 60;

    String prefix() default "rate_limit:";

    String message() default "请求过于频繁，请稍后再试";
}

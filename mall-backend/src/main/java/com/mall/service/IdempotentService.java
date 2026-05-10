package com.mall.service;

public interface IdempotentService {

    String createToken();

    boolean validateToken(String token);
}

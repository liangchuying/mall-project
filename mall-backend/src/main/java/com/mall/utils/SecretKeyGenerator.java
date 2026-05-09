package com.mall.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * JWT 密钥生成工具
 *
 * 生产环境请使用此工具生成随机密钥，不要使用默认密钥
 */
public class SecretKeyGenerator {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final int KEY_LENGTH = 256;

    /**
     * 生成随机密钥（Base64 编码）
     *
     * @return Base64 编码的密钥字符串
     */
    public static String generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[KEY_LENGTH / 8];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * 从字符串生成 SecretKey
     *
     * @param keyString 密钥字符串
     * @return SecretKey 对象
     */
    public static SecretKey getSecretKey(String keyString) {
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, HMAC_SHA256);
    }

    public static void main(String[] args) {
        System.out.println("=== JWT 密钥生成工具 ===");
        System.out.println();
        System.out.println("请使用以下生成的随机密钥（Base64 编码）：");
        System.out.println();
        String key = generateKey();
        System.out.println(key);
        System.out.println();
        System.out.println("将此密钥设置为环境变量 JWT_SECRET 或配置文件中 jwt.secret 的值");
    }
}

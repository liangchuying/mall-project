package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.annotation.DistributedLock;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.MQService;
import com.mall.service.UserService;
import com.mall.utils.JwtUtil;
import com.mall.utils.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired(required = false)
    private MQService mqService;

    private static final String USER_CACHE_PREFIX = "user:";
    private static final long USER_CACHE_EXPIRE = 30;
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    @Override
    @Transactional
    public void register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (baseMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("用户名已存在");
        }

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, user.getPhone());
        if (baseMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("手机号已注册");
        }

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, user.getEmail());
        if (baseMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("邮箱已注册");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        baseMapper.insert(user);

        if (mqService != null) {
            mqService.sendUserRegisterMessage(user.getId(), user.getUsername(), user.getNickname(), user.getPhone(), user.getEmail());
        }
    }

    @Override
    public String login(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        if (mqService != null) {
            String loginIp = getClientIp();
            mqService.sendUserLoginMessage(user.getId(), user.getUsername(), loginIp);
        }

        return token;
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("x-forwarded-for");
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
            }
        } catch (Exception e) {
            // ignore
        }
        return "unknown";
    }

    @Override
    public User getUserById(Long userId) {
        String cacheKey = USER_CACHE_PREFIX + userId;
        User cachedUser = (User) redisUtil.get(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        User user = baseMapper.selectById(userId);
        if (user != null) {
            user.setPassword(null);
            redisUtil.set(cacheKey, user, USER_CACHE_EXPIRE, TimeUnit.MINUTES);
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        String cacheKey = USER_CACHE_PREFIX + "username:" + username;
        User cachedUser = (User) redisUtil.get(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(wrapper);
        if (user != null) {
            String userIdKey = USER_CACHE_PREFIX + user.getId();
            redisUtil.set(cacheKey, user, USER_CACHE_EXPIRE, TimeUnit.MINUTES);
            redisUtil.set(userIdKey, user, USER_CACHE_EXPIRE, TimeUnit.MINUTES);
        }
        return user;
    }

    @Override
    public void logout(String token) {
        try {
            Long expireTime = jwtUtil.getExpirationTime(token) - System.currentTimeMillis();
            if (expireTime > 0) {
                String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;
                redisUtil.set(blacklistKey, "1", expireTime, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            throw new RuntimeException("登出失败");
        }
    }

    @Override
    @DistributedLock(key = "#user.id", waitTime = 5, leaseTime = 30)
    @Transactional
    public void updateUserWithLock(User user) {
        baseMapper.updateById(user);
        String cacheKey = USER_CACHE_PREFIX + user.getId();
        redisUtil.delete(cacheKey);
        String usernameKey = USER_CACHE_PREFIX + "username:" + user.getUsername();
        redisUtil.delete(usernameKey);
    }
}

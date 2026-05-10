package com.mall.mq.message;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserLoginMessage implements Serializable {

    private Long userId;

    private String username;

    private String loginIp;

    private LocalDateTime loginTime;

    public UserLoginMessage() {
    }

    public UserLoginMessage(Long userId, String username, String loginIp, LocalDateTime loginTime) {
        this.userId = userId;
        this.username = username;
        this.loginIp = loginIp;
        this.loginTime = loginTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}

package com.mall.mq.message;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserRegisterMessage implements Serializable {

    private Long userId;

    private String username;

    private String nickname;

    private String phone;

    private String email;

    private LocalDateTime registerTime;

    public UserRegisterMessage() {
    }

    public UserRegisterMessage(Long userId, String username, String nickname, String phone, String email, LocalDateTime registerTime) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.registerTime = registerTime;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }
}

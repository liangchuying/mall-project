package com.mall.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户登录响应数据")
public class LoginResponseVO {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "testuser")
    private String username;

    @Schema(description = "昵称", example = "测试用户")
    private String nickname;

    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    public LoginResponseVO() {
    }

    public LoginResponseVO(Long userId, String username, String nickname, String token) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

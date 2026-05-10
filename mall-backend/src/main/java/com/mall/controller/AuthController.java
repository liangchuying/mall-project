package com.mall.controller;

import com.mall.annotation.RateLimiter;
import com.mall.dto.LoginDTO;
import com.mall.dto.RegisterDTO;
import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.utils.JwtUtil;
import com.mall.utils.Result;
import com.mall.vo.LoginResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "用户注册、登录、获取用户信息等认证相关接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "用户注册", description = "新用户注册，需要提供用户名、密码、昵称、手机号等信息")
    @RateLimiter(count = 5, time = 60, message = "注册请求过于频繁，请稍后再试")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        userService.register(user);
        return Result.success();
    }

    @Operation(summary = "用户登录", description = "使用用户名和密码登录，成功后返回 JWT Token")
    @RateLimiter(count = 10, time = 60, message = "登录请求过于频繁，请稍后再试")
    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto.getUsername(), dto.getPassword());
        User user = userService.getUserByUsername(dto.getUsername());
        LoginResponseVO vo = new LoginResponseVO(user.getId(), user.getUsername(), user.getNickname(), token);
        return Result.success(vo);
    }

    @Operation(summary = "获取用户信息", description = "根据 JWT Token 获取当前登录用户的详细信息")
    @GetMapping("/info")
    public Result<User> getUserInfo(@Parameter(description = "JWT Token，格式：Bearer {token}", required = true) @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        User user = userService.getUserById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @Operation(summary = "用户登出", description = "用户登出，将 token 加入黑名单")
    @PostMapping("/logout")
    public Result<Void> logout(@Parameter(description = "JWT Token，格式：Bearer {token}", required = true) @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        userService.logout(token);
        return Result.success();
    }
}

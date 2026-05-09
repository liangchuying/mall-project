package com.mall.controller;

import com.mall.dto.LoginDTO;
import com.mall.dto.RegisterDTO;
import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.utils.JwtUtil;
import com.mall.utils.Result;
import com.mall.vo.LoginResponseVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto.getUsername(), dto.getPassword());
        User user = userService.getUserByUsername(dto.getUsername());
        LoginResponseVO vo = new LoginResponseVO(user.getId(), user.getUsername(), user.getNickname(), token);
        return Result.success(vo);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        User user = userService.getUserById(userId);
        user.setPassword(null);
        return Result.success(user);
    }
}

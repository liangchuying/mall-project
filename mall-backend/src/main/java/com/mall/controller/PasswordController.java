package com.mall.controller;

import com.mall.dto.ChangePasswordDTO;
import com.mall.dto.ResetPasswordDTO;
import com.mall.dto.SendCodeDTO;
import com.mall.utils.JwtUtil;
import com.mall.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "密码管理", description = "密码重置、修改等密码相关接口")
@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private com.mall.service.UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "发送重置密码验证码", description = "通过手机号发送验证码，用于重置密码")
    @PostMapping("/send-code")
    public Result<Void> sendResetCode(@Valid @RequestBody SendCodeDTO dto) {
        userService.sendResetCode(dto.getPhone());
        return Result.success();
    }

    @Operation(summary = "重置密码", description = "通过手机号和验证码重置密码")
    @PostMapping("/reset")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto.getPhone(), dto.getCode(), dto.getNewPassword());
        return Result.success();
    }

    @Operation(summary = "修改密码", description = "登录后修改当前用户密码，需要验证旧密码")
    @PostMapping("/change")
    public Result<Void> changePassword(
            @Valid @RequestBody ChangePasswordDTO dto,
            @Parameter(description = "JWT Token，格式：Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        userService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return Result.success();
    }
}

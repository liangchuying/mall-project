package com.mall.controller;

import com.mall.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试接口", description = "系统健康检查和测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "系统测试", description = "测试商城系统是否正常启动")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("商城系统启动成功！");
    }
}

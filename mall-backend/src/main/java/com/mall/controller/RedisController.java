package com.mall.controller;

import com.mall.utils.RedisUtil;
import com.mall.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Redis 测试", description = "Redis 缓存测试接口")
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;

    @Operation(summary = "设置缓存", description = "设置键值对，可指定过期时间")
    @PostMapping("/set")
    public Result<Void> set(@RequestParam String key, @RequestParam String value, @RequestParam(required = false, defaultValue = "0") long expire) {
        if (expire > 0) {
            redisUtil.set(key, value, expire, java.util.concurrent.TimeUnit.SECONDS);
        } else {
            redisUtil.set(key, value);
        }
        return Result.success();
    }

    @Operation(summary = "获取缓存", description = "根据 key 获取值")
    @GetMapping("/get")
    public Result<Object> get(@RequestParam String key) {
        Object value = redisUtil.get(key);
        return Result.success(value);
    }

    @Operation(summary = "删除缓存", description = "根据 key 删除缓存")
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam String key) {
        redisUtil.delete(key);
        return Result.success();
    }

    @Operation(summary = "检查 key 是否存在", description = "判断 key 是否存在")
    @GetMapping("/exists")
    public Result<Boolean> exists(@RequestParam String key) {
        return Result.success(redisUtil.hasKey(key));
    }
}

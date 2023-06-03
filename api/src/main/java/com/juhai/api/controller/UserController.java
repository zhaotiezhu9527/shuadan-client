package com.juhai.api.controller;

import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(value = "用户相关", tags = "用户")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest httpServletRequest) {
        return R.ok();
    }
}

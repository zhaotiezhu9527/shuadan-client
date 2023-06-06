package com.juhai.api.controller;

import cn.hutool.core.collection.CollStreamUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.commons.entity.Paramter;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "获取系统配置相关", tags = "获取系统配置相关")
@RequestMapping("/system")
@RestController
public class SiteConfigController {

    @Autowired
    private ParamterService paramterService;

    @ApiOperation(value = "获取系统配置")
    @GetMapping("/config")
    public R config(HttpServletRequest httpServletRequest) {
        List<Paramter> list = paramterService.list(new LambdaQueryWrapper<Paramter>().eq(Paramter::getIsShow, 0));
        Map<String, Object> map = CollStreamUtil.toMap(list, Paramter::getParamKey, Paramter::getParamValue);

        return R.ok().put("data", map);
    }
}

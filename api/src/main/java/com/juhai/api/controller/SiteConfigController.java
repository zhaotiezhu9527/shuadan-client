package com.juhai.api.controller;

import cn.hutool.core.collection.CollStreamUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.commons.entity.MessageText;
import com.juhai.commons.entity.Paramter;
import com.juhai.commons.service.MessageTextService;
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

    @Autowired
    private MessageTextService messageTextService;

    @ApiOperation(value = "获取系统配置")
    @GetMapping("/config")
    public R config(HttpServletRequest httpServletRequest) {
        Map<String, String> allParamByMap = paramterService.getAllParamByMap();
        Map<String, MessageText> allMessageMap = messageTextService.getAllMessageMap();

        JSONObject obj = new JSONObject();
        // 在线客服
        obj.put("onlineService", allParamByMap.get("online_service"));
        // 首页弹窗
        obj.put("homeMsg", allMessageMap.get("home_msg").getContent());
        // 首页轮播
        obj.put("homeNotice", allMessageMap.get("home_notice").getContent());

        return R.ok().put("data", obj);
    }
}

package com.juhai.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.commons.entity.CustomerService;
import com.juhai.commons.entity.MessageText;
import com.juhai.commons.service.CustomerServiceService;
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

    @Autowired
    private CustomerServiceService customerServiceService;

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

        obj.put("webDomain", allParamByMap.get("web_domain"));

        return R.ok().put("data", obj);
    }

    @ApiOperation(value = "获取客服列表")
    @GetMapping("/customerService/list")
    public R customerService(HttpServletRequest httpServletRequest) {
        List<CustomerService> list = customerServiceService.list(
                new LambdaQueryWrapper<CustomerService>()
                        .eq(CustomerService::getStatus, 0)
                        .orderByDesc(CustomerService::getPxh)
        );

        JSONArray array = new JSONArray();
        for (CustomerService temp : list) {
            JSONObject obj = new JSONObject();
            obj.put("serviceName", temp.getServiceName());
            obj.put("link", temp.getServiceLink());
            obj.put("workTime", temp.getWorkTime());
            obj.put("remark", temp.getRemake());
            array.add(obj);
        }

        return R.ok().put("data", array);
    }
}

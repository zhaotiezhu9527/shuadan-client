package com.juhai.api.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.CustomerService;
import com.juhai.commons.entity.MessageText;
import com.juhai.commons.entity.User;
import com.juhai.commons.service.CustomerServiceService;
import com.juhai.commons.service.MessageTextService;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.service.UserService;
import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取系统配置")
    @GetMapping("/config")
    public R config(HttpServletRequest httpServletRequest) {
        Map<String, String> allParamByMap = paramterService.getAllParamByMap();
        Map<String, MessageText> allMessageMap = messageTextService.getAllMessageMap();

        String onlineService = allParamByMap.get("online_service");

        String pankou = allParamByMap.get("pankou");
        if (StringUtils.equals(pankou, "liehuo")) {
            String userName = JwtUtils.getUserName(httpServletRequest);
            if (StringUtils.isNotBlank(userName)) {
                User userByName = userService.getUserByName(userName);
                onlineService += "?userName=" + userByName.getUserName() + "&phone=" + userByName.getPhone() + "&realName=" + userByName.getRealName();
            }
        } else if (StringUtils.equals(pankou, "facai")) {
            String userName = JwtUtils.getUserName(httpServletRequest);
            if (StringUtils.isNotBlank(userName)) {
                User userByName = userService.getUserByName(userName);
//                onlineService += "?userName=" + userByName.getUserName() + "&phone=" + userByName.getPhone() + "&realName=" + userByName.getRealName();
                JSONObject obj = new JSONObject();
                obj.put("tel", userByName.getUserName());
                if (StringUtils.isNotBlank(userByName.getRealName())) {
                    obj.put("name", userByName.getRealName());
                }
                onlineService += "&metadata=" + obj;
            }
        }

        JSONObject obj = new JSONObject();
        // 在线客服
        obj.put("onlineService", onlineService);
        // 首页弹窗
        obj.put("homeMsg", allMessageMap.get("home_msg").getContent());
        // 首页轮播
        obj.put("homeNotice", allMessageMap.get("home_notice").getContent());

        obj.put("webDomain", allParamByMap.get("web_domain"));

        // 汇率
        obj.put("huilv", MapUtil.getDouble(allParamByMap, "huilv", 1.0));

        return R.ok().put("data", obj);
    }

    @ApiOperation(value = "获取首页奖金列表")
    @GetMapping("/bonuslist")
    public R bonuslist(HttpServletRequest httpServletRequest) {
        String date = DateUtil.formatDate(new Date());
        String key = "bonuslist:" + date;
        String arrStr = redisTemplate.opsForValue().get(key);
        String lang = httpServletRequest.getHeader("lang");

        if (StringUtils.isNotBlank(arrStr)) {
            return R.ok().put("data", JSONArray.parseArray(arrStr));
        }
        List<JSONObject> arr = new ArrayList<>();
        List<String> zhphones = Arrays.asList("130", "131", "132", "155", "156", "186", "185", "134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159", "182", "183", "187", "188", "");
        List<String> ynphones = Arrays.asList("0984", "0982","0985","0903","8986","0901");
        List<String> rbphones = Arrays.asList("080", "090");
        if (StringUtils.equals(lang, "vi_VN")) {
            for (int i = 0; i < 20; i++) {
                JSONObject obj = new JSONObject();
                String s = ynphones.get(RandomUtil.randomInt(0, ynphones.size() - 1));
                obj.put("phone", s + "****" + RandomUtil.randomNumbers(3));
                obj.put("price", RandomUtil.randomLong(1000, 100000) * 100);
                obj.put("date", DateUtil.format(new Date(), "dd-MM-yyyy"));
                arr.add(obj);
            }
        } else if (StringUtils.equals(lang, "ja_JP")) {
            for (int i = 0; i < 20; i++) {
                JSONObject obj = new JSONObject();
                Collections.shuffle(rbphones);
                obj.put("phone", rbphones.get(0) + "****" + RandomUtil.randomNumbers(4));
                obj.put("price", RandomUtil.randomLong(100, 10000) * 100);
                obj.put("date", DateUtil.formatDate(new Date()));
                arr.add(obj);
            }
        }else {
            for (int i = 0; i < 20; i++) {
                JSONObject obj = new JSONObject();
                String s = zhphones.get(RandomUtil.randomInt(0, zhphones.size() - 1));
                String phone = s + RandomUtil.randomNumbers(8);
                obj.put("phone", DesensitizedUtil.mobilePhone(phone));
                obj.put("price", RandomUtil.randomLong(10, 1000) * 100);
                obj.put("date", DateUtil.formatDate(new Date()));
                arr.add(obj);
            }
        }
        redisTemplate.opsForValue().set(key, JSON.toJSONString(arr), 1, TimeUnit.HOURS);
        return R.ok().put("data", arr);
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

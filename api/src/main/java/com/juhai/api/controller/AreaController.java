package com.juhai.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.*;
import com.juhai.commons.service.AreaService;
import com.juhai.commons.service.MessageTextService;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.service.UserService;
import com.juhai.commons.utils.R;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "专区相关", tags = "专区相关")
@RequestMapping("/area")
@RestController
public class AreaController {

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取专区列表")
    @GetMapping("/list")
    public R list(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        // 用户信息
        JoinLambdaWrapper<User> wrapper = new JoinLambdaWrapper<>(User.class);
        wrapper.eq(User::getUserName, userName);
        wrapper.leftJoin(Level.class,Level::getId,User::getLevelId).oneToOneSelect(User::getLevel, Level.class).end();
        User user = userService.joinGetOne(wrapper, User.class);

        // 区域信息
        JoinLambdaWrapper<Area> areaWrapper = new JoinLambdaWrapper<>(Area.class);
        areaWrapper.eq(Area::getStatus, 0);
        areaWrapper.leftJoin(Level.class,Level::getId,Area::getLevelId).oneToOneSelect(Area::getLevel, Level.class).end();
        areaWrapper.orderByDesc(Area::getPxh);
        List<Area> areas = areaService.joinList(areaWrapper, Area.class);

        Map<String, String> params = paramterService.getAllParamByMap();
        String resourceDomain = params.get("resource_domain");

        JSONArray array = new JSONArray();
        for (Area area : areas) {
            if (area.getLevel() == null) {
                continue;
            }
            JSONObject object = new JSONObject();
            boolean flag = false;
            if (area.getLevel().getLevelValue() <= user.getLevel().getLevelValue()) {
                flag = true;
            }
            object.put("unlock", flag);
            object.put("areaImg", resourceDomain + area.getAreaIcon());
            object.put("areaName", area.getAreaName());
            object.put("areaId", area.getId());
            object.put("remark", area.getRemark());
            object.put("levelImg", resourceDomain + area.getLevel().getLevelIcon());
            array.add(object);
        }

        return R.ok().put("data", array);
    }

    @ApiOperation(value = "根据ID获取专区详情")
    @GetMapping("/detail/{id}")
    public R detail(HttpServletRequest httpServletRequest, @PathVariable(value = "id") String id) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        // 区域信息
        JoinLambdaWrapper<Area> areaWrapper = new JoinLambdaWrapper<>(Area.class);
        areaWrapper.eq(Area::getStatus, 0);
        areaWrapper.eq(Area::getId, id);
        areaWrapper.leftJoin(Level.class,Level::getId,Area::getLevelId).oneToOneSelect(Area::getLevel, Level.class).end();
        areaWrapper.orderByDesc(Area::getPxh);
        Area area = areaService.joinGetOne(areaWrapper, Area.class);

        Map<String, String> params = paramterService.getAllParamByMap();
        String resourceDomain = params.get("resource_domain");

        JSONObject object = new JSONObject();
        object.put("areaImg", resourceDomain + area.getAreaIcon());
        object.put("areaName", area.getAreaName());
        object.put("areaId", area.getId());
        object.put("remark", area.getRemark());
        object.put("levelImg", resourceDomain + area.getLevel().getLevelIcon());
        object.put("commission", area.getLevel().getCommissionRate());

        return R.ok().put("data", object);
    }
}

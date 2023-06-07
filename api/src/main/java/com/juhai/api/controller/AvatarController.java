package com.juhai.api.controller;

import cn.hutool.core.collection.CollStreamUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.Avatar;
import com.juhai.commons.entity.Paramter;
import com.juhai.commons.entity.User;
import com.juhai.commons.service.AvatarService;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.service.UserService;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "头像相关", tags = "头像相关")
@RequestMapping("/avatar")
@RestController
public class AvatarController {

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取头像列表")
    @GetMapping("/list")
    public R list(HttpServletRequest httpServletRequest) {

        Map<String, String> params = paramterService.getAllParamByMap();
        String resourceDomain = params.get("resource_domain");

        List<Avatar> list = avatarService.list(new LambdaQueryWrapper<Avatar>().eq(Avatar::getStatus, 0).orderByDesc(Avatar::getPxh));
        JSONArray array = new JSONArray();
        for (Avatar avatar : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", avatar.getId());
            obj.put("url", resourceDomain + avatar.getImgUrl());
            array.add(obj);
        }
        return R.ok().put("data", array);
    }

    @ApiOperation(value = "设置用户头像")
    @PostMapping("/set/{id}")
    public R set(HttpServletRequest httpServletRequest, @PathVariable(value = "id") String id) {
        Avatar avatar = avatarService.getById(id);
        if (avatar == null) {
            return R.error(MsgUtil.get("system.param.err"));
        }

        String userName = JwtUtils.getUserName(httpServletRequest);
        userService.update(
                new UpdateWrapper<User>().lambda()
                        .eq(User::getUserName, userName)
                        .set(User::getAvatarId, avatar.getId())
        );

        return R.ok();
    }
}

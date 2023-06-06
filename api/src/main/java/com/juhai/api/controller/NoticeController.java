package com.juhai.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.commons.entity.Notice;
import com.juhai.commons.service.NoticeService;
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

@Slf4j
@Api(value = "信息公告相关", tags = "信息公告相关")
@RequestMapping("/notice")
@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "获取信息公告列表")
    @GetMapping("/list")
    public R config(HttpServletRequest httpServletRequest) {
        List<Notice> list = noticeService.list(new LambdaQueryWrapper<Notice>().eq(Notice::getStatus, 0).orderByDesc(Notice::getCreateTime));

        JSONArray array = new JSONArray();
        for (Notice notice : list) {
            JSONObject object = new JSONObject();
            object.put("title", notice.getTitle());
            object.put("content", notice.getContent());
            object.put("pushTime", notice.getCreateTime());
            array.add(object);
        }

        return R.ok().put("data", array);
    }
}

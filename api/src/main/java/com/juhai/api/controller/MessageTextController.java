package com.juhai.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.commons.entity.MessageText;
import com.juhai.commons.entity.Notice;
import com.juhai.commons.service.MessageTextService;
import com.juhai.commons.service.NoticeService;
import com.juhai.commons.utils.R;
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
@Api(value = "文本消息相关", tags = "文本消息相关")
@RequestMapping("/message")
@RestController
public class MessageTextController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MessageTextService messageTextService;

    @ApiOperation(value = "代码获取文本")
    @GetMapping("/{code}")
    public R config(HttpServletRequest httpServletRequest, @PathVariable(value = "code") String code) {
        MessageText message = messageTextService.getOne(new LambdaQueryWrapper<MessageText>().eq(MessageText::getCode, code));

        JSONObject obj = new JSONObject();
        obj.put("title", message == null ? "" : message.getTitle());
        obj.put("content", message == null ? "" : message.getContent());
        return R.ok().put("data", obj);
    }
}

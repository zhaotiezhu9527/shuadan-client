package com.juhai.commons.service.impl;

import cn.hutool.core.collection.CollStreamUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.MessageText;
import com.juhai.commons.entity.Paramter;
import com.juhai.commons.service.MessageTextService;
import com.juhai.commons.mapper.MessageTextMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_message_text】的数据库操作Service实现
* @createDate 2023-06-04 19:23:33
*/
@Service
public class MessageTextServiceImpl extends ServiceImpl<MessageTextMapper, MessageText>
    implements MessageTextService{

    @Override
    public Map<String, MessageText> getAllMessageMap() {
        List<MessageText> list = list();
        return CollStreamUtil.toMap(list, MessageText::getCode, messageText -> messageText);
    }
}





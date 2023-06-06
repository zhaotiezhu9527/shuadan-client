package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Notice;
import com.juhai.commons.service.NoticeService;
import com.juhai.commons.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_notice】的数据库操作Service实现
* @createDate 2023-06-06 16:31:41
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}





package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.UserLog;
import com.juhai.commons.service.UserLogService;
import com.juhai.commons.mapper.UserLogMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_user_log】的数据库操作Service实现
* @createDate 2023-05-18 16:52:23
*/
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog>
    implements UserLogService{

}





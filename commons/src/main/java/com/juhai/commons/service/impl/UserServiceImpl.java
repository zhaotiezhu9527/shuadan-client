package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.User;
import com.juhai.commons.service.UserService;
import com.juhai.commons.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2023-06-04 19:23:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public User getUserByName(String userName) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
    }
}





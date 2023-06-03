package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.User;
import com.juhai.commons.service.UserService;
import com.juhai.commons.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
* @author Administrator
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2023-05-18 15:34:56
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByName(String userName) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
    }

    @Override
    public void updateUserBalance(String userName, BigDecimal balance) throws Exception {
        int updateUserBalance = userMapper.updateUserBalance(userName, balance);
        if (updateUserBalance <= 0) {
            throw new Exception("修改用户余额失败.");
        }
    }
}





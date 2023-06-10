package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.User;
import com.juhai.commons.service.UserService;
import com.juhai.commons.mapper.UserMapper;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Administrator
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2023-06-04 19:23:47
*/
@Service
public class UserServiceImpl extends JoinServiceImpl<UserMapper, User>
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

    @Override
    public void batchUpdateReport(List<User> list) throws Exception {
        int i = userMapper.batchUpdateReport(list);
        if (i <= 0) {
            throw new Exception("修改日报表失败");
        }
    }
}





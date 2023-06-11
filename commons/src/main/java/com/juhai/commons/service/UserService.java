package com.juhai.commons.service;

import com.juhai.commons.entity.DayReport;
import com.juhai.commons.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.mhb.mybatisplus.plugln.base.service.JoinIService;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Administrator
* @description 针对表【t_user】的数据库操作Service
* @createDate 2023-06-04 19:23:47
*/
public interface UserService extends JoinIService<User> {

    User getUserByName(String userName);

    void updateUserBalance(String userName, BigDecimal balance) throws Exception;

    void batchUpdateReport(List<User> list) throws Exception;
}

package com.juhai.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author Administrator
* @description 针对表【t_user】的数据库操作Mapper
* @createDate 2023-05-18 15:34:56
* @Entity com.juhai.commons.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 修改用户余额
     * @param userName
     * @param balance
     * @return
     */
    int updateUserBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance);
}





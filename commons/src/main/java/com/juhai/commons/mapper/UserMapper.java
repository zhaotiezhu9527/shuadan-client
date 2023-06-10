package com.juhai.commons.mapper;

import com.juhai.commons.entity.DayReport;
import com.juhai.commons.entity.User;
import icu.mhb.mybatisplus.plugln.base.mapper.JoinBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Administrator
* @description 针对表【t_user】的数据库操作Mapper
* @createDate 2023-06-06 13:32:42
* @Entity com.juhai.commons.entity.User
*/
public interface UserMapper extends JoinBaseMapper<User> {

    /**
     * 修改用户余额
     * @param userName
     * @param balance
     * @return
     */
    int updateUserBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance);


    int batchUpdateReport(List<User> list);
}





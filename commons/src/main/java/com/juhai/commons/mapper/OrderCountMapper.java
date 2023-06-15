package com.juhai.commons.mapper;

import com.juhai.commons.entity.OrderCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【t_order_count】的数据库操作Mapper
* @createDate 2023-06-14 17:44:51
* @Entity com.juhai.commons.entity.OrderCount
*/
public interface OrderCountMapper extends BaseMapper<OrderCount> {

    int insertOrUpdate(@Param("report") OrderCount report);

}





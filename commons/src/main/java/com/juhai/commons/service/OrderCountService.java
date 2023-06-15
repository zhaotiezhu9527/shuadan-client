package com.juhai.commons.service;

import com.juhai.commons.entity.DayReport;
import com.juhai.commons.entity.OrderCount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_order_count】的数据库操作Service
* @createDate 2023-06-14 17:44:51
*/
public interface OrderCountService extends IService<OrderCount> {

    void insertOrUpdate(OrderCount report) throws Exception;

}

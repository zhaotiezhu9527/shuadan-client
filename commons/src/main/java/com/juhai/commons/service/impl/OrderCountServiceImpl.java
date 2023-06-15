package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.OrderCount;
import com.juhai.commons.service.OrderCountService;
import com.juhai.commons.mapper.OrderCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_order_count】的数据库操作Service实现
* @createDate 2023-06-14 17:44:51
*/
@Service
public class OrderCountServiceImpl extends ServiceImpl<OrderCountMapper, OrderCount>
    implements OrderCountService{

    @Autowired
    private OrderCountMapper orderCountMapper;

    @Override
    public void insertOrUpdate(OrderCount report) throws Exception {
        int i = orderCountMapper.insertOrUpdate(report);
        if (i <= 0) {
            throw new Exception("修改日报表失败");
        }
    }
}





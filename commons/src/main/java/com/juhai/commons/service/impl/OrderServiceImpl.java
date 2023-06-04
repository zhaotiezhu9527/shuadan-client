package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Order;
import com.juhai.commons.service.OrderService;
import com.juhai.commons.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2023-06-04 19:23:37
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}





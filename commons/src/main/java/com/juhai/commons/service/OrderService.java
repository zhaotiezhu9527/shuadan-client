package com.juhai.commons.service;

import com.juhai.commons.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juhai.commons.utils.PageUtils;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_order】的数据库操作Service
* @createDate 2023-05-18 15:37:58
*/
public interface OrderService extends IService<Order> {

    PageUtils queryPage(Map<String,Object> params);
}

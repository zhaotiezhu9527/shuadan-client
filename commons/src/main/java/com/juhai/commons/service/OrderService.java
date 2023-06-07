package com.juhai.commons.service;

import com.juhai.commons.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juhai.commons.utils.PageUtils;
import icu.mhb.mybatisplus.plugln.base.service.JoinIService;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_order】的数据库操作Service
* @createDate 2023-06-04 19:23:37
*/
public interface OrderService extends JoinIService<Order> {

    PageUtils queryPage(Map<String,Object> params);
}

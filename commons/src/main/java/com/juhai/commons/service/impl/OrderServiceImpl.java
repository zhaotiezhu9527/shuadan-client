package com.juhai.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Account;
import com.juhai.commons.entity.Order;
import com.juhai.commons.service.OrderService;
import com.juhai.commons.mapper.OrderMapper;
import com.juhai.commons.utils.PageUtils;
import com.juhai.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2023-05-18 15:37:58
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<Order> wrapper = new QueryWrapper<Order>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), Order::getUserName, userName);

        String status = MapUtil.getStr(params, "status");
        wrapper.eq(StringUtils.isNotBlank(status), Order::getStatus, status);

        wrapper.orderByDesc(Order::getOrderTime);

        IPage<Order> page = this.page(
                new Query<Order>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}





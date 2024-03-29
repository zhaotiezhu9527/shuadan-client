package com.juhai.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juhai.commons.entity.Goods;
import com.juhai.commons.entity.Order;
import com.juhai.commons.mapper.OrderMapper;
import com.juhai.commons.service.OrderService;
import com.juhai.commons.utils.PageUtils;
import com.juhai.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2023-06-04 19:23:37
*/
@Service
public class OrderServiceImpl extends JoinServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        JoinLambdaWrapper<Order> wrapper = new JoinLambdaWrapper<>(Order.class);

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), Order::getUserName, userName);

        String status = MapUtil.getStr(params, "status");
        if (StringUtils.isBlank(status)) {
            wrapper.in(Order::getStatus, Arrays.asList(0, 1, 2, 4));
        } else if (StringUtils.equals(status, "0")) {
            wrapper.in(Order::getStatus, Arrays.asList(0, 4));
        } else {
            wrapper.eq(Order::getStatus, status);
        }

        wrapper.leftJoin(Goods.class,Goods::getId,Order::getGoodsId).oneToOneSelect(Order::getGoods, Goods.class).end();

        wrapper.orderByDesc(Order::getOrderTime);

        IPage<Order> page = this.joinPage(new Query<Order>().getPage(params), wrapper, Order.class);
        return new PageUtils(page);
    }
}





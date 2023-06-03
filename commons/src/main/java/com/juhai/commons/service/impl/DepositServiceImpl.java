package com.juhai.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Deposit;
import com.juhai.commons.entity.Order;
import com.juhai.commons.service.DepositService;
import com.juhai.commons.mapper.DepositMapper;
import com.juhai.commons.utils.PageUtils;
import com.juhai.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_deposit】的数据库操作Service实现
* @createDate 2023-05-18 15:37:54
*/
@Service
public class DepositServiceImpl extends ServiceImpl<DepositMapper, Deposit>
    implements DepositService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<Deposit> wrapper = new QueryWrapper<Deposit>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), Deposit::getUserName, userName);

        String status = MapUtil.getStr(params, "status");
        wrapper.eq(StringUtils.isNotBlank(status), Deposit::getStatus, status);

        wrapper.orderByDesc(Deposit::getOptTime);

        IPage<Deposit> page = this.page(
                new Query<Deposit>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}





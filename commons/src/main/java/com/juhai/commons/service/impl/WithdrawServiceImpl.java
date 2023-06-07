package com.juhai.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Withdraw;
import com.juhai.commons.service.WithdrawService;
import com.juhai.commons.mapper.WithdrawMapper;
import com.juhai.commons.utils.PageUtils;
import com.juhai.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_withdraw】的数据库操作Service实现
* @createDate 2023-06-04 19:23:53
*/
@Service
public class WithdrawServiceImpl extends ServiceImpl<WithdrawMapper, Withdraw>
    implements WithdrawService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<Withdraw> wrapper = new QueryWrapper<Withdraw>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), Withdraw::getUserName, userName);

        String status = MapUtil.getStr(params, "status");
        wrapper.eq(StringUtils.isNotBlank(status), Withdraw::getStatus, status);

        wrapper.orderByDesc(Withdraw::getOrderTime);

        IPage<Withdraw> page = this.page(
                new Query<Withdraw>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}





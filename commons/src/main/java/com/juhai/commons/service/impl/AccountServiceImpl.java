package com.juhai.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Account;
import com.juhai.commons.service.AccountService;
import com.juhai.commons.mapper.AccountMapper;
import com.juhai.commons.utils.PageUtils;
import com.juhai.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_account】的数据库操作Service实现
* @createDate 2023-06-04 19:23:12
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<Account> wrapper = new QueryWrapper<Account>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), Account::getUserName, userName);

        wrapper.orderByDesc(Account::getOptTime);

        IPage<Account> page = this.page(
                new Query<Account>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}





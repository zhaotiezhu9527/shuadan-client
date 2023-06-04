package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Account;
import com.juhai.commons.service.AccountService;
import com.juhai.commons.mapper.AccountMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_account】的数据库操作Service实现
* @createDate 2023-06-04 19:23:12
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

}





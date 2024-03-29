package com.juhai.commons.service;

import com.juhai.commons.entity.Deposit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juhai.commons.utils.PageUtils;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_deposit】的数据库操作Service
* @createDate 2023-06-04 19:23:23
*/
public interface DepositService extends IService<Deposit> {

    PageUtils queryPage(Map<String,Object> params);

}

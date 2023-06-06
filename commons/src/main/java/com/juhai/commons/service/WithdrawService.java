package com.juhai.commons.service;

import com.juhai.commons.entity.Withdraw;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juhai.commons.utils.PageUtils;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_withdraw】的数据库操作Service
* @createDate 2023-06-04 19:23:53
*/
public interface WithdrawService extends IService<Withdraw> {

    PageUtils queryPage(Map<String,Object> params);

}

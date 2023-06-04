package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.CustomerService;
import com.juhai.commons.service.CustomerServiceService;
import com.juhai.commons.mapper.CustomerServiceMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_customer_service】的数据库操作Service实现
* @createDate 2023-06-04 19:23:20
*/
@Service
public class CustomerServiceServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService>
    implements CustomerServiceService{

}





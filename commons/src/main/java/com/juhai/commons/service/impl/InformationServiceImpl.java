package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Information;
import com.juhai.commons.mapper.InformationMapper;
import com.juhai.commons.service.InformationService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_information】的数据库操作Service实现
* @createDate 2023-12-14 17:08:48
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

}




